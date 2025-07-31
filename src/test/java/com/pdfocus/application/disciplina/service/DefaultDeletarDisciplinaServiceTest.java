package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
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
 * Testes unitários para a classe {@link DefaultDeletarDisciplinaService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarDisciplinaService")
class DefaultDeletarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    @InjectMocks
    private DefaultDeletarDisciplinaService service;

    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final UUID ID_USUARIO = UUID.randomUUID();

    /**
     * Agrupa os testes para os cenários principais de deleção.
     */
    @Nested
    @DisplayName("Cenários de Deleção")
    class CenariosDeDelecao {

        @Test
        @DisplayName("Deve chamar o repositório para deletar quando a disciplina existe")
        void deveChamarRepositorioParaDeletar() {
            // --- Arrange (Preparação) ---
            // Ensinamos o mock do repositório a não fazer nada (comportamento padrão de um método void)
            // quando 'deletarPorIdEUsuario' for chamado.
            // A linha abaixo é opcional, pois mocks já não fazem nada por padrão,
            // mas pode deixar a intenção mais clara.
            doNothing().when(disciplinaRepositoryMock).deletarPorIdEUsuario(ID_DISCIPLINA, ID_USUARIO);

            // --- Act (Ação) & Assert (Verificação) ---
            // Executamos o método e verificamos que nenhuma exceção foi lançada.
            assertDoesNotThrow(() -> {
                service.executar(ID_DISCIPLINA, ID_USUARIO);
            });

            // Verificamos se o serviço realmente chamou o método correto do repositório.
            verify(disciplinaRepositoryMock).deletarPorIdEUsuario(ID_DISCIPLINA, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve propagar a exceção quando o repositório não encontra a disciplina")
        void devePropagarExcecaoQuandoNaoEncontrada() {
            // Arrange: Ensinamos o mock a lançar a exceção que esperamos que o adapter lance.
            doThrow(new DisciplinaNaoEncontradaException(ID_DISCIPLINA))
                    .when(disciplinaRepositoryMock).deletarPorIdEUsuario(ID_DISCIPLINA, ID_USUARIO);

            // Act & Assert: Verificamos se o nosso serviço corretamente "deixa passar" a exceção.
            assertThrows(DisciplinaNaoEncontradaException.class, () -> {
                service.executar(ID_DISCIPLINA, ID_USUARIO);
            });

            // Verificamos que, mesmo com a exceção, a tentativa de chamar o repositório foi feita.
            verify(disciplinaRepositoryMock).deletarPorIdEUsuario(ID_DISCIPLINA, ID_USUARIO);
        }
    }

    /**
     * Agrupa os testes para as validações de entrada do serviço.
     */
    @Nested
    @DisplayName("Validação de Entradas")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Deve lançar exceção quando o ID da disciplina for nulo")
        void deveLancarExcecaoQuandoIdDisciplinaForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, ID_USUARIO);
            });

            // Garante que o repositório nunca foi chamado se a validação falhou.
            verifyNoInteractions(disciplinaRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoIdUsuarioForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(ID_DISCIPLINA, null);
            });

            verifyNoInteractions(disciplinaRepositoryMock);
        }
    }
}
