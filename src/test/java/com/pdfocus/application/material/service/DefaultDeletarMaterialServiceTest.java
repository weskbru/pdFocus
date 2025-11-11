package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarMaterialService")
public class DefaultDeletarMaterialServiceTest {

    @Mock
    private MaterialRepository materialRepositoryMock;
    @Mock
    private MaterialStoragePort materialStoragePortMock;
    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @InjectMocks
    private DefaultDeletarMaterialService service;

    private final UUID ID_MATERIAL = UUID.randomUUID();
    private final String EMAIL_USUARIO = "teste@email.com";
    private final String NOME_STORAGE = "abc-123.pdf";

    @Test
    @DisplayName("Deve apagar o arquivo físico e o registro do DB quando o material pertence ao usuário logado")
    void deveApagarArquivoERegistroComSucesso() {
        // --- Arrange (Preparação) ---
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

            Material materialEncontrado = mock(Material.class);
            when(materialEncontrado.getNomeStorage()).thenReturn(NOME_STORAGE);


            when(materialRepositoryMock.buscarPorIdEUsuario(ID_MATERIAL, usuarioLogado.getId()))
                    .thenReturn(Optional.of(materialEncontrado));

            // --- Act (Ação) ---
            service.executar(ID_MATERIAL);

            // --- Assert (Verificação) ---
            InOrder inOrder = inOrder(materialStoragePortMock, materialRepositoryMock);
            inOrder.verify(materialStoragePortMock).apagar(NOME_STORAGE);
            inOrder.verify(materialRepositoryMock).deletarPorIdEUsuario(ID_MATERIAL, usuarioLogado.getId());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção se o material não for encontrado para o usuário logado")
    void deveLancarExcecaoQuandoMaterialNaoEncontrado() {
        // --- Arrange ---
        Usuario usuarioLogado = mock(Usuario.class);
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            when(userDetailsMock.getUsername()).thenReturn(EMAIL_USUARIO);
            Authentication authenticationMock = mock(Authentication.class);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepositoryMock.buscarPorEmail(EMAIL_USUARIO)).thenReturn(Optional.of(usuarioLogado));


            when(materialRepositoryMock.buscarPorIdEUsuario(ID_MATERIAL, usuarioLogado.getId()))
                    .thenReturn(Optional.empty());

            // --- Act & Assert ---
            assertThrows(MaterialNaoEncontradoException.class, () -> {
                service.executar(ID_MATERIAL);
            });

            // Garante que nenhuma operação de deleção foi tentada
            verify(materialStoragePortMock, never()).apagar(anyString());
            verify(materialRepositoryMock, never()).deletarPorIdEUsuario(any(), any());
        }
    }
}