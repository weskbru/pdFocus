package com.pdfocus.application.resumo.port.saida;

import com.pdfocus.core.models.Disciplina;

import java.util.Optional;
import java.util.UUID;

public interface DisciplinaRepository {
    Optional<Disciplina> findById(UUID id);
}
