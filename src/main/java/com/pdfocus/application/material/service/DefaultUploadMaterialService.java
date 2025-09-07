package com.pdfocus.application.material.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Material;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para o upload de um novo material.
 */
@Service
public class DefaultUploadMaterialService implements UploadMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final MaterialStoragePort materialStoragePort;

    public DefaultUploadMaterialService(
            MaterialRepository materialRepository,
            DisciplinaRepository disciplinaRepository,
            MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é transacional. Orquestra a validação, o armazenamento físico do ficheiro
     * e a persistência dos seus metadados no banco de dados.
     * </p>
     */
    @Override
    @Transactional
    public Material executar(UploadMaterialCommand command, UUID usuarioId) {
        Objects.requireNonNull(command, "O comando de upload não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // 1. Valida se a disciplina associada existe e pertence ao usuário.
        disciplinaRepository.findByIdAndUsuarioId(command.disciplinaId(), usuarioId)
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.disciplinaId()));

        // 2. Gera um nome de arquivo único para o armazenamento.
        String extensao = StringUtils.getFilenameExtension(command.nomeOriginal());
        String nomeFicheiroStorage = UUID.randomUUID() + "." + extensao;

        // 3. Guarda o arquivo usando a porta de armazenamento.
        materialStoragePort.guardar(nomeFicheiroStorage, command.inputStream());

        // 4. Cria o objeto de domínio Material com todos os metadados.
        // Fazemos isso porque a responsabilidade de gerar a data foi delegada ao banco de dados
        // através da anotação @CreationTimestamp na MaterialEntity.
        Material novoMaterial = Material.criar(
                UUID.randomUUID(),
                command.nomeOriginal(),
                nomeFicheiroStorage,
                command.tipoArquivo(),
                command.tamanho(),
                usuarioId,
                command.disciplinaId(),
                null
        );

        // 5. Salva os metadados do material no banco de dados.
        return materialRepository.salvar(novoMaterial);
    }

}
