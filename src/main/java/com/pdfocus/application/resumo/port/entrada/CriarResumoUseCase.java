package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.core.models.Resumo;

import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável pela criação de um novo {@link Resumo}.
 * <p>
 * Este caso de uso orquestra a validação de dados e delega a persistência
 * para a camada de saída (portas e adapters).
 * </p>
 */
public interface CriarResumoUseCase {

    /**
     * Executa a criação de um novo resumo com base nos dados fornecidos.
     *
     * @param command   O comando contendo os dados necessários para criar o resumo.
     * @param usuarioId O identificador do usuário autenticado que está criando o resumo.
     * @return O objeto de domínio {@link Resumo} recém-criado e persistido.
     */
    Resumo executar(CriarResumoCommand command, UUID usuarioId);
}
