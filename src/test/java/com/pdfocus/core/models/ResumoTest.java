package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe de domínio {@link Resumo}.
 * Garante que as regras de negócio e validações do método de fábrica 'criar' estão corretas.
 */
@DisplayName("Testes Unitários - Modelo de Domínio Resumo")
public class ResumoTest {

    // Dados de teste comuns que serão inicializados antes de cada teste
    private UUID id;
    private UUID usuarioId;
    private String titulo;
    private String conteudo;
    private Disciplina disciplina;

    /**
     * Prepara os dados de teste válidos antes da execução de cada teste.
     * Isso evita a repetição de código (Princípio DRY).
     */
    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        usuarioId = UUID.randomUUID();
        titulo = "Resumo Válido";
        conteudo = "Conteúdo válido.";
        // A criação da Disciplina agora usa o construtor correto de 4 argumentos
        disciplina = new Disciplina(UUID.randomUUID(), "Matemática", "Descrição", usuarioId);
    }

    /**
     * Testa o cenário de sucesso da criação de um resumo.
     */
    @Test
    @DisplayName("Deve criar um resumo com sucesso quando todos os dados são válidos")
    void deveCriarResumoComDadosValidos() {
        // Act
        Resumo resumo = Resumo.criar(id, usuarioId, titulo, conteudo, disciplina);

        // Assert
        assertNotNull(resumo);
        assertEquals(id, resumo.getId());
        assertEquals(usuarioId, resumo.getUsuarioId());
        assertEquals(titulo, resumo.getTitulo());
        assertEquals(conteudo, resumo.getConteudo());
        assertEquals(disciplina, resumo.getDisciplina());
    }

    /**
     * Agrupa todos os testes que validam as regras de negócio para campos inválidos.
     */
    @Nested
    @DisplayName("Validações de Campos Inválidos")
    class ValidacoesDeCampos {

        @Test
        @DisplayName("Deve lançar exceção quando o ID for nulo")
        void deveLancarExcecaoQuandoIdForNulo() {
            assertThrows(CampoNuloException.class, () -> {
                Resumo.criar(null, usuarioId, titulo, conteudo, disciplina);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção quando o usuarioId for nulo")
        void deveLancarExcecaoQuandoUsuarioIdForNulo() {
            assertThrows(CampoNuloException.class, () -> {
                Resumo.criar(id, null, titulo, conteudo, disciplina);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção quando o título for nulo")
        void deveLancarExcecaoQuandoTituloForNulo() {
            assertThrows(CampoNuloException.class, () -> {
                Resumo.criar(id, usuarioId, null, conteudo, disciplina);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção quando o título for vazio ou apenas espaços")
        void deveLancarExcecaoQuandoTituloForVazio() {
            assertThrows(CampoVazioException.class, () -> Resumo.criar(id, usuarioId, "", conteudo, disciplina));
            assertThrows(CampoVazioException.class, () -> Resumo.criar(id, usuarioId, "   ", conteudo, disciplina));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o conteúdo for nulo")
        void deveLancarExcecaoQuandoConteudoForNulo() {
            assertThrows(CampoNuloException.class, () -> {
                Resumo.criar(id, usuarioId, titulo, null, disciplina);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção quando o conteúdo for vazio ou apenas espaços")
        void deveLancarExcecaoQuandoConteudoForVazio() {
            assertThrows(CampoVazioException.class, () -> Resumo.criar(id, usuarioId, titulo, "", disciplina));
            assertThrows(CampoVazioException.class, () -> Resumo.criar(id, usuarioId, titulo, "   ", disciplina));
        }

        @Test
        @DisplayName("Deve lançar exceção quando a disciplina for nula")
        void deveLancarExcecaoQuandoDisciplinaForNula() {
            assertThrows(CampoNuloException.class, () -> {
                Resumo.criar(id, usuarioId, titulo, conteudo, null);
            });
        }
    }
}
