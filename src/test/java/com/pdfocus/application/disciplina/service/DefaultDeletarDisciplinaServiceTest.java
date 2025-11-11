package com.pdfocus.application.disciplina.service;

// --- IMPORT DE "CONTRATOS" (PORTAS) ---
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository; // <-- 1. IMPORT NECESSÁRIO
import com.pdfocus.application.resumo.port.saida.ResumoRepository; // <-- 1. IMPORT NECESSÁRIO
// --- FIM DOS IMPORTS DE CONTRATO ---
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultDeletarDisciplinaService}.
 * <p>
 * (Pilar 4) - Testes atualizados para refletir a refatoração
 * de "cascade delete" (novas dependências de Resumo e Material).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarDisciplinaService")
class DefaultDeletarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    // --- 2. ADICIONAR OS MOCKS QUE FALTAVAM ---
    @Mock
    private ResumoRepository resumoRepository;
    @Mock
    private MaterialRepository materialRepository;
    // --- FIM DA CORREÇÃO ---

    @InjectMocks
    private DefaultDeletarDisciplinaService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Delete", "delete@email.com", "hash");
        disciplinaTeste = new Disciplina(UUID.randomUUID(), "Disciplina a Apagar", "", usuarioTeste.getId());
    }

    @Test
    @DisplayName("Deve apagar a disciplina e seus 'filhos' com sucesso se ela pertence ao usuário logado")
    void deveApagarDisciplinaComSucesso() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Arrange (Mock do Usuário)
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));

            // Arrange (Mock da Lógica de Segurança)
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.of(disciplinaTeste));

            // --- 3. MOCKAR O NOVO COMPORTAMENTO (CASCADE DELETE) ---
            // "Ensina" os novos mocks a não fazer nada (e evitar o NPE)
            doNothing().when(resumoRepository).deletarTodosPorDisciplinaId(disciplinaTeste.getId());
            doNothing().when(materialRepository).deletarTodosPorDisciplinaId(disciplinaTeste.getId());
            // --- FIM DA CORREÇÃO ---

            doNothing().when(disciplinaRepository).deletarPorId(disciplinaTeste.getId());

            // Act (A linha que falhava - Linha 68)
            assertDoesNotThrow(() -> service.executar(disciplinaTeste.getId()));

            // Assert (Verifica se a lógica completa foi chamada)
            verify(resumoRepository, times(1)).deletarTodosPorDisciplinaId(disciplinaTeste.getId());
            verify(materialRepository, times(1)).deletarTodosPorDisciplinaId(disciplinaTeste.getId());
            verify(disciplinaRepository, times(1)).deletarPorId(disciplinaTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar apagar disciplina que não pertence ao usuário")
    void deveLancarExcecaoAoApagarDisciplinaDeOutroUsuario() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Arrange (Mock do Usuário)
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));

            // Arrange (Mock da Falha de Segurança)
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty()); // <-- A segurança falha aqui

            // Act
            assertThrows(DisciplinaNaoEncontradaException.class, () -> {
                service.executar(disciplinaTeste.getId());
            });

            // Assert (Verifica que a exclusão NUNCA foi chamada)
            verify(disciplinaRepository, never()).deletarPorId(any(UUID.class));
            verify(resumoRepository, never()).deletarTodosPorDisciplinaId(any(UUID.class));
            verify(materialRepository, never()).deletarTodosPorDisciplinaId(any(UUID.class));
        }
    }
}