package com.pdfocus.application.material.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Teste unitário para DefaultBuscarMateriaisRecentesService.
 * Foco: Validar a lógica de busca e mapeamento de materiais recentes,
 * incluindo a interação com repositórios dependentes (Usuario, Disciplina).
 */
@ExtendWith(MockitoExtension.class)
class DefaultBuscarMateriaisRecentesServiceTest {

    // Mocks das nossas portas de saída para isolar o serviço.
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;

    // Instância real do serviço sob teste, com os mocks injetados.
    @InjectMocks
    private DefaultBuscarMateriaisRecentesService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaTeste;
    private Material materialTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Recentes", "recentes@email.com", "hash");
        disciplinaTeste = new Disciplina(UUID.randomUUID(), "Cálculo I", "", usuarioTeste.getId());

        materialTeste = Material.criar(
                UUID.randomUUID(),
                "derivadas.pdf",
                "storage-name.pdf",
                "application/pdf",
                1024L,
                usuarioTeste.getId(),
                disciplinaTeste.getId(),
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve buscar e mapear corretamente os materiais recentes do usuário logado")
    void deveBuscarEMapearMateriaisRecentes() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn("recentes@email.com");
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail("recentes@email.com")).thenReturn(Optional.of(usuarioTeste));
            when(materialRepository.buscar5MaisRecentesPorUsuario(usuarioTeste)).thenReturn(List.of(materialTeste));
            when(disciplinaRepository.buscarPorId(disciplinaTeste.getId())).thenReturn(Optional.of(disciplinaTeste));

            // Act
            List<MaterialRecenteResponse> resultado = service.executar();

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(materialTeste.getId(), resultado.get(0).id());
            assertEquals(disciplinaTeste.getNome(), resultado.get(0).nomeDisciplina());

            verify(materialRepository, times(1)).buscar5MaisRecentesPorUsuario(usuarioTeste);
            verify(disciplinaRepository, times(1)).buscarPorId(disciplinaTeste.getId());
        }
    }
}