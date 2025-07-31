package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
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
 * Testes unitários para a classe {@link DefaultAtualizarDisciplinaService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultAtualizarDisciplinaService")
class DefaultAtualizarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    @InjectMocks
    private DefaultAtualizarDisciplinaService service;

    private UUID idDisciplina;
    private UUID idUsuario;
    private Disciplina disciplinaExistente;
    private AtualizarDisciplinaCommand comandoAtualizacao;

    /**
     * Prepara os dados de teste comuns antes de cada teste.
     */
    @BeforeEach
    void setUp() {
        idDisciplina = UUID.randomUUID();
        idUsuario = UUID.randomUUID();

        // Simula a disciplina que já existe no banco de dados
        disciplinaExistente = new Disciplina(idDisciplina, "Nome Antigo", "Descrição Antiga", idUsuario);

        // Simula os novos dados que o usuário enviou para a atualização
        comandoAtualizacao = new AtualizarDisciplinaCommand("Nome Novo", "Descrição Nova");
    }

    /**
     * Agrupa os testes para os cenários principais de atualização.
     */
    @Nested
    @DisplayName("Cenários de Atualização")
    class CenariosDeAtualizacao {

        @Test
        @DisplayName("Deve atualizar e retornar a disciplina quando ela for encontrada")
        void deveAtualizarERetornarDisciplinaQuandoEncontrada() {
            // --- Arrange (Preparação) ---
            // 1. Criamos a versão atualizada da disciplina, como esperamos que ela fique após a operação.
            Disciplina disciplinaAtualizadaEsperada = new Disciplina(idDisciplina, "Nome Novo", "Descrição Nova", idUsuario);

            // 2. Ensinamos o mock do repositório a se comportar em duas etapas:
            //    - Primeiro, quando 'findByIdAndUsuarioId' for chamado, ele deve encontrar a disciplina original.
            when(disciplinaRepositoryMock.findByIdAndUsuarioId(idDisciplina, idUsuario))
                    .thenReturn(Optional.of(disciplinaExistente));
            //    - Segundo, quando 'salvar' for chamado com qualquer objeto Disciplina, ele deve retornar
            //      a nossa versão atualizada esperada.
            when(disciplinaRepositoryMock.salvar(any(Disciplina.class)))
                    .thenReturn(disciplinaAtualizadaEsperada);

            // --- Act (Ação) ---
            // 3. Executamos o método de atualização do serviço.
            Optional<Disciplina> resultado = service.executar(idDisciplina, comandoAtualizacao, idUsuario);

            // --- Assert (Verificação) ---
            // 4. Verificamos se o resultado é o que esperávamos.
            assertTrue(resultado.isPresent(), "O Optional não deveria estar vazio.");
            assertEquals("Nome Novo", resultado.get().getNome(), "O nome da disciplina não foi atualizado.");
            assertEquals("Descrição Nova", resultado.get().getDescricao(), "A descrição não foi atualizada.");

            // 5. Verificamos se o serviço interagiu corretamente com o repositório.
            verify(disciplinaRepositoryMock).findByIdAndUsuarioId(idDisciplina, idUsuario); // Verificamos a busca
            verify(disciplinaRepositoryMock).salvar(any(Disciplina.class)); // Verificamos o salvamento
        }

        @Test
        @DisplayName("Deve retornar um Optional vazio quando a disciplina não for encontrada")
        void deveRetornarVazioQuandoNaoEncontrada() {
            // Arrange: Ensinamos o mock a retornar um Optional vazio para simular "não encontrado".
            when(disciplinaRepositoryMock.findByIdAndUsuarioId(idDisciplina, idUsuario))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Disciplina> resultado = service.executar(idDisciplina, comandoAtualizacao, idUsuario);

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
            // Garante que, se a disciplina não foi encontrada, o método 'salvar' nunca foi chamado.
            verify(disciplinaRepositoryMock, never()).salvar(any(Disciplina.class));
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
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, comandoAtualizacao, idUsuario);
            });
            verifyNoInteractions(disciplinaRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o comando for nulo")
        void deveLancarExcecaoQuandoComandoForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(idDisciplina, null, idUsuario);
            });
            verifyNoInteractions(disciplinaRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoIdUsuarioForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(idDisciplina, comandoAtualizacao, null);
            });
            verifyNoInteractions(disciplinaRepositoryMock);
        }
    }
}
