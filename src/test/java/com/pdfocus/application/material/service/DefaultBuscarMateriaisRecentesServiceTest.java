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
 * <p>
 * (Pilar 4) - Testes atualizados para validar o fluxo de segurança (multi-tenancy)
 * e a refatoração de performance (N+1 Fix).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class DefaultBuscarMateriaisRecentesServiceTest {

    // Mocks das nossas portas de saída para isolar o serviço.
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository; // Mock ainda necessário (para o 'never()')

    // Instância real do serviço sob teste, com os mocks injetados.
    @InjectMocks
    private DefaultBuscarMateriaisRecentesService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaTeste;
    private Material materialTesteComDisciplina; // Nomeado para clareza

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Recentes", "recentes@email.com", "hash");
        disciplinaTeste = new Disciplina(UUID.randomUUID(), "Cálculo I", "", usuarioTeste.getId());

        // --- CORREÇÃO NO SETUP ---
        // Usamos o método de fábrica que anexa a Disciplina,
        // pois é isso que o Mapper (no Adapter) agora faz.
        materialTesteComDisciplina = Material.reconstituirComDisciplina(
                UUID.randomUUID(),
                "derivadas.pdf",
                "storage-name.pdf",
                "application/pdf",
                1024L,
                usuarioTeste.getId(),
                OffsetDateTime.now(),
                disciplinaTeste // <-- Anexamos a disciplina
        );
        // --- FIM DA CORREÇÃO ---
    }

    @Test
    @DisplayName("Deve buscar (com JOIN) e mapear corretamente os materiais recentes do usuário")
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

            // --- CORREÇÃO NO MOCK (N+1 FIX) ---
            // Zombamos (mock) o *novo* método performático (1 query)
            when(materialRepository.buscar5MaisRecentesPorUsuarioComDisciplina(usuarioTeste))
                    .thenReturn(List.of(materialTesteComDisciplina));

            // NÃO precisamos mais zombar (mock) o disciplinaRepository.findById()
            // --- FIM DA CORREÇÃO ---

            // Act
            List<MaterialRecenteResponse> resultado = service.executar();

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals(materialTesteComDisciplina.getId(), resultado.get(0).id());
            assertEquals(disciplinaTeste.getNome(), resultado.get(0).nomeDisciplina()); // O nome veio do JOIN

            // --- CORREÇÃO NA VERIFICAÇÃO (N+1 FIX) ---
            // Verificamos se o método RÁPIDO foi chamado
            verify(materialRepository, times(1)).buscar5MaisRecentesPorUsuarioComDisciplina(usuarioTeste);

            // Verificamos se o método ANTIGO (lento) NÃO foi chamado
            verify(materialRepository, never()).buscar5MaisRecentesPorUsuario(any());

            // Provamos que o N+1 foi corrigido: o disciplinaRepository NUNCA é chamado
            verify(disciplinaRepository, never()).findById(any());
            // --- FIM DA CORREÇÃO ---
        }
    }
}