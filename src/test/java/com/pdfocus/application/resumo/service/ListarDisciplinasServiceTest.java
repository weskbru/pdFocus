package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para a classe {@link ListarDisciplinasService}.
 * Garante que o serviço de listagem de disciplinas se comporta como esperado
 * em diferentes cenários, utilizando um mock para o {@link DisciplinaRepository}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários para ListarDisciplinasService")
public class ListarDisciplinasServiceTest {

    /**
     * Mock do repositório de disciplinas, usado para simular o comportamento
     * da camada de persistência sem acessá-la de fato.
     */
    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    /**
     * Instância do serviço a ser testado. As dependências mockadas (como
     * {@code disciplinaRepositoryMock}) são injetadas nesta instância.
     */
    @InjectMocks
    private ListarDisciplinasService listarDisciplinasService;

    private Disciplina disciplina1;
    private Disciplina disciplina2;

    /**
     * Configuração inicial executada antes de cada método de teste.
     * Inicializa instâncias de {@link Disciplina} que são usadas como dados
     * de exemplo nos testes.
     */
    @BeforeEach
    void setUp() {
        // Inicializando algumas disciplinas de exemplo para os testes
        disciplina1 = new Disciplina(UUID.randomUUID(), "Matemática", "Cálculo e Álgebra");
        disciplina2 = new Disciplina(UUID.randomUUID(), "Português", "Gramática e Literatura");
    }

    /**
     * Testa se o serviço retorna corretamente uma lista de disciplinas
     * quando o repositório as encontra e as retorna.
     * Verifica se a lista não é nula, tem o tamanho esperado, contém os elementos corretos
     * e se o método {@code listaTodas} do repositório foi chamado.
     */
    @Test
    @DisplayName("Deve retornar lista de disciplinas quando o repositório as encontrar")
    void deveRetornarListaDeDisciplinasQuandoRepositorioEncontrar() {
        // Arrange
        List<Disciplina> disciplinasEsperadas = Arrays.asList(disciplina1, disciplina2);
        when(disciplinaRepositoryMock.listarTodas()).thenReturn(disciplinasEsperadas);

        // Act
        List<Disciplina> disciplinasRetornadas = listarDisciplinasService.listarTodas();

        // Assert
        assertNotNull(disciplinasRetornadas, "A lista de disciplinas retornadas não deve ser nula.");
        assertEquals(2, disciplinasRetornadas.size(), "A lista de disciplinas deve conter 2 elementos.");
        assertEquals(disciplinasEsperadas, disciplinasRetornadas, "A lista de disciplinas retornada deve ser igual à esperada.");
        // Verifica se os elementos são os mesmos (opcional, mas bom para garantir a ordem e conteúdo)
        assertEquals(disciplina1.getNome(), disciplinasRetornadas.get(0).getNome(), "O nome da primeira disciplina não corresponde ao esperado.");
        assertEquals(disciplina2.getNome(), disciplinasRetornadas.get(1).getNome(), "O nome da segunda disciplina não corresponde ao esperado.");

        // Verifica se o método do mock foi chamado
        verify(disciplinaRepositoryMock).listarTodas();
    }

    /**
     * Testa se o serviço retorna uma lista vazia quando o repositório
     * não encontra nenhuma disciplina (e, portanto, retorna uma lista vazia).
     * Verifica se a lista retornada não é nula e se seu tamanho é zero,
     * e se o método {@code listaTodas} do repositório foi chamado.
     */
    @Test
    @DisplayName("Deve retornar lista vazia quando o repositório não encontrar disciplinas")
    void deveRetornarListaVaziaQuandoRepositorioNaoEncontrarDisciplinas() {
        // Arrange
        when(disciplinaRepositoryMock.listarTodas()).thenReturn(Collections.emptyList());

        // Act
        List<Disciplina> disciplinasRetornadas = listarDisciplinasService.listarTodas();

        // Assert
        assertNotNull(disciplinasRetornadas, "A lista de disciplinas retornadas não deve ser nula, mesmo que vazia.");
        assertEquals(0, disciplinasRetornadas.size(), "A lista de disciplinas deve estar vazia.");

        // Verifica se o método do mock foi chamado
        verify(disciplinaRepositoryMock).listarTodas();
    }
}