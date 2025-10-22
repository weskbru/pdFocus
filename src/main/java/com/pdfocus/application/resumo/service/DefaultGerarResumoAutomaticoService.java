package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.entrada.GerarResumoAutomaticoUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
import com.pdfocus.application.resumo.port.saida.ResumidorIAPort;
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
    private final ResumoRepository resumoRepository;
    private final ResumidorIAPort resumidorIAPort;

    public DefaultGerarResumoAutomaticoService(
            MaterialRepository materialRepository,
            DisciplinaRepository disciplinaRepository,
            TextExtractorPort textExtractorPort,
            ResumoRepository resumoRepository,
            ResumidorIAPort resumidorIAPort) {
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.textExtractorPort = textExtractorPort;
        this.resumoRepository = resumoRepository;
        this.resumidorIAPort = resumidorIAPort;
    }

    @Override
    public Resumo executar(CriarResumoDeMaterialCommand comando, UUID usuarioId) {
        System.out.println("🚀 INICIANDO GERAÇÃO DE RESUMO AUTOMÁTICO");

        var material = materialRepository.buscarPorIdEUsuario(comando.materialId(), usuarioId)
                .orElseThrow(() -> new MaterialNaoEncontradoException(comando.materialId()));

        Disciplina disciplina = disciplinaRepository.buscarPorId(comando.disciplinaId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(comando.disciplinaId()));

        String conteudo;
        if (comando.conteudo() != null && !comando.conteudo().isBlank()) {
            // Usa o conteúdo fornecido manualmente
            conteudo = comando.conteudo();
            System.out.println("📝 Usando conteúdo manual fornecido");
        } else {
            // FLUXO PRINCIPAL: RESUMO AUTOMÁTICO
            try {
                // 1. Extrair texto do PDF
                System.out.println("🔤 Extraindo texto do PDF...");
                String textoCompleto = textExtractorPort.extrairTexto(material.getNomeStorage());
                System.out.println("✅ Texto extraído: " + textoCompleto.length() + " caracteres");

                // 2. ✅ AGORA GERAR O RESUMO REAL (NOVO!)
                System.out.println("Gerando resumo automático...");
                conteudo = resumidorIAPort.resumir(textoCompleto, 300); // 300 palavras máximo
                System.out.println("Resumo gerado: " + conteudo.length() + " caracteres");

            } catch (Exception e) {
                System.out.println("Erro ao gerar resumo: " + e.getMessage());
                throw new TextoNaoPodeSerExtraidoException(material.getId().toString(), e);
            }
        }

        String titulo = comando.titulo() != null && !comando.titulo().isBlank()
                ? comando.titulo()
                : "Resumo - " + material.getNomeOriginal();

        // Criar e salvar o resumo
        Resumo resumo = Resumo.criarDeMaterial(
                UUID.randomUUID(),
                usuarioId,
                titulo,
                conteudo, // AGORA É O TEXTO RESUMIDO, NÃO O COMPLETO
                disciplina,
                comando.materialId()
        );

        Resumo resumoSalvo = resumoRepository.salvar(resumo);
        System.out.println("Resumo salvo com ID: " + resumoSalvo.getId());

        return resumoSalvo;
    }
}