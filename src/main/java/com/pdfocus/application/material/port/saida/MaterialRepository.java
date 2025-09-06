package com.pdfocus.application.material.port.saida;

import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de Saída (Interface de Repositório) para operações de persistência
 * relacionadas à entidade de domínio {@link Material}.
 */
public interface MaterialRepository {

    /**
     * Salva (cria ou atualiza) um material.
     *
     * @param material O objeto de domínio {@link Material} a ser salvo.
     * @return O material salvo.
     */
    Material salvar(Material material);

    /**
     * Lista todos os materiais de uma disciplina específica que pertencem a um usuário.
     *
     * @param disciplinaId O ID da disciplina.
     * @param usuarioId O ID do usuário.
     * @return Uma lista de {@link Material} da disciplina para o usuário.
     */
    List<Material> listarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);

    /**
     * Busca um material pelo seu ID, garantindo que ele pertença ao usuário.
     *
     * @param id O ID do material.
     * @param usuarioId O ID do usuário.
     * @return um {@link Optional} contendo o {@link Material} se encontrado, ou vazio caso contrário.
     */
    Optional<Material> buscarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Deleta um material, garantindo que ele pertença ao usuário.
     *
     * @param id O ID do material a ser deletado.
     * @param usuarioId O ID do usuário proprietário.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId);

    long countByUsuario(Usuario usuario);
}
