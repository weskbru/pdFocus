package com.pdfocus.application.resumo.port.entrada;


import com.pdfocus.core.models.Resumo;

public interface CriarResumoUseCase {
    executar(comand: CriarResumoComand): Resumo
}
