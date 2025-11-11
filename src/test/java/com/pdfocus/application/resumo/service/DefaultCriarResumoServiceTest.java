package com.pdfocus.application.resumo.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.dto.CriarResumoCommand;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
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
 * Testes unitários para a classe {@link DefaultCriarResumoService}.
 * Valida a lógica de negócio para a criação de novos resumos.
 *
 * <p><b>[REFATORAÇÃO - PILAR 3]</b>
 * Os testes foram atualizados para refletir a refatoração de segurança
 * (multi-tenancy). O mock do {@link DisciplinaRepository} agora valida
 * a chamada ao método {@code findByIdAndUsuarioId} em vez do antigo {@code findById}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultCriarResumoService")
public class DefaultCriarResumoServiceTest {

    @Mock
    private ResumoRepository resumoRepository;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DefaultCriarResumoService service;

    // Dados de teste reutilizáveis
    private final UUID ID_USUARIO = UUID.randomUUID();
    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final String TITULO = "Resumo de Direito Constitucional";
    private final String CONTEUDO = "Artigo 5º da Constituição Federal.";
    private Disciplina disciplinaValida;

    /**
     * Configura objetos comuns usados em múltiplos testes para evitar repetição.
     * (Nota: O construtor de Disciplina pode precisar ser adaptado se o modelo mudou)
     */
    @BeforeEach
    void setUp() {
        // Assumindo que o construtor de Disciplina ainda é válido para o mock
        disciplinaValida = new Disciplina(ID_DISCIPLINA, "Direito Constitucional", "Estudo da CF.", ID_USUARIO);
    }

    /**
     * Testa o cenário de sucesso da criação de um resumo.
     */
    @Test
    @DisplayName("Deve criar um resumo com sucesso quando todos os dados são válidos")
    void deveCriarResumoComSucesso() {
        // Arrange (Preparação)
        CriarResumoCommand command = new CriarResumoCommand(ID_DISCIPLINA, TITULO, CONTEUDO);

        // --- CORREÇÃO APLICADA AQUI ---
        // Agora zombamos (mock) o método de segurança correto: findByIdAndUsuarioId
        when(disciplinaRepository.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                .thenReturn(Optional.of(disciplinaValida));
        // --- FIM DA CORREÇÃO ---

        when(resumoRepository.salvar(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act (Ação)
        Resumo resultado = service.executar(command, ID_USUARIO);

        // Assert (Verificação)
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(TITULO, resultado.getTitulo());
        assertEquals(CONTEUDO, resultado.getConteudo());
        assertEquals(ID_USUARIO, resultado.getUsuarioId());
        assertEquals(disciplinaValida, resultado.getDisciplina());

        // --- CORREÇÃO APLICADA AQUI ---
        // Verificamos se o método de segurança correto foi chamado
        verify(disciplinaRepository).findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO);
        // --- FIM DA CORREÇÃO ---

        verify(resumoRepository).salvar(any(Resumo.class));
    }

    /**
     * Agrupa os testes para cenários de validação e erro.
     */
    @Nested
    @DisplayName("Cenários de Validação e Erro")
    class ValidacaoErro {

        @Test
        @DisplayName("Deve lançar exceção quando a disciplina associada não existir (ou não pertencer ao usuário)")
        void deveLancarExcecaoQuandoDisciplinaNaoExistir() {
            // Arrange
            CriarResumoCommand command = new CriarResumoCommand(ID_DISCIPLINA, TITULO, CONTEUDO);

            // --- CORREÇÃO APLICADA AQUI ---
            // Zombamos (mock) o método de segurança, que é o que o serviço realmente chama.
            when(disciplinaRepository.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Optional.empty());
            // --- FIM DA CORREÇÃO ---

            // Act & Assert
            // O serviço (corretamente) lançará a exceção porque o mock retornou vazio.
            assertThrows(DisciplinaNaoEncontradaException.class, () -> service.executar(command, ID_USUARIO));
            verify(resumoRepository, never()).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o comando de criação for nulo")
        void deveLancarExcecaoQuandoComandoForNulo() {
            // Act & Assert
            // (Nenhuma mudança necessária aqui, pois a validação ocorre antes das chamadas ao repo)
            assertThrows(NullPointerException.class, () -> service.executar(null, ID_USUARIO));
            verifyNoInteractions(resumoRepository, disciplinaRepository);
        }

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o título for vazio")
        void deveRejeitarTituloVazio() {
            // Arrange
            CriarResumoCommand command = new CriarResumoCommand(ID_DISCIPLINA, "  ", CONTEUDO);

            // --- CORREÇÃO APLICADA AQUI ---
            // Precisamos do mock correto para que o código passe pela verificação
            // da disciplina e chegue à validação do título (que é feita no Resumo.criar).
            when(disciplinaRepository.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Optional.of(disciplinaValida));
            // --- FIM DA CORREÇÃO ---

            // Act & Assert
            assertThrows(CampoVazioException.class, () -> service.executar(command, ID_USUARIO));
        }

        @Test
        @DisplayName("Deve lançar exceção do domínio quando o título for nulo")
        void deveRejeitarTituloNulo() {
            // Arrange
            CriarResumoCommand command = new CriarResumoCommand(ID_DISCIPLINA, null, CONTEUDO);

            // --- CORREÇÃO APLICADA AQUI ---
            // Mesmo caso do teste anterior: mock correto é necessário.
            when(disciplinaRepository.findByIdAndUsuarioId(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Optional.of(disciplinaValida));
            // --- FIM DA CORREÇÃO ---

            // Act & Assert
            assertThrows(CampoNuloException.class, () -> service.executar(command, ID_USUARIO));
        }
    }
}