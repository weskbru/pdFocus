package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultListarDisciplinasService}.
 * <p>
 * O objetivo destes testes é verificar a lógica da classe de serviço de forma isolada,
 * garantindo que ela se comporte como esperado sem depender de um banco de dados real.
 * </p>
 */
@ExtendWith(MockitoExtension.class) // Habilita o uso do Mockito com JUnit 5
@DisplayName("Testes Unitários - DefaultListarDisciplinasService")
// 1. O nome da classe de teste foi atualizado para corresponder à classe de serviço
public class DefaultListarDisciplinasServiceTest {

    /**
     * @Mock: Cria um "dublê" ou uma versão falsa da nossa interface DisciplinaRepository.
     * Isso nos permite controlar o que o repositório retorna em cada teste,
     * sem precisar de uma conexão real com o banco de dados.
     */
    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    /**
     * @InjectMocks: Cria uma instância real da nossa classe de serviço
     * e injeta automaticamente os "dublês" (mocks) que criamos acima nela.
     * Neste caso, o 'disciplinaRepositoryMock' será injetado no construtor do 'service'.
     */
    @InjectMocks
    // 2. O tipo da variável foi atualizado para o nome correto da classe de serviço
    private DefaultListarDisciplinasService service;

    // Um ID de usuário de exemplo que usaremos nos testes.
    private final UUID ID_USUARIO = UUID.randomUUID();

    @Test
    @DisplayName("Deve retornar a lista de disciplinas do usuário quando o repositório as encontra")
    void deveRetornarListaDeDisciplinasDoUsuario() {
        // --- Arrange (Preparação) ---
        // Nesta fase, preparamos tudo que o teste precisa.

        // 1. Criamos uma lista de resultado falsa. Não precisamos de Disciplinas reais,
        // apenas de uma lista para simular o retorno do repositório.
        List<Disciplina> disciplinasEsperadas = Collections.singletonList(mock(Disciplina.class));

        // 2. Ensinamos o nosso repositório falso (o mock) a se comportar.
        // "QUANDO o método 'listaTodasPorUsuario' for chamado com o ID_USUARIO,
        // ENTÃO retorne a lista 'disciplinasEsperadas' que preparamos."
        when(disciplinaRepositoryMock.listaTodasPorUsuario(ID_USUARIO)).thenReturn(disciplinasEsperadas);

        // --- Act (Ação) ---
        // Nesta fase, executamos o método que realmente queremos testar.
        // 3. Chamamos o método 'executar' do nosso serviço.
        List<Disciplina> resultado = service.executar(ID_USUARIO);

        // --- Assert (Verificação) ---
        // Nesta fase, verificamos se o resultado da ação é o que esperávamos.
        assertNotNull(resultado, "O resultado não deveria ser nulo.");
        assertEquals(disciplinasEsperadas, resultado, "A lista retornada deve ser a mesma que o repositório forneceu.");

        // Também verificamos se o nosso serviço interagiu corretamente com suas dependências.
        // "Verifique se o método 'listaTodasPorUsuario' do nosso repositório falso foi chamado exatamente 1 vez."
        verify(disciplinaRepositoryMock).listaTodasPorUsuario(ID_USUARIO);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o repositório não encontra disciplinas para o usuário")
    void deveRetornarListaVazia() {
        // Arrange: Ensinamos o repositório a retornar uma lista vazia.
        when(disciplinaRepositoryMock.listaTodasPorUsuario(ID_USUARIO)).thenReturn(Collections.emptyList());

        // Act: Executamos o serviço.
        List<Disciplina> resultado = service.executar(ID_USUARIO);

        // Assert: Verificamos se o resultado é de fato uma lista vazia.
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "A lista retornada deveria estar vazia.");
        verify(disciplinaRepositoryMock).listaTodasPorUsuario(ID_USUARIO);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
    void deveLancarExcecaoQuandoUsuarioIdForNulo() {
        // Arrange: Não precisamos preparar nada, pois vamos testar uma entrada inválida.

        // Act & Assert: Verificamos se a execução do serviço com um ID nulo
        // lança a exceção esperada (NullPointerException, por causa do Objects.requireNonNull).
        assertThrows(NullPointerException.class, () -> {
            service.executar(null);
        });

        // Verificação extra: Garantimos que, se a validação falhou,
        // o nosso serviço NUNCA chegou a tentar chamar o repositório.
        verifyNoInteractions(disciplinaRepositoryMock);
    }
}
