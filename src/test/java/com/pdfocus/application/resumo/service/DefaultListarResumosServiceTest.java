package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultListarResumosService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultListarResumosService")
public class DefaultListarResumosServiceTest {

    @Mock
    private ResumoRepository resumoRepositoryMock;

    @InjectMocks
    private DefaultListarResumosService listarResumosService;

    private UUID usuarioIdValido;
    private UUID disciplinaIdValida;

    @BeforeEach
    void setUp() {
        usuarioIdValido = UUID.randomUUID();
        disciplinaIdValida = UUID.randomUUID();
    }

    /**
     * Testes para o método {@link DefaultListarResumosService#buscarTodosPorUsuario(UUID)}.
     */
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Testes Unitários - DefaultListarResumosService")
    class BuscarTodosPorUsuario {

        @Test
        @DisplayName("Deve retornar lista de resumos quando o repositório os encontra")
        void deveRetornarListaQuandoRepositorioEncontra() {
            // Arrange
            // Não precisamos de um Resumo real, uma lista mockada já serve para testar o fluxo.
            List<Resumo> resumosEsperados = Collections.singletonList(mock(Resumo.class));
            when(resumoRepositoryMock.buscarTodosPorUsuario(usuarioIdValido)).thenReturn(resumosEsperados);

            // Act
            List<Resumo> resumosAtuais = listarResumosService.buscarTodosPorUsuario(usuarioIdValido);

            // Assert
            assertNotNull(resumosAtuais);
            assertEquals(resumosEsperados, resumosAtuais);
            verify(resumoRepositoryMock).buscarTodosPorUsuario(usuarioIdValido);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoUsuarioIdForNulo() {
            // Arrange
            String mensagemEsperada = "ID do usuário não pode ser nulo.";

            // Act & Assert
            // Usamos assertThrows para verificar se a exceção correta é lançada.
            // A exceção pode ser NullPointerException (de Objects.requireNonNull) ou IllegalArgumentException se você usar um if.
            Exception exception = assertThrows(NullPointerException.class, () -> {
                listarResumosService.buscarTodosPorUsuario(null);
            });

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(resumoRepositoryMock, never()).buscarTodosPorUsuario(any());
        }
    }

    /**
     * Testes para o método {@link DefaultListarResumosService#buscarPorDisciplinaEUsuario(UUID, UUID)}.
     */
    @Nested
    @DisplayName("Método: buscarPorDisciplinaEUsuario")
    class BuscarPorDisciplinaEUsuario {

        @Test
        @DisplayName("Deve retornar lista de resumos quando os IDs são válidos e o repositório os encontra")
        void deveRetornarListaQuandoIdsValidosERepositorioEncontra() {
            // Arrange
            List<Resumo> resumosEsperados = Collections.singletonList(mock(Resumo.class));
            when(resumoRepositoryMock.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido)).thenReturn(resumosEsperados);

            // Act
            List<Resumo> resumosAtuais = listarResumosService.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);

            // Assert
            assertNotNull(resumosAtuais);
            assertEquals(resumosEsperados, resumosAtuais);
            verify(resumoRepositoryMock).buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID da disciplina for nulo")
        void deveLancarExcecaoQuandoDisciplinaIdForNulo() {
            // Arrange
            String mensagemEsperada = "ID da disciplina não pode ser nulo.";

            // Act & Assert
            Exception exception = assertThrows(NullPointerException.class, () -> {
                listarResumosService.buscarPorDisciplinaEUsuario(null, usuarioIdValido);
            });

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(resumoRepositoryMock, never()).buscarPorDisciplinaEUsuario(any(), any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoUsuarioIdForNulo() {
            // Arrange
            String mensagemEsperada = "ID do usuário não pode ser nulo.";

            // Act & Assert
            Exception exception = assertThrows(NullPointerException.class, () -> {
                listarResumosService.buscarPorDisciplinaEUsuario(disciplinaIdValida, null);
            });

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(resumoRepositoryMock, never()).buscarPorDisciplinaEUsuario(any(), any());
        }
    }

}
