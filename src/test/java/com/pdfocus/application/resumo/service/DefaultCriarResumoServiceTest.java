package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
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
 * Testes unitários para {@link DefaultCriarResumoService}.
 * Valida a lógica de negócio para a criação de novos resumos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultCriarResumoService")
public class DefaultCriarResumoServiceTest {

    /** Mock para simular o repositório de resumos. */
    @Mock
    private ResumoRepository resumoRepository;

    /** Mock para simular o repositório de disciplinas, uma dependência do serviço. */
    @Mock
    private DisciplinaRepository disciplinaRepository;

    /** Instância de {@link DefaultCriarResumoService} a ser testada, com os mocks injetados. */
    @InjectMocks
    private DefaultCriarResumoService service;

    // Dados de teste reutilizáveis
    private final UUID ID_USUARIO = UUID.randomUUID();
    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final String TITULO = "Resumo de Direito Constitucional";
    private final String CONTEUDO = "Artigo 5º - Direitos e Deveres Individuais e Coletivos.";
    private Disciplina disciplinaValida;

    /**
     * Configura objetos comuns usados em múltiplos testes para evitar repetição.
     */
    @BeforeEach
    void setUp() {
        disciplinaValida = new Disciplina(ID_DISCIPLINA, "Direito Constitucional", "Estudo da Constituição Federal.");
    }

    /**
     * Testa o cenário de sucesso da criação de um resumo.
     */
    @Test
    @DisplayName("Deve criar um resumo com sucesso quando todos os dados são válidos")
    void deveCriarResumoComSucesso() {
        // Arrange
        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, TITULO, CONTEUDO);
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplinaValida));
        when(resumoRepository.salvar(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Resumo resultado = service.executar(command);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(TITULO, resultado.getTitulo());
        assertEquals(CONTEUDO, resultado.getConteudo());
        assertEquals(ID_USUARIO, resultado.getUsuarioId());
        assertEquals(disciplinaValida, resultado.getDisciplina());
        verify(disciplinaRepository).findById(ID_DISCIPLINA);
        verify(resumoRepository).salvar(any(Resumo.class));
    }

    /**
     * Agrupa os testes para cenários de validação e erro.
     */
    @Nested
    @DisplayName("Cenários de Validação e Erro")
    class ValidacaoErro {

        @Test
        @DisplayName("Deve lançar exceção quando a disciplina associada não existir")
        void deveLancarExcecaoQuandoDisciplinaNaoExistir() {
            // Arrange
            when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.empty());
            CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, TITULO, CONTEUDO);

            // Act & Assert
            assertThrows(DisciplinaNaoEncontradaException.class, () -> service.executar(command));
            verify(resumoRepository, never()).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o comando de criação for nulo")
        void deveLancarExcecaoQuandoComandoForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> service.executar(null));
            verifyNoInteractions(resumoRepository, disciplinaRepository);
        }

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o título for vazio")
        void deveRejeitarTituloVazio() {
            // Arrange
            when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplinaValida));
            CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, "  ", CONTEUDO);

            // Act & Assert
            assertThrows(CampoVazioException.class, () -> service.executar(command));
        }

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o título for nulo")
        void deveRejeitarTituloNulo() {
            // Arrange
            when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplinaValida));
            CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, null, CONTEUDO);

            // Act & Assert
            assertThrows(CampoNuloException.class, () -> service.executar(command));
        }
    }
}