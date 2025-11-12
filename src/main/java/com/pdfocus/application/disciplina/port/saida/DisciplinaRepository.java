package com.pdfocus.application.disciplina.port.saida; // <-- ESTA É A LINHA MAIS IMPORTANTE

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de Saída (Interface) para operações de persistência de Disciplina.
 * <p>
 * Define o "contrato" que a camada de Aplicação espera que a camada de
 * Infraestrutura (Adapters) implemente.
 * </p>
 * <p>
 * Esta interface foi refatorada (Pilar 3) para remover métodos duplicados
 * e incluir validações de segurança (como 'findByIdAndUsuarioId').
 * </p>
 */
public interface DisciplinaRepository {

    /**
     * Salva ou atualiza uma disciplina no banco de dados.
     *
     * @param disciplina O modelo de domínio a ser salvo.
     * @return A disciplina salva.
     */
    Disciplina salvar(Disciplina disciplina);

    /**
     * Busca uma disciplina pelo seu ID (sem checagem de usuário).
     *
     * @param id O ID da disciplina.
     * @return Um Optional contendo a disciplina.
     */
    Optional<Disciplina> findById(UUID id);

    /**
     * Deleta uma disciplina pelo seu ID.
     *
     * @param id O ID da disciplina a ser deletada.
     */
    void deletarPorId(UUID id);

    /**
     * Busca uma disciplina pelo seu ID e pelo ID do usuário proprietário.
     * (Implementação de segurança multi-tenancy).
     *
     * @param id O ID da disciplina.
     * @param usuarioId O ID do usuário.
     * @return Um Optional contendo a disciplina, se encontrada e pertencente ao usuário.
     */
    Optional<Disciplina> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    /**
     * Conta o número total de disciplinas de um usuário.
     *
     * @param usuario O usuário.
     * @return O total de disciplinas.
     */
    long countByUsuario(Usuario usuario);

    /**
     * Lista todas as disciplinas pertencentes a um usuário específico.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de {@link Disciplina} do usuário (pode ser vazia).
     */
    List<Disciplina> listaTodasPorUsuario(UUID usuarioId);
}