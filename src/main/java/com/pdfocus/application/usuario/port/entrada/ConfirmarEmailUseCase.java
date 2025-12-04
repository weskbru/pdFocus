package com.pdfocus.application.usuario.port.entrada;

import com.pdfocus.application.usuario.dto.AuthenticationResponse;

public interface ConfirmarEmailUseCase {
    AuthenticationResponse executar(String token);
}