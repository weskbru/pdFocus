package com.pdfocus.application.resumo.port.entrada;


import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.core.models.Resumo;

/**
 * Caso de uso para a criação de um novo Resumo.
 */
public interface CriarResumoUseCase {

    /**
     * Executa a criação de um novo resumo com base nos dados fornecidos.
     *
     * @param command O comando contendo os dados necessários para criar o resumo.
     * @return O objeto de domínio {@link Resumo} recém-criado.
     */
    Resumo executar(CriarResumoCommand command);

}
