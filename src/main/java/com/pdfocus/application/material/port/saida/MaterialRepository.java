package com.pdfocus.application.material.port.saida;

import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Define o contrato (Porta de Saída) para interações de persistência
 * com a entidade de domínio {@link Material}.
 */
public interface MaterialRepository {

    /**
     * Salva (cria ou atualiza) um material.
     * @param material O objeto de domínio a ser salvo.
     * @return O material salvo.
     */
    Material salvar(Material material);

    /**
     * Lista todos os materiais de uma disciplina específica que pertencem a um usuário.
     * @param disciplinaId O ID da disciplina.
     * @param usuarioId O ID do usuário.
     * @return Uma lista de {@link Material}.
     */
    List<Material> listarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);

    /**
     * Busca um material pelo seu ID, garantindo que ele pertença ao usuário.
     * @param id O ID do material.
     * @param usuarioId O ID do usuário.
     * @return um {@link Optional} contendo o {@link Material} se encontrado.
     */
    Optional<Material> buscarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Deleta um material, garantindo que ele pertença ao usuário.
     * @param id O ID do material a ser deletado.
     * @param usuarioId O ID do usuário proprietário.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Conta o total de materiais de um usuário.
     * @param usuario O usuário para o qual os materiais serão contados.
     * @return O número total de materiais.
     */
    long countByUsuario(Usuario usuario);

    /**
     * Busca uma lista limitada dos materiais mais recentes de um usuário.
     * @param usuario O usuário para o qual os materiais serão buscados.
     * @return uma Lista de {@link Material}, ordenada do mais recente para o mais antigo.
     */
    List<Material> buscar5MaisRecentesPorUsuario(Usuario usuario);

    /**
     * Busca um material apenas pelo seu ID, sem verificação de usuário.
     * Útil para operações internas do sistema onde a validação de usuário não é necessária.
     *
     * @param id O ID do material.
     * @return um {@link Optional} contendo o {@link Material} se encontrado.
     */
    Optional<Material> buscarPorId(UUID id);

    /**
     * Busca uma página de materiais pertencentes a uma disciplina específica.
     * @param disciplinaId O ID da disciplina.
     * @param pageable Objeto que contém as informações de paginação.
     * @return Uma página (Page) de materiais.
     */
    Page<Material> buscarPorDisciplinaDeFormaPaginada(UUID disciplinaId, Pageable pageable);

    /**
     * Deleta todos os materiais de uma disciplina específica
     * @param disciplinaId ID da disciplina
     */
    void deletarTodosPorDisciplinaId(UUID disciplinaId);
}

