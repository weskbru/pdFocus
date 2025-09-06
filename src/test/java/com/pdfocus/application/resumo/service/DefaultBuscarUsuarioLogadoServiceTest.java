package com.pdfocus.application.resumo.service;

import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.application.usuario.service.DefaultBuscarUsuarioLogadoService;
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
 * Teste unitário para DefaultBuscarUsuarioLogadoService.
 * Foco: Validar a lógica de negócio em isolamento, usando Mocks para simular
 * as dependências externas como o repositório e o contexto de segurança.
 */
@ExtendWith(MockitoExtension.class)
class DefaultBuscarUsuarioLogadoServiceTest {

    // Mock (simulação) da nossa porta de saída para isolar o teste do banco de dados.
    @Mock
    private UsuarioRepository usuarioRepository;

    // Instância real do serviço sob teste, com os mocks acima injetados.
    @InjectMocks
    private DefaultBuscarUsuarioLogadoService service;

    private Usuario usuarioTeste;

    // Prepara um cenário comum antes da execução de cada teste.
    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Teste", "teste@email.com", "senhaHash");
    }

    @Test
    @DisplayName("Deve retornar o usuário logado com sucesso quando encontrado")
    void deveRetornarUsuarioLogadoComSucesso() {
        // Arrange (Preparação)
        // Simula o SecurityContextHolder estático para controlar quem está "logado" no teste.
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);

            when(userDetailsMock.getUsername()).thenReturn("teste@email.com");
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensina ao nosso mock do repositório como se comportar.
            when(usuarioRepository.buscarPorEmail("teste@email.com")).thenReturn(Optional.of(usuarioTeste));

            // Act (Ação)
            Usuario resultado = service.executar();

            // Assert (Verificação)
            assertNotNull(resultado);
            assertEquals(usuarioTeste.getId(), resultado.getId());
            assertEquals("teste@email.com", resultado.getEmail());

            // Verifica se a interação esperada com o mock realmente aconteceu.
            verify(usuarioRepository, times(1)).buscarPorEmail("teste@email.com");
        }
    }
}

