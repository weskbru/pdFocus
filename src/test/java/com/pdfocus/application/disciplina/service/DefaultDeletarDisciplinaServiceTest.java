package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
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
 * Foco: Validar a lógica de segurança que verifica a posse da disciplina
 * antes de executar a operação de deleção.
 */
@ExtendWith(MockitoExtension.class)
class DefaultDeletarDisciplinaServiceTest {

    // Mocks das dependências para isolar o serviço.
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    // Instância real do serviço sob teste.
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
    @DisplayName("Deve apagar a disciplina com sucesso se ela pertence ao usuário logado")
    void deveApagarDisciplinaComSucesso() {
        // Arrange (Preparação do cenário de sucesso)
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Simula o usuário autenticado
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensina aos mocks como se comportar
            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula que a verificação de posse FOI BEM-SUCEDIDA
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.of(disciplinaTeste));
            doNothing().when(disciplinaRepository).deletarPorId(disciplinaTeste.getId());

            // Act (Ação) & Assert (Verificação)
            // Executamos o método e verificamos que nenhuma exceção foi lançada.
            assertDoesNotThrow(() -> service.executar(disciplinaTeste.getId()));

            // Verificamos se a chamada para apagar foi realmente feita.
            verify(disciplinaRepository, times(1)).deletarPorId(disciplinaTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar apagar disciplina que não pertence ao usuário")
    void deveLancarExcecaoAoApagarDisciplinaDeOutroUsuario() {
        // Arrange (Preparação do cenário de FALHA de segurança)
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Simula o usuário autenticado
            UserDetails userDetailsMock = mock(UserDetails.class);
            // ... (setup de segurança igual ao anterior) ...
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            // ...

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula que a verificação de posse FALHOU (repositório não encontrou a disciplina para este usuário)
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            // Verificamos se o nosso serviço corretamente lança a exceção de segurança.
            assertThrows(DisciplinaNaoEncontradaException.class, () -> {
                service.executar(disciplinaTeste.getId());
            });

            // A VERIFICAÇÃO MAIS IMPORTANTE:
            // Garantimos que o método para apagar NUNCA foi chamado se a verificação de posse falhou.
            verify(disciplinaRepository, never()).deletarPorId(any(UUID.class));
        }
    }
}
