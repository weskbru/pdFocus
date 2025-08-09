package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.models.Resumo;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para obter um único resumo pelo seu ID,
 * garantindo que ele pertença ao usuário especificado.
 */
public interface ObterResumoPorIdUseCase {

    /**
     * Busca um resumo com base no seu ID e no ID do usuário proprietário.
     *
     * @param id O identificador único do resumo a ser buscado.
     * @param usuarioId O identificador único do usuário que deve ser o proprietário do resumo.
     * @return Um {@link Optional} contendo o {@link Resumo} se encontrado e se pertencer ao usuário,
     * ou um Optional vazio caso contrário.
     */
    Optional<Resumo> executar(UUID id, UUID usuarioId);
}
