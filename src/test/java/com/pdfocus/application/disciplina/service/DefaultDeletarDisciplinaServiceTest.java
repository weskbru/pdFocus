package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarDisciplinaService")
class DefaultDeletarDisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

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
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.of(disciplinaTeste));
            doNothing().when(disciplinaRepository).deletarPorId(disciplinaTeste.getId());

            assertDoesNotThrow(() -> service.executar(disciplinaTeste.getId()));

            verify(disciplinaRepository, times(1)).deletarPorId(disciplinaTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar apagar disciplina que não pertence ao usuário")
    void deveLancarExcecaoAoApagarDisciplinaDeOutroUsuario() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // ✅ CORREÇÃO FINAL: Completamos a configuração do mock de segurança aqui
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(DisciplinaNaoEncontradaException.class, () -> {
                service.executar(disciplinaTeste.getId());
            });

            verify(disciplinaRepository, never()).deletarPorId(any(UUID.class));
        }
    }
}