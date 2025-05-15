package com.pdfocus.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class DisciplinaTest {

    @Test
    void criaDisciplinaComDadosValidos() {
        UUID id = UUID.randomUUID();
        Disciplina d = new Disciplina(id, "Matemática", "Descrição opcional");

        assertEquals(id, d.getId());
        assertEquals("Matemática", d.getNome());
        assertEquals("Descrição opcional", d.getDescricao());
    }

    @Test
    void naoCriaComIdNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina(null, "Matemática", "Descrição");
        });

        assertEquals("ID não pode ser nulo", exception.getMessage());
    }

    @Test
    void naoCriaComNomeNuloOuVazio() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina(UUID.randomUUID(), null, "Descrição");
        });
        assertEquals("Nome obrigatório", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina(UUID.randomUUID(), "   ", "Descrição");
        });
        assertEquals("Nome obrigatório", exception2.getMessage());
    }
}
