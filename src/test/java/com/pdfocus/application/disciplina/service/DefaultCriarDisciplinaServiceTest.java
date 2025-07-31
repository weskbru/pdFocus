package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.models.Disciplina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultCriarDisciplinaService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultCriarDisciplinaService")
public class DefaultCriarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DefaultCriarDisciplinaService service;

    private final UUID ID_USUARIO = UUID.randomUUID();
    private final String NOME_DISCIPLINA_VALIDO = "Matemática Avançada";
    private final String DESCRICAO_DISCIPLINA_VALIDA = "Estudo de tópicos avançados em matemática.";

    /**
     * Testa o cenário de sucesso da criação de uma disciplina.
     */
    @Test
    @DisplayName("Deve criar uma disciplina com sucesso quando os dados são válidos")
    void deveCriarDisciplinaComSucesso() {
        // Arrange
        CriarDisciplinaCommand command = new CriarDisciplinaCommand(NOME_DISCIPLINA_VALIDO, DESCRICAO_DISCIPLINA_VALIDA);
        when(disciplinaRepository.salvar(any(Disciplina.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        // A chamada agora inclui o ID do usuário
        Disciplina disciplinaCriada = service.executar(command, ID_USUARIO);

        // Assert
        assertNotNull(disciplinaCriada);
        assertNotNull(disciplinaCriada.getId());
        assertEquals(NOME_DISCIPLINA_VALIDO, disciplinaCriada.getNome());
        assertEquals(DESCRICAO_DISCIPLINA_VALIDA, disciplinaCriada.getDescricao());
        // Nova asserção para garantir que o ID do usuário foi salvo corretamente
        assertEquals(ID_USUARIO, disciplinaCriada.getUsuarioId());

        verify(disciplinaRepository).salvar(any(Disciplina.class));
    }

    /**
     * Agrupa os testes para cenários de validação e erro.
     */
    @Nested
    @DisplayName("Cenários de Validação e Erro")
    class ValidacaoErro {

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o nome for vazio")
        void deveLancarExcecaoSeNomeForVazio() {
            // Arrange
            CriarDisciplinaCommand command = new CriarDisciplinaCommand("  ", "Descrição qualquer");

            // Act & Assert
            // A exceção é lançada pelo construtor do modelo de domínio 'Disciplina'
            assertThrows(CampoVazioException.class, () -> {
                service.executar(command, ID_USUARIO);
            });
            verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
        }

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o nome for nulo")
        void deveLancarExcecaoSeNomeForNulo() {
            // Arrange
            CriarDisciplinaCommand command = new CriarDisciplinaCommand(null, "Descrição qualquer");

            // Act & Assert
            assertThrows(CampoNuloException.class, () -> {
                service.executar(command, ID_USUARIO);
            });
            verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoSeUsuarioIdForNulo() {
            // Arrange
            CriarDisciplinaCommand command = new CriarDisciplinaCommand(NOME_DISCIPLINA_VALIDO, DESCRICAO_DISCIPLINA_VALIDA);

            // Act & Assert
            // A exceção é lançada pela validação de entrada do próprio serviço
            assertThrows(NullPointerException.class, () -> {
                service.executar(command, null);
            });
            verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
        }
    }
}
