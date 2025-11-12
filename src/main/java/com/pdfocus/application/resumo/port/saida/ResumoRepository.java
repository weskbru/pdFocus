package com.pdfocus.application.resumo.port.saida;

import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saída responsável pela persistência de {@link Resumo}.
 * <p>
 * Define as operações que a camada de infraestrutura deve implementar
 * para armazenamento, busca e remoção de resumos.
 * Esta interface abstrai os detalhes do banco de dados, permitindo que
 * o domínio e a aplicação permaneçam desacoplados da tecnologia de persistência.
 * </p>
 *
 * <p>
 * Implementações típicas incluem repositórios JPA, Mongo ou adaptadores customizados.
 * </p>
 */
public interface ResumoRepository {

    /**
     * Persiste um resumo no repositório.
     * <p>
     * Se o resumo não possuir um identificador, um novo registro é criado.
     * Caso já exista, a entidade é atualizada.
     * </p>
     *
     * @param resumo O resumo a ser salvo.
     * @return O resumo salvo, incluindo possíveis alterações (ex.: ID gerado).
     */
    Resumo salvar(Resumo resumo);

    /**
     * Busca um resumo específico pelo seu ID e pelo ID do usuário proprietário.
     *
     * @param id O identificador do resumo.
     * @param usuarioId O identificador do usuário proprietário.
     * @return Um {@link Optional} contendo o resumo, se encontrado e pertencente ao usuário.
     */
    Optional<Resumo> buscarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Lista todos os resumos pertencentes a um determinado usuário.
     *
     * @param usuarioId O identificador do usuário.
     * @return Lista de resumos associados ao usuário.
     *         Pode retornar uma lista vazia, mas nunca {@code null}.
     */
    List<Resumo> buscarTodosPorUsuario(UUID usuarioId);

    /**
     * Lista todos os resumos de uma disciplina específica pertencentes a um usuário.
     *
     * @param disciplinaId O identificador da disciplina.
     * @param usuarioId O identificador do usuário.
     * @return Lista de resumos da disciplina para o usuário.
     *         Pode retornar uma lista vazia.
     */
    List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);

    /**
     * Remove um resumo específico, garantindo que pertence ao usuário.
     *
     * @param id O identificador do resumo a ser removido.
     * @param usuarioId O identificador do usuário proprietário.
     * @throws ResumoNaoEncontradoException se o resumo não for encontrado
     *                                     ou não pertencer ao usuário informado.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Conta a quantidade total de resumos associados a um usuário.
     *
     * @param usuario O usuário a ser analisado.
     * @return O número total de resumos pertencentes ao usuário.
     */
    long countByUsuario(Usuario usuario);

    /**
     * Exclui todos os resumos associados a uma disciplina específica.
     *
     * @param disciplinaId O identificador da disciplina.
     */
    void deletarTodosPorDisciplinaId(UUID disciplinaId);
}
