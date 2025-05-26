package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.dto.CriarDisciplinaCommand;
import com.pdfocus.application.resumo.port.saida.DisciplinaRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.models.Disciplina;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link CriarDisciplinaService}.
 */
@ExtendWith(MockitoExtension.class)
public class CriarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private CriarDisciplinaService criarDisciplinaService;

    private final String NOME_DISCIPLINA_VALIDO = "Matemática Avançada";
    private final String DESCRICAO_DISCIPLINA_VALIDA = "Estudo de tópicos avançados em matemática.";

    /**
     * Testa a criação bem-sucedida de uma disciplina.
     * Garante que uma nova {@link Disciplina} é criada e salva corretamente
     * através do {@link DisciplinaRepository}.
     */
    @Test
    void DeveCriarDisciplinaComSucesso() {
        // Arrange
        CriarDisciplinaCommand command = new CriarDisciplinaCommand(NOME_DISCIPLINA_VALIDO, DESCRICAO_DISCIPLINA_VALIDA);

        // Captura a Disciplina que será passada para o repositório
        ArgumentCaptor<Disciplina> disciplinaCaptor = ArgumentCaptor.forClass(Disciplina.class);
        when(disciplinaRepository.salvar(disciplinaCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Disciplina disciplinaCriada = criarDisciplinaService.criar(command);

        // Assert
        assertNotNull(disciplinaCriada);
        assertNotNull(disciplinaCriada.getId());
        assertEquals(NOME_DISCIPLINA_VALIDO, disciplinaCriada.getNome());
        assertEquals(DESCRICAO_DISCIPLINA_VALIDA, disciplinaCriada.getDescricao());

        // Verifica se o método salvar do repositório foi chamado uma vez
        verify(disciplinaRepository, times(1)).salvar(any(Disciplina.class));

        // Verifica os valores exatos passados para o mock
        Disciplina disciplinaSalva = disciplinaCaptor.getValue();
        assertNotNull(disciplinaSalva.getId());
        assertEquals(NOME_DISCIPLINA_VALIDO, disciplinaSalva.getNome());
        assertEquals(DESCRICAO_DISCIPLINA_VALIDA, disciplinaSalva.getDescricao());
    }

    /**
     * Testa o cenário em que ocorre uma exceção {@link CampoVazioException}
     * ao tentar criar uma disciplina com um nome contendo apenas espaços.
     * Garante que o {@link DisciplinaRepository#salvar(Disciplina)} não é chamado.
     */
    @Test
    void deveLancarExcecaoSeNomeForVazio() {
        // Arrange
        String nomeVazio = " ";
        String descricaoQualquer = "Descrição de teste";
        CriarDisciplinaCommand command = new CriarDisciplinaCommand(nomeVazio, descricaoQualquer);

        // Act & Assert
        assertThrows(CampoVazioException.class, () -> {
            criarDisciplinaService.criar(command);
        });

        // Garante que o metodo salvar do repositório não foi chamado
        verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
    }

    /**
     * Testa o cenário em que ocorre uma exceção {@link CampoNuloException}
     * ao tentar criar uma disciplina com um nome nulo.
     * Garante que o {@link DisciplinaRepository#salvar(Disciplina)} não é chamado.
     */
    @Test
    void deveLancarExcecaoSeNomeForNulo() {
        // Arrange
        String nomeNulo = null;
        String descricaoQualquer = "Descrição de teste";
        CriarDisciplinaCommand command = new CriarDisciplinaCommand(nomeNulo, descricaoQualquer);

        // Act & Assert
        assertThrows(CampoNuloException.class, () -> { // Mantendo CampoVazioException, conforme a sua lógica
            criarDisciplinaService.criar(command);
        });

        // Garante que o metodo salvar do repositório não foi chamado
        verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
    }

}