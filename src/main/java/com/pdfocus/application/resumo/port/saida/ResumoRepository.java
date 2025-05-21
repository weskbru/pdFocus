package com.pdfocus.application.resumo.port.saida;

import com.pdfocus.core.models.Resumo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumoRepository {

    Resumo salvar(Resumo resumo);

    Optional<Resumo> buscaPorId(UUID id);

    List<Resumo> buscarTodos();

    void deletar(UUID id);
}
