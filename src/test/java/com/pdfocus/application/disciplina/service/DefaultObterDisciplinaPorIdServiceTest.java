package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
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
 * Testes unitários para a classe {@link DefaultObterDisciplinaPorIdService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultObterDisciplinaPorIdService")
public class DefaultObterDisciplinaPorIdServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    @InjectMocks
    private DefaultObterDisciplinaPorIdService service;

    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final UUID ID_USUARIO = UUID.randomUUID();

    /**
     * Agrupa os testes para os cenários principais de busca.
     */
    @Nested
    @DisplayName("Cenários de Busca")
    class CenariosDeBusca {

        @Test
        @DisplayName("Deve retornar a disciplina quando encontrada no repositório")
        void deveRetornarDisciplinaQuandoEncontrada() {
            // Arrange (Preparação)
            // 1. Criamos um objeto de domínio 'Disciplina' para ser o resultado esperado.
            Disciplina disciplinaEsperada = new Disciplina(ID_DISCIPLINA, "Nome Teste", "Desc Teste", ID_USUARIO);

            // 2. Ensinamos o nosso repositório FALSO (mock) a se comportar:
            // "QUANDO o metodo 'findByIdAndUsuarioId' for chamado com estes IDs, ENTÃO retorne a disciplinaEsperada".
            when(disciplinaRepositoryMock.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Optional.of(disciplinaEsperada));

            // Act (Ação)
            // 3. Executamos o método que queremos testar.
            Optional<Disciplina> resultado = service.executar(ID_DISCIPLINA, ID_USUARIO);

            // Assert (Verificação)
            // 4. Verificamos se o resultado é o que esperávamos.
            assertTrue(resultado.isPresent(), "O Optional não deveria estar vazio.");
            assertEquals(disciplinaEsperada.getId(), resultado.get().getId(), "O ID da disciplina não corresponde.");

            // 5. Verificamos se o serviço realmente chamou o repositório da forma correta.
            verify(disciplinaRepositoryMock).findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve retornar um Optional vazio quando a disciplina não for encontrada")
        void deveRetornarVazioQuandoNaoEncontrada() {
            // Arrange
            // Ensinamos o mock a retornar um Optional vazio para simular "não encontrado".
            when(disciplinaRepositoryMock.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Disciplina> resultado = service.executar(ID_DISCIPLINA, ID_USUARIO);

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
            verify(disciplinaRepositoryMock).findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO);
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
            // Verificamos se uma NullPointerException é lançada ao executar o metodo com o primeiro parâmetro nulo.
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, ID_USUARIO);
            });

            // Verificamos que o repositório NUNCA foi chamado, provando que a validação falhou antes.
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
