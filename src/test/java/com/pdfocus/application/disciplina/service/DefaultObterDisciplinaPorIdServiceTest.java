package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultObterDisciplinaPorIdService}.
 */
@ExtendWith(MockitoExtension.class)
class DefaultObterDisciplinaPorIdServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DefaultObterDisciplinaPorIdService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Get", "get@email.com", "hash");
        disciplinaTeste = new Disciplina(UUID.randomUUID(), "Disciplina Teste", "", usuarioTeste.getId());
    }

    @Test
    @DisplayName("Deve retornar a disciplina quando ela pertence ao utilizador logado")
    void deveRetornarDisciplinaComSucesso() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Prepara a simulação do utilizador logado.
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

            // Act
            Optional<Disciplina> resultado = service.executar(disciplinaTeste.getId());

            // Assert
            assertTrue(resultado.isPresent());
            assertEquals(disciplinaTeste.getId(), resultado.get().getId());
            verify(disciplinaRepository).findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar disciplina que não pertence ao utilizador")
    void deveRetornarVazioParaDisciplinaDeOutroUsuario() {
        // Arrange
        // AQUI ESTÁ A CORREÇÃO:
        // Precisamos de simular o contexto de segurança também neste cenário de teste.
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Prepara a simulação do utilizador logado.
            UserDetails userDetailsMock = mock(UserDetails.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            Authentication authenticationMock = mock(Authentication.class);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula que a busca segura FALHOU
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Disciplina> resultado = service.executar(disciplinaTeste.getId());

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
        }
    }
}

