package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
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

import java.util.ArrayList; // Import para listas vazias
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

    // Adicionando os mocks que faltavam para a lógica do service
    @Mock
    private com.pdfocus.application.resumo.port.saida.ResumoRepository resumoRepository;

    @Mock
    private com.pdfocus.application.material.port.saida.MaterialRepository materialRepository;


    @InjectMocks
    private DefaultObterDisciplinaPorIdService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Get", "get@email.com", "hash");

        disciplinaTeste = new Disciplina(
                UUID.randomUUID(),
                "Disciplina Teste",
                "Descrição da disciplina para teste",
                usuarioTeste.getId()
        );
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

            // Simula que não há resumos ou materiais para simplificar o teste
            when(resumoRepository.buscarPorDisciplinaEUsuario(any(), any())).thenReturn(new ArrayList<>());
            when(materialRepository.listarPorDisciplinaEUsuario(any(), any())).thenReturn(new ArrayList<>());

            // Act
            Optional<DetalheDisciplinaResponse> resultado = service.executar(disciplinaTeste.getId());

            // Assert
            assertTrue(resultado.isPresent());
            // ✅ CORREÇÃO AQUI: trocado .getId() por .id() pois é um record
            assertEquals(disciplinaTeste.getId(), resultado.get().id());
            verify(disciplinaRepository).findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar disciplina que não pertence ao utilizador")
    void deveRetornarVazioParaDisciplinaDeOutroUsuario() {
        // Arrange
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
            Optional<DetalheDisciplinaResponse> resultado = service.executar(disciplinaTeste.getId());

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
        }
    }
}