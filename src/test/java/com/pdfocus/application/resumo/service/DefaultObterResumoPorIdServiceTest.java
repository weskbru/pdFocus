package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
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
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultObterResumoPorIdService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultObterResumoPorIdService")
class DefaultObterResumoPorIdServiceTest {

    @Mock
    private ResumoRepository resumoRepositoryMock;

    @InjectMocks
    private DefaultObterResumoPorIdService service;

    private final UUID ID_RESUMO = UUID.randomUUID();
    private final UUID ID_USUARIO = UUID.randomUUID();

    /**
     * Agrupa os testes para os cenários principais de busca.
     */
    @Nested
    @DisplayName("Cenários de Busca")
    class CenariosDeBusca {

        @Test
        @DisplayName("Deve retornar o resumo quando encontrado no repositório")
        void deveRetornarResumoQuandoEncontrado() {
            // --- Arrange (Preparação) ---
            // 1. Criamos um objeto de domínio 'Resumo' para ser o resultado esperado.
            // Não precisamos de uma Disciplina real, podemos usar um mock para simplificar.
            Resumo resumoEsperado = Resumo.criar(ID_RESUMO, ID_USUARIO, "Título Teste", "Conteúdo Teste", mock(Disciplina.class));

            // 2. Ensinamos o nosso repositório falso (mock) a se comportar:
            // "QUANDO o método 'buscarPorIdEUsuario' for chamado, ENTÃO retorne o resumoEsperado".
            when(resumoRepositoryMock.buscarPorIdEUsuario(ID_RESUMO, ID_USUARIO))
                    .thenReturn(Optional.of(resumoEsperado));

            // --- Act (Ação) ---
            // 3. Executamos o método que queremos testar.
            Optional<Resumo> resultado = service.executar(ID_RESUMO, ID_USUARIO);

            // --- Assert (Verificação) ---
            // 4. Verificamos se o resultado é o que esperávamos.
            assertTrue(resultado.isPresent(), "O Optional não deveria estar vazio.");
            assertEquals(resumoEsperado.getId(), resultado.get().getId(), "O ID do resumo não corresponde.");

            // 5. Verificamos se o serviço realmente chamou o repositório da forma correta.
            verify(resumoRepositoryMock).buscarPorIdEUsuario(ID_RESUMO, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve retornar um Optional vazio quando o resumo não for encontrado")
        void deveRetornarVazioQuandoNaoEncontrado() {
            // Arrange: Ensinamos o mock a retornar um Optional vazio.
            when(resumoRepositoryMock.buscarPorIdEUsuario(ID_RESUMO, ID_USUARIO))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Resumo> resultado = service.executar(ID_RESUMO, ID_USUARIO);

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
            verify(resumoRepositoryMock).buscarPorIdEUsuario(ID_RESUMO, ID_USUARIO);
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
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, ID_USUARIO);
            });
            verifyNoInteractions(resumoRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoIdUsuarioForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(ID_RESUMO, null);
            });
            verifyNoInteractions(resumoRepositoryMock);
        }
    }
}
