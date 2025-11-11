package com.pdfocus.application.resumo.port.saida;

import com.pdfocus.core.exceptions.ResumoNaoEncontradoException;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumoRepository {


    /**
     * Salva um resumo (cria um novo se não tiver ID, ou atualiza um existente).
     *
     * @param resumo O resumo a ser salvo.
     * @return O resumo salvo (pode conter o ID gerado, por exemplo).
     */
    Resumo salvar(Resumo resumo);

    /**
     * Busca um resumo específico pelo seu ID e pelo ID do usuário proprietário.
     *
     * @param id O ID do resumo.
     * @param usuarioId O ID do usuário proprietário do resumo.
     * @return Um Optional contendo o resumo se encontrado e pertencente ao usuário, ou vazio caso contrário.
     */
    Optional<Resumo> buscarPorIdEUsuario(UUID id, UUID usuarioId);

    /**
     * Lista todos os resumos pertencentes a um usuário específico.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de resumos do usuário. Pode retornar uma lista vazia.
     */
    List<Resumo> buscarTodosPorUsuario(UUID usuarioId);

    /**
     * Lista todos os resumos de uma disciplina específica que pertencem a um usuário.
     *
     * @param disciplinaId O ID da disciplina.
     * @param usuarioId O ID do usuário.
     * @return Uma lista de resumos da disciplina para o usuário. Pode retornar uma lista vazia.
     */
    List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);

    /**
     * Deleta um resumo específico, verificando se pertence ao usuário.
     *
     * @param id O ID do resumo a ser deletado.
     * @param usuarioId O ID do usuário proprietário do resumo.
     * @throws ResumoNaoEncontradoException se o resumo não for encontrado ou não pertencer ao usuário.
     */
    void deletarPorIdEUsuario(UUID id, UUID usuarioId); // Ou void, dependendo da sua preferência de design para feedback de deleção

    long countByUsuario(Usuario usuario);

    /**
     * Deleta todos os resumos de uma disciplina específica
     * @param disciplinaId ID da disciplina
     */
    void deletarTodosPorDisciplinaId(UUID disciplinaId);

}
