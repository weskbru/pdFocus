package com.pdfocus.core.models;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ResumoTest {

    /**
     * Teste de criação bem-sucedida de um resumo com todos os dados válidos.
     * Verifica se os campos são corretamente atribuídos ao objeto `Resumo`.
     */
    @Test
    void deveCriarResumoComDadosValidos() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Matemática: Álgebra Linear";
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Conteúdo de exatas");

        Resumo resumo = new Resumo(id, usuarioId, titulo, conteudo, disciplina);

        assertEquals(id, resumo.getId());
        assertEquals(usuarioId, resumo.getUsuarioId());
        assertEquals(titulo, resumo.getTitulo());
        assertEquals(conteudo, resumo.getConteudo());
        assertEquals(disciplina, resumo.getDisciplina());
    }


    /**
     * Teste de falha ao criar resumo com ID nulo.
     * Espera-se uma exceção indicando que o ID é obrigatório.
     */
    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        UUID usuarioId = UUID.randomUUID();
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(null, usuarioId, "Título", "Conteúdo", disciplina);
        });

        assertEquals("ID não pode ser nulo", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com usuarioId nulo.
     * Garante que não é possível criar um resumo sem vincular a um usuário.
     */
    @Test
    void deveLancarExcecaoQuandoUsuarioIdForNulo() {
        UUID id = UUID.randomUUID();
        String titulo = "Resumo de Matemática: Álgebra Linear";
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, null, titulo, conteudo, disciplina);
        });

        assertEquals("Usuario responsavel não pode ser nulo", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com título nulo.
     * Valida que o campo título é obrigatório.
     */
    @Test
    void deveLancarExcecaoQuandoTituloForNulo() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este resumo cobre conceitos essenciais de vetores e matrizes.";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, null, conteudo, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com título vazio.
     * Verifica a rejeição de string vazia no campo título.
     */
    @Test
    void deveLancarExcecaoQuandoTituloForVazio() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este é um conteúdo válido";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "História", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, "", conteudo, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com título contendo apenas espaços.
     * Garante que espaços em branco também sejam considerados inválidos.
     */
    @Test
    void deveLancarExcecaoQuandoTituloForApenasEspaco() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String conteudo = "Este é um conteúdo válido";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "História", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, "   ", conteudo, disciplina);
        });

        assertEquals("Título é obrigatório", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com conteúdo nulo.
     * Garante que o conteúdo do resumo é obrigatório.
     */
    @Test
    void deveLancarExcecaoQuandoConteudoForNulo() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, titulo, null, disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com conteúdo vazio.
     * Valida que um conteúdo em branco não é aceito.
     */
    @Test
    void deveLancarExcecaoQuandoConteudoForVazio() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, titulo, "", disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }


    /**
     * Teste de falha ao criar resumo com conteúdo contendo apenas espaços.
     * Verifica se o sistema impede criação de resumos "vazios disfarçados".
     */
    @Test
    void deveLancarExcecaoQuandoConteudoForApenasEspaco() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        Disciplina disciplina = new Disciplina(UUID.randomUUID(), "Física", "Desc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, titulo, "    ", disciplina);
        });

        assertEquals("Conteúdo é obrigatório", exception.getMessage());
    }

    /**
     * Teste de falha ao criar resumo com disciplina nula.
     * Garante que cada resumo esteja vinculado a uma disciplina válida.
     */
    @Test
    void deveLancarExcecaoQuandoDisciplinaForNula() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        String titulo = "Resumo de Física";
        String conteudo = "Este é um resumo válido";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Resumo(id, usuarioId, titulo, conteudo, null);
        });

        assertEquals("Disciplina não pode ser nula", exception.getMessage());
    }

}
