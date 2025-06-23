package com.pdfocus.application.disciplina.port.saida;

import com.pdfocus.core.models.Disciplina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplinaRepository {
    Optional<Disciplina> findById(UUID id);
    Disciplina salvar(Disciplina disciplina);
    List<Disciplina> listarTodas();
    void deletarPorId(UUID id);
}
