package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe de domínio {@link Disciplina}.
 * Garante que as regras de negócio e validações da entidade estão corretas.
 */
@DisplayName("Testes Unitários - Modelo de Domínio Disciplina")
class DisciplinaTest {

    private final UUID id = UUID.randomUUID();
    private final UUID usuarioId = UUID.randomUUID(); // Adicionado para o novo construtor
    private final String nome = "Direito Penal";
    private final String descricao = "Parte Geral do Código Penal";

    /**
     * Testa o cenário de criação bem-sucedida de uma instância de Disciplina.
     */
    @Test
    @DisplayName("Deve criar uma instância de Disciplina com sucesso quando os dados são válidos")
    void deveCriarInstanciaComSucesso() {
        // Act
        // A chamada ao construtor agora inclui o usuarioId
        Disciplina disciplina = new Disciplina(id, nome, descricao, usuarioId);

        // Assert
        assertNotNull(disciplina);
        assertEquals(id, disciplina.getId());
        assertEquals(nome, disciplina.getNome());
        assertEquals(descricao, disciplina.getDescricao());
        assertEquals(usuarioId, disciplina.getUsuarioId()); // Nova verificação
    }

    /**
     * Agrupa os testes para as validações do construtor.
     */
    @Nested
    @DisplayName("Validações do Construtor")
    class ValidacoesDoConstrutor {

        @Test
        @DisplayName("Deve lançar CampoNuloException se o ID for nulo")
        void deveLancarExcecaoSeIdForNulo() {
            // Act & Assert
            assertThrows(CampoNuloException.class, () -> {
                new Disciplina(null, nome, descricao, usuarioId);
            });
        }

        @Test
        @DisplayName("Deve lançar CampoNuloException se o nome for nulo")
        void deveLancarExcecaoSeNomeForNulo() {
            // Act & Assert
            assertThrows(CampoNuloException.class, () -> {
                new Disciplina(id, null, descricao, usuarioId);
            });
        }

        @Test
        @DisplayName("Deve lançar CampoVazioException se o nome estiver em branco")
        void deveLancarExcecaoSeNomeEstiverEmBranco() {
            // Act & Assert
            assertThrows(CampoVazioException.class, () -> {
                new Disciplina(id, "   ", descricao, usuarioId);
            });
        }

        @Test
        @DisplayName("Deve lançar CampoNuloException se o usuarioId for nulo")
        void deveLancarExcecaoSeUsuarioIdForNulo() {
            // Act & Assert
            assertThrows(CampoNuloException.class, () -> {
                new Disciplina(id, nome, descricao, null);
            });
        }

        @Test
        @DisplayName("Não deve lançar exceção se a descrição for nula")
        void naoDeveLancarExcecaoSeDescricaoForNula() {
            // Act & Assert
            assertDoesNotThrow(() -> {
                new Disciplina(id, nome, null, usuarioId);
            });
        }
    }
}
