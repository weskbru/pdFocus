package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.models.Resumo;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável por recuperar um {@link Resumo}
 * específico com base em seu identificador único.
 * <p>
 * Este caso de uso garante que o resumo retornado pertença ao usuário autenticado,
 * evitando acesso indevido a dados de outros usuários.
 * </p>
 */
public interface ObterResumoPorIdUseCase {

    /**
     * Executa a busca de um resumo específico pelo seu ID e pelo ID do usuário proprietário.
     *
     * @param id         O identificador único do resumo a ser buscado.
     * @param usuarioId  O identificador único do usuário que deve ser o proprietário do resumo.
     * @return Um {@link Optional} contendo o {@link Resumo} se encontrado e pertencente ao usuário,
     *         ou um Optional vazio caso contrário.
     */
    Optional<Resumo> executar(UUID id, UUID usuarioId);
}
