package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.UUID;

public interface ListarResumosUseCase {

    List<Resumo> buscarTodosPorUsuario(UUID usuarioId);

    List<Resumo> buscarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId);
}
