package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.models.Disciplina; // Importar Disciplina
import com.pdfocus.core.models.Resumo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link ListarResumosService}.
 * Garante que o serviço de listagem de resumos se comporta como esperado
 * em diferentes cenários, utilizando um mock para o {@link ResumoRepository}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários para ListarResumosService")
class ListarResumosServiceTest {

    @Mock
    private ResumoRepository resumoRepositoryMock;

    @InjectMocks
    private ListarResumosService listarResumosService;

    private UUID usuarioIdValido;
    private UUID disciplinaIdValida;
    private Disciplina disciplinaExemplo;
    private Resumo resumo1;
    private Resumo resumo2;

    /**
     * Configuração inicial executada antes de cada método de teste.
     * Inicializa instâncias de {@link Disciplina}, {@link Resumo} e UUIDs
     * que são usadas como dados de exemplo nos testes.
     * <p>
     * Adapte a criação de {@code disciplinaExemplo} se a sua classe {@code Disciplina}
     * utilizar um método de fábrica estático em vez de um construtor público.
     * </p>
     */
    @BeforeEach
    void setUp() {
        usuarioIdValido = UUID.randomUUID();
        disciplinaIdValida = UUID.randomUUID();

        // Adapte esta linha se sua classe Disciplina usar um método de fábrica estático.
        disciplinaExemplo = new Disciplina(disciplinaIdValida, "Disciplina de Teste", "Descrição da disciplina");

        resumo1 = Resumo.criar(
                UUID.randomUUID(),
                usuarioIdValido,
                "Resumo Tópico 1",
                "Conteúdo do tópico 1...",
                disciplinaExemplo
        );
        resumo2 = Resumo.criar(
                UUID.randomUUID(),
                usuarioIdValido,
                "Resumo Tópico 2",
                "Conteúdo do tópico 2...",
                disciplinaExemplo
        );
    }

    /**
     * Agrupa os testes relacionados ao método {@link ListarResumosService#buscarTodosPorUsuario(UUID)}.
     */
    @Nested
    @DisplayName("Testes para o método buscarTodosPorUsuario")
    class BuscarTodosPorUsuarioTestes {

        /**
         * Testa se o método {@code buscarTodosPorUsuario} retorna uma lista correta de resumos
         * quando um ID de usuário válido é fornecido e o repositório encontra os dados correspondentes.
         */
        @Test
        @DisplayName("Deve retornar lista de resumos quando usuário ID é válido e repositório encontra dados")
        void deveRetornarListaDeResumos_quandoUsuarioIdValidoERepositorioEncontraDados() {
            // Arrange
            List<Resumo> resumosEsperados = Arrays.asList(resumo1, resumo2);
            when(resumoRepositoryMock.buscarTodosPorUsuario(usuarioIdValido)).thenReturn(resumosEsperados);

            // Act
            List<Resumo> resumosRetornados = listarResumosService.buscarTodosPorUsuario(usuarioIdValido);

            // Assert
            assertNotNull(resumosRetornados, "A lista retornada não deve ser nula.");
            assertEquals(2, resumosRetornados.size(), "A lista deve conter 2 resumos.");
            assertEquals(resumosEsperados, resumosRetornados, "A lista retornada deve ser igual à esperada.");
            verify(resumoRepositoryMock).buscarTodosPorUsuario(usuarioIdValido);
        }

        /**
         * Testa se o método {@code buscarTodosPorUsuario} retorna uma lista vazia
         * quando um ID de usuário válido é fornecido, mas o repositório não encontra resumos para esse usuário.
         */
        @Test
        @DisplayName("Deve retornar lista vazia quando usuário ID é válido e repositório não encontra dados")
        void deveRetornarListaVazia_quandoUsuarioIdValidoERepositorioNaoEncontraDados() {
            // Arrange
            when(resumoRepositoryMock.buscarTodosPorUsuario(usuarioIdValido)).thenReturn(Collections.emptyList());

            // Act
            List<Resumo> resumosRetornados = listarResumosService.buscarTodosPorUsuario(usuarioIdValido);

            // Assert
            assertNotNull(resumosRetornados, "A lista retornada não deve ser nula, mesmo que vazia.");
            assertTrue(resumosRetornados.isEmpty(), "A lista retornada deve estar vazia.");
            verify(resumoRepositoryMock).buscarTodosPorUsuario(usuarioIdValido);
        }

        /**
         * Testa se o método {@code buscarTodosPorUsuario} lança {@link CampoNuloException}
         * quando o ID do usuário fornecido é nulo.
         * Também verifica se o repositório não é chamado neste cenário.
         */
        @Test
        @DisplayName("Deve lançar CampoNuloException quando usuário ID for nulo")
        void deveLancarCampoNuloException_quandoUsuarioIdForNulo() {
            // Arrange
            UUID idNulo = null;
            String mensagemEsperada = "O ID do usuário não pode ser nulo para listar os resumos.";

            // Act & Assert
            CampoNuloException exception = assertThrows(CampoNuloException.class, () -> {
                listarResumosService.buscarTodosPorUsuario(idNulo);
            });

            assertEquals(mensagemEsperada, exception.getMessage(), "A mensagem da exceção não é a esperada.");
            verify(resumoRepositoryMock, never()).buscarTodosPorUsuario(any());
        }
    }

    /**
     * Agrupa os testes relacionados ao método {@link ListarResumosService#buscarPorDisciplinaEUsuario(UUID, UUID)}.
     */
    @Nested
    @DisplayName("Testes para o método buscarPorDisciplinaEUsuario")
    class BuscarPorDisciplinaEUsuarioTestes {

        /**
         * Testa se o método {@code buscarPorDisciplinaEUsuario} retorna uma lista correta de resumos
         * quando IDs de disciplina e usuário válidos são fornecidos e o repositório encontra os dados.
         */
        @Test
        @DisplayName("Deve retornar lista de resumos quando IDs são válidos e repositório encontra dados")
        void deveRetornarListaDeResumos_quandoIdsValidosERepositorioEncontraDados() {
            // Arrange
            List<Resumo> resumosEsperados = Arrays.asList(resumo1); // Supondo que resumo1 é da disciplinaExemplo
            when(resumoRepositoryMock.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido))
                    .thenReturn(resumosEsperados);

            // Act
            List<Resumo> resumosRetornados = listarResumosService.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);

            // Assert
            assertNotNull(resumosRetornados);
            assertEquals(1, resumosRetornados.size());
            assertEquals(resumosEsperados, resumosRetornados);
            verify(resumoRepositoryMock).buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);
        }

        /**
         * Testa se o método {@code buscarPorDisciplinaEUsuario} retorna uma lista vazia
         * quando IDs de disciplina e usuário válidos são fornecidos, mas o repositório não encontra resumos.
         */
        @Test
        @DisplayName("Deve retornar lista vazia quando IDs são válidos e repositório não encontra dados")
        void deveRetornarListaVazia_quandoIdsValidosERepositorioNaoEncontraDados() {
            // Arrange
            when(resumoRepositoryMock.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido))
                    .thenReturn(Collections.emptyList());

            // Act
            List<Resumo> resumosRetornados = listarResumosService.buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);

            // Assert
            assertNotNull(resumosRetornados);
            assertTrue(resumosRetornados.isEmpty());
            verify(resumoRepositoryMock).buscarPorDisciplinaEUsuario(disciplinaIdValida, usuarioIdValido);
        }

        /**
         * Testa se o método {@code buscarPorDisciplinaEUsuario} lança {@link CampoNuloException}
         * quando o ID da disciplina fornecido é nulo.
         * Também verifica se o repositório não é chamado.
         */
        @Test
        @DisplayName("Deve lançar CampoNuloException quando disciplina ID for nulo")
        void deveLancarCampoNuloException_quandoDisciplinaIdForNulo() {
            // Arrange
            UUID idNulo = null;
            String mensagemEsperada = "O ID da disciplina não pode ser nulo para listar os resumos.";

            // Act & Assert
            CampoNuloException exception = assertThrows(CampoNuloException.class, () -> {
                listarResumosService.buscarPorDisciplinaEUsuario(idNulo, usuarioIdValido);
            });

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(resumoRepositoryMock, never()).buscarPorDisciplinaEUsuario(any(), any());
        }

        /**
         * Testa se o método {@code buscarPorDisciplinaEUsuario} lança {@link CampoNuloException}
         * quando o ID do usuário fornecido é nulo.
         * Também verifica se o repositório não é chamado.
         */
        @Test
        @DisplayName("Deve lançar CampoNuloException quando usuário ID for nulo")
        void deveLancarCampoNuloException_quandoUsuarioIdForNulo() {
            // Arrange
            UUID idNulo = null;
            String mensagemEsperada = "O ID do usuário não pode ser nulo para listar os resumos.";

            // Act & Assert
            CampoNuloException exception = assertThrows(CampoNuloException.class, () -> {
                listarResumosService.buscarPorDisciplinaEUsuario(disciplinaIdValida, idNulo);
            });

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(resumoRepositoryMock, never()).buscarPorDisciplinaEUsuario(any(), any());
        }
    }
}