package com.pdfocus.core.models;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ResumoTeste {

    // Teste positivo: deve criar um resumo válido com todos os campos corretos
    @Test
    void deveCriarResumoComDadosValidos() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Matemática: Álgebra Linear";
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Conteúdo de exatas");

        Resumo resumo = new Resumo(id, titulo, conteudo, usuarioId, disciplina);

        assertEquals(id, resumo.getId());
        assertEquals(usuarioId, resumo.getUsuarioId());
        assertEquals(titulo, resumo.getTitulo());
        assertEquals(conteudo, resumo.getConteudo());
        assertEquals(disciplina, resumo.getDisciplina());
    }

    // Testes negativos (validação de dados obrigatórios)

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        UUID usuarioId = UUID.randomUUID();
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(null, "Título", "Conteúdo", usuarioId, disciplina);
        });

        assertEquals("ID não pode ser nulo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioIdForNulo() {
        UUID id = UUID.randomUUID();
        String titulo = "Resumo de Matemática: Álgebra Linear";
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, titulo, conteudo, null, disciplina);
        });

        assertEquals("Usuario responsavel não pode ser nulo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTituloForNulo() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, null, conteudo, usuarioId, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTituloForVazio() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este é um conteúdo válido";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "História", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, "", conteudo, usuarioId, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTituloForApenasEspaco() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este é um conteúdo válido";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "História", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, "   ", conteudo, usuarioId, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForNulo() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, titulo, null, usuarioId, disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForVazio() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, titulo, "", usuarioId, disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoConteudoForApenasEspaco() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, titulo, "    ", usuarioId, disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDisciplinaForNula() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        String conteudo = "Este é um resumo válido";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, titulo, conteudo, usuarioId, null);
        });

        assertEquals("Disciplina não pode ser nula", exception.getMessage());
    }

}
