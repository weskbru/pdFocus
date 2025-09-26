package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.entrada.GerarResumoAutomaticoUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.exceptions.TextoNaoPodeSerExtraidoException;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Disciplina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DefaultGerarResumoAutomaticoService implements GerarResumoAutomaticoUseCase {

    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TextExtractorPort textExtractorPort;

    public DefaultGerarResumoAutomaticoService(
            MaterialRepository materialRepository,
            DisciplinaRepository disciplinaRepository,
            TextExtractorPort textExtractorPort) {
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.textExtractorPort = textExtractorPort;
    }

    @Override
    public Resumo executar(CriarResumoDeMaterialCommand comando, UUID usuarioId) {
        var material = materialRepository.buscarPorIdEUsuario(comando.materialId(), usuarioId)
                .orElseThrow(() -> new MaterialNaoEncontradoException(comando.materialId()));

        Disciplina disciplina = disciplinaRepository.buscarPorId(comando.disciplinaId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(comando.disciplinaId()));

        // ✅ LÓGICA CORRIGIDA: Extração de texto condicional
        String conteudo;
        if (comando.conteudo() != null && !comando.conteudo().isBlank()) {
            // Usa o conteúdo fornecido
            conteudo = comando.conteudo();
        } else {
            // Apenas extrai o texto se for necessário
            try {
                conteudo = textExtractorPort.extrairTexto(material.getNomeStorage());
            } catch (Exception e) {
                throw new TextoNaoPodeSerExtraidoException(material.getId().toString(), e);
            }
        }

        String titulo = comando.titulo() != null && !comando.titulo().isBlank()
                ? comando.titulo()
                : "Resumo - " + material.getNomeOriginal();

        return Resumo.criarDeMaterial(
                UUID.randomUUID(),
                usuarioId,
                titulo,
                conteudo,
                disciplina,
                comando.materialId()
        );
    }
}