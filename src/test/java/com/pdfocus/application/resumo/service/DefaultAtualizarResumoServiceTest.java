package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.AtualizarResumoCommand;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultAtualizarResumoService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultAtualizarResumoService")
class DefaultAtualizarResumoServiceTest {

    @Mock
    private ResumoRepository resumoRepositoryMock;

    @InjectMocks
    private DefaultAtualizarResumoService service;

    private UUID idResumo;
    private UUID idUsuario;
    private Resumo resumoExistente;
    private AtualizarResumoCommand comandoAtualizacao;

    /**
     * Prepara os dados de teste comuns antes de cada teste.
     */
    @BeforeEach
    void setUp() {
        idResumo = UUID.randomUUID();
        idUsuario = UUID.randomUUID();

        // Simula o resumo que já existe no banco de dados
        resumoExistente = Resumo.criar(idResumo, idUsuario, "Título Antigo", "Conteúdo Antigo", mock(Disciplina.class));

        // Simula os novos dados que o usuário enviou para a atualização
        comandoAtualizacao = new AtualizarResumoCommand("Título Novo", "Conteúdo Novo");
    }

    /**
     * Agrupa os testes para os cenários principais de atualização.
     */
    @Nested
    @DisplayName("Cenários de Atualização")
    class CenariosDeAtualizacao {

        @Test
        @DisplayName("Deve atualizar e retornar o resumo quando ele for encontrado")
        void deveAtualizarERetornarResumoQuandoEncontrado() {
            // --- Arrange (Preparação) ---
            // 1. Criamos a versão atualizada do resumo, como esperamos que ela fique.
            Resumo resumoAtualizadoEsperado = Resumo.criar(idResumo, idUsuario, "Título Novo", "Conteúdo Novo", resumoExistente.getDisciplina());

            // 2. Ensinamos o mock do repositório a se comportar em duas etapas:
            //    - Primeiro, ao buscar, ele deve encontrar o resumo original.
            when(resumoRepositoryMock.buscarPorIdEUsuario(idResumo, idUsuario))
                    .thenReturn(Optional.of(resumoExistente));
            //    - Segundo, ao salvar, ele deve retornar a nossa versão atualizada.
            when(resumoRepositoryMock.salvar(any(Resumo.class)))
                    .thenReturn(resumoAtualizadoEsperado);

            // --- Act (Ação) ---
            // 3. Executamos o método de atualização do serviço.
            Optional<Resumo> resultado = service.executar(idResumo, idUsuario, comandoAtualizacao);

            // --- Assert (Verificação) ---
            // 4. Verificamos se o resultado é o que esperávamos.
            assertTrue(resultado.isPresent(), "O Optional não deveria estar vazio.");
            assertEquals("Título Novo", resultado.get().getTitulo(), "O título do resumo não foi atualizado.");
            assertEquals("Conteúdo Novo", resultado.get().getConteudo(), "O conteúdo não foi atualizado.");

            // 5. Verificamos se o serviço interagiu corretamente com o repositório.
            verify(resumoRepositoryMock).buscarPorIdEUsuario(idResumo, idUsuario); // Verificamos a busca
            verify(resumoRepositoryMock).salvar(any(Resumo.class)); // Verificamos o salvamento
        }

        @Test
        @DisplayName("Deve retornar um Optional vazio quando o resumo não for encontrado")
        void deveRetornarVazioQuandoNaoEncontrado() {
            // Arrange: Ensinamos o mock a retornar um Optional vazio para simular "não encontrado".
            when(resumoRepositoryMock.buscarPorIdEUsuario(idResumo, idUsuario))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Resumo> resultado = service.executar(idResumo, idUsuario, comandoAtualizacao);

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
            // Garante que, se o resumo não foi encontrado, o método 'salvar' nunca foi chamado.
            verify(resumoRepositoryMock, never()).salvar(any(Resumo.class));
        }
    }

    /**
     * Agrupa os testes para as validações de entrada do serviço.
     */
    @Nested
    @DisplayName("Validação de Entradas")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Deve lançar exceção quando o ID do resumo for nulo")
        void deveLancarExcecaoQuandoIdResumoForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, idUsuario, comandoAtualizacao);
            });
            verifyNoInteractions(resumoRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoIdUsuarioForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(idResumo, null, comandoAtualizacao);
            });
            verifyNoInteractions(resumoRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o comando for nulo")
        void deveLancarExcecaoQuandoComandoForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(idResumo, idUsuario, null);
            });
            verifyNoInteractions(resumoRepositoryMock);
        }
    }
}
