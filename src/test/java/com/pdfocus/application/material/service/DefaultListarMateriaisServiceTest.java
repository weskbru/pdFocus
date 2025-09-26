package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository; // 1. IMPORT NECESSÁRIO
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultListarMateriaisService")
public class DefaultListarMateriaisServiceTest {

    @Mock
    private MaterialRepository materialRepositoryMock;

    // 2. ADICIONAMOS O MOCK DO USUARIOREPOSITORY, POIS O SERVICE AGORA DEPENDE DELE
    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @InjectMocks
    private DefaultListarMateriaisService service;

    private final UUID ID_DISCIPLINA = UUID.randomUUID();
    private final String EMAIL_USUARIO = "teste@email.com";

    @Test
    @DisplayName("Deve retornar a lista de materiais quando o repositório os encontra")
    void deveRetornarListaQuandoRepositorioEncontra() {
        // --- Arrange (Preparação) ---
        // 3. SIMULAMOS O USUÁRIO LOGADO E SEU COMPORTAMENTO
        Usuario usuarioLogado = mock(Usuario.class);
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        List<Material> materiaisEsperados = Collections.singletonList(mock(Material.class));

        // 4. SIMULAMOS O SECURITYCONTEXTHOLDER PARA "LOGAR" NOSSO USUÁRIO FALSO
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(EMAIL_USUARIO);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensinamos o usuarioRepository a encontrar nosso usuário falso
            when(usuarioRepositoryMock.buscarPorEmail(EMAIL_USUARIO)).thenReturn(Optional.of(usuarioLogado));

            // Ensinamos o materialRepository a usar o ID do nosso usuário falso
            when(materialRepositoryMock.listarPorDisciplinaEUsuario(ID_DISCIPLINA, usuarioLogado.getId()))
                    .thenReturn(materiaisEsperados);

            // --- Act (Ação) ---
            // 5. CHAMAMOS O MÉTODO EXECUTAR COM APENAS UM PARÂMETRO
            List<Material> resultado = service.executar(ID_DISCIPLINA);

            // --- Assert (Verificação) ---
            assertNotNull(resultado);
            assertEquals(materiaisEsperados, resultado);
            verify(materialRepositoryMock).listarPorDisciplinaEUsuario(ID_DISCIPLINA, usuarioLogado.getId());
        }
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o repositório não encontra materiais")
    void deveRetornarListaVazia() {
        // Arrange
        Usuario usuarioLogado = mock(Usuario.class);
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(EMAIL_USUARIO);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepositoryMock.buscarPorEmail(EMAIL_USUARIO)).thenReturn(Optional.of(usuarioLogado));

            when(materialRepositoryMock.listarPorDisciplinaEUsuario(ID_DISCIPLINA, usuarioLogado.getId()))
                    .thenReturn(Collections.emptyList());

            // Act
            List<Material> resultado = service.executar(ID_DISCIPLINA);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(materialRepositoryMock).listarPorDisciplinaEUsuario(ID_DISCIPLINA, usuarioLogado.getId());
        }
    }

}