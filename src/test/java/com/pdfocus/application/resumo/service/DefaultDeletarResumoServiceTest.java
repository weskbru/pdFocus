package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.ResumoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultDeletarResumoService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarResumoService")
class DefaultDeletarResumoServiceTest {

    @Mock
    private ResumoRepository resumoRepositoryMock;

    @InjectMocks
    private DefaultDeletarResumoService service;

    private final UUID ID_RESUMO = UUID.randomUUID();
    private final UUID ID_USUARIO = UUID.randomUUID();

    /**
     * Agrupa os testes para os cenários principais de deleção.
     */
    @Nested
    @DisplayName("Cenários de Deleção")
    class CenariosDeDelecao {

        @Test
        @DisplayName("Deve chamar o repositório para deletar quando o resumo existe")
        void deveChamarRepositorioParaDeletar() {
            // --- Arrange (Preparação) ---
            // Ensinamos o mock a não fazer nada (comportamento padrão de um método void)
            // quando 'deletarPorIdEUsuario' for chamado.
            doNothing().when(resumoRepositoryMock).deletarPorIdEUsuario(ID_RESUMO, ID_USUARIO);

            // --- Act (Ação) & Assert (Verificação) ---
            // Executamos o método e verificamos que nenhuma exceção foi lançada.
            assertDoesNotThrow(() -> {
                service.executar(ID_RESUMO, ID_USUARIO);
            });

            // Verificamos se o serviço realmente chamou o método correto do repositório.
            verify(resumoRepositoryMock).deletarPorIdEUsuario(ID_RESUMO, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve propagar a exceção quando o repositório não encontra o resumo")
        void devePropagarExcecaoQuandoNaoEncontrado() {
            // Arrange: Ensinamos o mock a lançar a exceção que esperamos que o adapter lance.
            doThrow(new ResumoNaoEncontradoException(ID_RESUMO))
                    .when(resumoRepositoryMock).deletarPorIdEUsuario(ID_RESUMO, ID_USUARIO);

            // Act & Assert: Verificamos se o nosso serviço corretamente "deixa passar" a exceção.
            assertThrows(ResumoNaoEncontradoException.class, () -> {
                service.executar(ID_RESUMO, ID_USUARIO);
            });

            // Verificamos que, mesmo com a exceção, a tentativa de chamar o repositório foi feita.
            verify(resumoRepositoryMock).deletarPorIdEUsuario(ID_RESUMO, ID_USUARIO);
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

            // Garante que o repositório nunca foi chamado se a validação falhou.
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
