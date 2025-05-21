package com.pdfocus.application.resumo.port.entrada;


import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.core.models.Resumo;

import java.util.UUID;

public interface CriarResumoUseCase {
    Resumo execute(CriarResumoCommand command);

}
