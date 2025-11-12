package com.pdfocus.application.resumo.port.entrada;

import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.core.models.Resumo;

import java.util.UUID;

/**
 * Porta de entrada (Use Case) responsável pela geração automática de resumos
 * a partir de materiais em PDF.
 * <p>
 * Este caso de uso orquestra o processo de extração de texto do material,
 * valida a disciplina associada e cria um novo {@link Resumo} no sistema.
 * </p>
 */
public interface GerarResumoAutomaticoUseCase {

    /**
     * Executa a geração automática de um resumo com base em um material PDF existente.
     *
     * @param comando   O comando com os dados necessários (ID do material e parâmetros opcionais).
     * @param usuarioId O identificador do usuário que solicita a geração.
     * @return O {@link Resumo} gerado automaticamente.
     *
     * @throws com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException
     *         Se o material informado não for encontrado.
     * @throws com.pdfocus.core.exceptions.pdf.TextoNaoPodeSerExtraidoException
     *         Se não for possível extrair o texto do PDF.
     * @throws com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException
     *         Se a disciplina associada não existir.
     */
    Resumo executar(CriarResumoDeMaterialCommand comando, UUID usuarioId);
}
