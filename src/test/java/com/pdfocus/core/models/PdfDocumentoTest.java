package com.pdfocus.core.models;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PdfDocumentoTest {

    /**
     * Teste positivo: criação de um PdfDocumento com todos os dados válidos.
     * Garante que os valores passados ao construtor são corretamente atribuídos aos campos.
     */
    @Test
        void criaPdfDocumentoComDadosValidos() {
            UUID id = UUID.randomUUID();
            Disciplina disciplina = new Disciplina(id, "Matemática", "Descrição");

            PdfDocumento pdf = new PdfDocumento(id, "Apostila", "/caminho/arquivo.pdf", disciplina);

            // Verifica se os atributos do objeto são os mesmos que passamos no construtor
            assertEquals(id, pdf.getId());
            assertEquals("Apostila", pdf.getNome());
            assertEquals("/caminho/arquivo.pdf", pdf.getCaminho());
            assertEquals(disciplina, pdf.getDisciplina());
        }

    /**
     * Teste negativo: impede criação de PdfDocumento com ID nulo.
     * Espera-se uma IllegalArgumentException com mensagem apropriada.
     */
    @Test
    void naoCriaComIdNulo() {
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Descrição");

        // Espera que o construtor lance IllegalArgumentException com a mensagem "ID não pode ser nulo"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(null, "Apostila", "/caminho/arquivo.pdf", disciplina);
        });
        assertEquals("ID não pode ser nulo", exception.getMessage());
    }

    /**
     * Teste negativo: impede criação de PdfDocumento com nome nulo ou apenas espaços.
     * Valida obrigatoriedade do campo nome.
     */
    @Test
    void naoCriaComNomeNuloOuVazio() {
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Descrição");

        // Testa nome nulo
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), null, "/caminho/arquivo.pdf", disciplina);
        });
        assertEquals("Nome do PDF é obrigatório", exception1.getMessage());

        // Testa nome com espaços em branco (vazio)
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), "   ", "/caminho/arquivo.pdf", disciplina);
        });
        assertEquals("Nome do PDF é obrigatório", exception2.getMessage());
    }

    /**
     * Teste negativo: impede criação de PdfDocumento com caminho nulo ou em branco.
     * Garante que o caminho do arquivo seja informado corretamente.
     */
    @Test
    void naoCriaComCaminhoNuloOuVazio() {
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Descrição");

        // Testa caminho nulo
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), "Apostila", null, disciplina);
        });
        assertEquals("Caminho do PDF é obrigatório", exception1.getMessage());

        // Testa caminho com espaços em branco (vazio)
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), "Apostila", "   ", disciplina);
        });
        assertEquals("Caminho do PDF é obrigatório", exception2.getMessage());
    }

    /**
     * Teste negativo: impede criação de PdfDocumento com disciplina nula.
     * Verifica que um PDF precisa estar vinculado a uma disciplina válida.
     */
    @Test
    void naoCriaComDisciplinaNula() {
        // Espera que o construtor lance IllegalArgumentException com a mensagem "Disciplina não pode ser nula"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), "Apostila", "/caminho/arquivo.pdf", null);
        });
        assertEquals("Disciplina não pode ser nula", exception.getMessage());
    }
}
