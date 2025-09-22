package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.core.models.Resumo;
import java.util.UUID;

/**
 * Porta de entrada para a geração automática de resumos baseados em materiais (PDFs).
 * Este caso de uso é responsável por orquestrar a extração de texto do PDF e criação do resumo.
 */
public interface GerarResumoAutomaticoUseCase {

    /**
     * Gera um resumo automaticamente com base em um material (PDF) existente.
     *
     * @param comando DTO contendo os dados necessários para gerar o resumo
     * @param usuarioId ID do usuário que está gerando o resumo
     * @return O resumo gerado automaticamente
     * @throws MaterialNaoEncontradoException se o material não existir
     * @throws TextoNaoPodeSerExtraidoException se não for possível extrair texto do PDF
     * @throws DisciplinaNaoEncontradaException se a disciplina não existir
     */
    Resumo executar(CriarResumoDeMaterialCommand comando, UUID usuarioId);
}