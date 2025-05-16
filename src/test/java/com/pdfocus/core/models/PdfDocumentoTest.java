package com.pdfocus.core.models;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PdfDocumentoTest {

    // Teste básico: cria um PdfDocumento com dados válidos e verifica se os getters retornam o esperado
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

    // Teste que verifica se o construtor lança exceção quando o ID for nulo
    @Test
    void naoCriaComIdNulo() {
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Descrição");

        // Espera que o construtor lance IllegalArgumentException com a mensagem "ID não pode ser nulo"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(null, "Apostila", "/caminho/arquivo.pdf", disciplina);
        });
        assertEquals("ID não pode ser nulo", exception.getMessage());
    }

    // Teste que verifica se o construtor lança exceção quando o nome do PDF for nulo ou vazio
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

    // Teste que verifica se o construtor lança exceção quando o caminho do PDF for nulo ou vazio
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

    // Teste que verifica se o construtor lança exceção quando a disciplina for nula
    @Test
    void naoCriaComDisciplinaNula() {
        // Espera que o construtor lance IllegalArgumentException com a mensagem "Disciplina não pode ser nula"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PdfDocumento(UUID.randomUUID(), "Apostila", "/caminho/arquivo.pdf", null);
        });
        assertEquals("Disciplina não pode ser nula", exception.getMessage());
    }
}
