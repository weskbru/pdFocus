package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.DeletarMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para deletar um material de estudo.
 */
@Service
public class DefaultDeletarMaterialService implements DeletarMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final MaterialStoragePort materialStoragePort;

    public DefaultDeletarMaterialService(
            MaterialRepository materialRepository,
            MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é transacional. Ela primeiro busca o material para garantir que ele
     * existe e pertence ao usuário. Se encontrado, apaga o arquivo físico e, em seguida,
     * o registro no banco de dados. Se qualquer etapa falhar, a transação é revertida.
     * </p>
     * @throws MaterialNaoEncontradoException se o material não for encontrado.
     * @throws IllegalArgumentException se qualquer um dos IDs for nulo.
     */
    @Override
    @Transactional
    public void executar(UUID id, UUID usuarioId) {
        // 1. Valida as entradas.
        Objects.requireNonNull(id, "O ID do material não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // 2. Busca o material no banco de dados, validando a propriedade.
        // Isso é crucial para obter o 'nomeStorage' antes de apagar o registro.
        Material material = materialRepository.buscarPorIdEUsuario(id, usuarioId)
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        // 3. Apaga o arquivo físico do armazenamento.
        // Fazemos isso primeiro. Se esta etapa falhar, a transação do banco de dados
        // será revertida e o registro não será apagado, mantendo a consistência.
        materialStoragePort.apagar(material.getNomeStorage());

        // 4. Apaga o registro (metadados) do material no banco de dados.
        materialRepository.deletarPorIdEUsuario(id, usuarioId);
    }
}
