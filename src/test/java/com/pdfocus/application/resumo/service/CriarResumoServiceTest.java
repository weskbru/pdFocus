package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
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
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class CriarResumoServiceTest {

    @Mock
    private ResumoRepository resumoRepository;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private CriarResumoService service;

    // Dados de teste reutilizáveis
    private final UUID ID_USUARIO = UUID.randomUUID();
    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final String TITULO = "Resumo de Matemática";
    private final String CONTEUDO = "Conteúdo do resumo...";

    @Test
    void deveCriarResumoComSucesso() {
        // Arrange
        Disciplina disciplina = new Disciplina(ID_DISCIPLINA, "Matemática", "Álgebra Linear");
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplina));
        when(resumoRepository.salvar(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, TITULO, CONTEUDO);

        // Act
        Resumo resultado = service.execute(command);

        // Assert
        assertNotNull(resultado);
        assertEquals(TITULO, resultado.getTitulo());
        assertEquals(CONTEUDO, resultado.getConteudo());
        assertEquals(ID_USUARIO, resultado.getUsuarioId());
        assertEquals(disciplina, resultado.getDisciplina());

        verify(disciplinaRepository, times(1)).findById(ID_DISCIPLINA);
        verify(resumoRepository, times(1)).salvar(any(Resumo.class));
    }

    @Test
    void deveLancarExcecaoQuandoDisciplinaNaoExistir() {
        // Arrange
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.empty());

        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, TITULO, CONTEUDO);

        // Act & Assert
        DisciplinaNaoEncontradaException exception = assertThrows(
                DisciplinaNaoEncontradaException.class,
                () -> service.execute(command)
        );

        assertEquals("Disciplina não encontrada com ID: " + ID_DISCIPLINA, exception.getMessage());
        verify(disciplinaRepository).findById(ID_DISCIPLINA);
        verify(resumoRepository, never()).salvar(any());
    }


    @Test
    void deveRejeitarTituloVazio() {
        // Arrange
        Disciplina disciplina = new Disciplina(ID_DISCIPLINA, "Matemática", "Álgebra Linear");
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplina));

        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, "", CONTEUDO);

        // Act & Assert
        assertThrows(CampoVazioException.class, () -> service.execute(command));
    }

    @Test
    void deveRejeitarTituloNulo() {
        Disciplina disciplina = new Disciplina(ID_DISCIPLINA, "Matemática", "Álgebra Linear");
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplina));

        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, null, CONTEUDO);

        assertThrows(CampoNuloException.class, () -> service.execute(command));
    }


    @Test
    void deveGerarIdAutomaticoParaResumo() {
        // Arrange
        Disciplina disciplina = new Disciplina(ID_DISCIPLINA, "Matemática", "Álgebra Linear");
        when(disciplinaRepository.findById(ID_DISCIPLINA)).thenReturn(Optional.of(disciplina));
        when(resumoRepository.salvar(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CriarResumoCommand command = new CriarResumoCommand(ID_USUARIO, ID_DISCIPLINA, TITULO, CONTEUDO);

        // Act
        Resumo resultado = service.execute(command);

        // Assert
        assertNotNull(resultado.getId());
        assertTrue(resultado.getId() instanceof UUID);
    }

}
