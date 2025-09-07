package com.pdfocus.application.disciplina.port.saida;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de Saída (Interface de Repositório) para operações de persistência
 * relacionadas à entidade de domínio {@link Disciplina}.
 * <p>
 * Define o contrato que a camada de aplicação usa para interagir com a
 * camada de infraestrutura para persistir e recuperar dados de disciplinas.
 * </p>
 */
public interface DisciplinaRepository {

    /**
     * Salva (cria ou atualiza) uma disciplina.
     *
     * @param disciplina O objeto de domínio {@link Disciplina} a ser salvo.
     * @return A disciplina salva, possivelmente com dados atualizados pela persistência.
     */
    Disciplina salvar(Disciplina disciplina);

    /**
     * Busca uma disciplina pelo seu ID.
     * <p>
     * NOTA: Este método não valida a propriedade do usuário e deve ser usado com
     * cautela, principalmente para lógicas internas que não dependem do contexto do usuário.
     * </p>
     *
     * @param id O ID da disciplina a ser buscada.
     * @return um {@link Optional} contendo a {@link Disciplina} se encontrada, ou vazio caso contrário.
     */
    Optional<Disciplina> findById(UUID id);

    /**
     * Lista todas as disciplinas pertencentes a um usuário específico.
     *
     * @param usuarioId O ID do usuário proprietário das disciplinas.
     * @return Uma lista de {@link Disciplina} do usuário. Retorna uma lista vazia se nenhuma for encontrada.
     */
    List<Disciplina> listaTodasPorUsuario(UUID usuarioId);

    /**
     * Deleta uma disciplina, garantindo que ela pertença ao usuário especificado.
     *
     * @param id O ID da disciplina a ser deletada.
     * @param usuarioId O ID do usuário proprietário.
     * @throws DisciplinaNaoEncontradaException se a disciplina não for encontrada ou não pertencer ao usuário.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Busca uma disciplina específica pelo seu ID, garantindo que ela pertença
     * ao usuário especificado.
     *
     * @param id O ID da disciplina a ser buscada.
     * @param usuarioId O ID do usuário proprietário.
     * @return um {@link Optional} contendo a {@link Disciplina} se encontrada e se
     * pertencer ao usuário, ou vazio caso contrário.
     */
    Optional<Disciplina> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    long countByUsuario(Usuario usuario);

    Optional<Disciplina> buscarPorId(UUID id);
}
