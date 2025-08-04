package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.ListarMateriaisUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.core.models.Material;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para listar os materiais de um usuário em uma disciplina.
 */
@Service
public class DefaultListarMateriaisService implements ListarMateriaisUseCase {

    private final MaterialRepository materialRepository;

    public DefaultListarMateriaisService(MaterialRepository materialRepository) {
        this.materialRepository = Objects.requireNonNull(materialRepository, "MaterialRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * A busca é transacional e somente leitura para otimização.
     * </p>
     * @throws IllegalArgumentException se {@code disciplinaId} ou {@code usuarioId} forem nulos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Material> executar(UUID disciplinaId, UUID usuarioId) {
        // 1. Valida as entradas para garantir que os IDs necessários foram fornecidos.
        Objects.requireNonNull(disciplinaId, "O ID da disciplina não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        // 2. Delega a chamada para a porta do repositório, que sabe como buscar os dados.
        return materialRepository.listarPorDisciplinaEUsuario(disciplinaId, usuarioId);
    }
}
