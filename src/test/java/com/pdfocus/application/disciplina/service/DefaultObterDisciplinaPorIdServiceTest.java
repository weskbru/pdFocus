package com.pdfocus.application.disciplina.service;

// --- IMPORT DE "CONTRATOS" (PORTAS) ---
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
// --- FIM DOS IMPORTS DE CONTRATO ---

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material; // Import necessário
import com.pdfocus.core.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

// --- IMPORTS NECESSÁRIOS PARA PAGINAÇÃO ---
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
// --- FIM DOS IMPORTS DE PAGINAÇÃO ---

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List; // Import necessário
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultObterDisciplinaPorIdService}.
 * <p>
 * (Pilar 4) - Testes atualizados para validar o fluxo de segurança (multi-tenancy)
 * e o carregamento de dados agregados, incluindo paginação de materiais.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class DefaultObterDisciplinaPorIdServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ResumoRepository resumoRepository;
    @Mock
    private MaterialRepository materialRepository;

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
        // Cria um objeto Pageable "qualquer" para o teste
        Pageable pageableMock = Pageable.unpaged();
        // Cria uma Página "vazia" de Materiais para o mock retornar
        Page<Material> paginaVaziaDeMateriais = new PageImpl<>(new ArrayList<>());

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Prepara a simulação do utilizador logado.
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Simula o "caminho feliz" da nossa lógica
            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.of(disciplinaTeste));
            when(resumoRepository.buscarPorDisciplinaEUsuario(any(), any())).thenReturn(new ArrayList<>());

            // --- CORREÇÃO DO MOCK DE MATERIAIS ---
            // O serviço não chama mais 'listarPorDisciplinaEUsuario',
            // ele agora chama 'buscarPorDisciplinaDeFormaPaginada'.
            when(materialRepository.buscarPorDisciplinaDeFormaPaginada(disciplinaTeste.getId(), pageableMock))
                    .thenReturn(paginaVaziaDeMateriais);
            // --- FIM DA CORREÇÃO ---

            // --- CORREÇÃO DA CHAMADA DO SERVIÇO ---
            // Passamos os 2 argumentos que o serviço agora espera.
            // Act
            Optional<DetalheDisciplinaResponse> resultado = service.executar(disciplinaTeste.getId(), pageableMock);
            // --- FIM DA CORREÇÃO ---

            // Assert
            assertTrue(resultado.isPresent());
            assertEquals(disciplinaTeste.getId(), resultado.get().id());
            verify(disciplinaRepository).findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId());
        }
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar disciplina que não pertence ao utilizador")
    void deveRetornarVazioParaDisciplinaDeOutroUsuario() {
        // Arrange
        Pageable pageableMock = Pageable.unpaged();

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
            // Simula que a busca segura FALHOU (não encontrou / não pertence)
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaTeste.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty());

            // --- CORREÇÃO DA CHAMADA DO SERVIÇO ---
            // Passamos os 2 argumentos que o serviço agora espera.
            // Act
            Optional<DetalheDisciplinaResponse> resultado = service.executar(disciplinaTeste.getId(), pageableMock);
            // --- FIM DA CORREÇÃO ---

            // Assert
            assertFalse(resultado.isPresent(), "O Optional deveria estar vazio.");
            // Garante que não tentamos carregar os "filhos" (resumos/materiais) desnecessariamente
            verify(resumoRepository, never()).buscarPorDisciplinaEUsuario(any(), any());
            verify(materialRepository, never()).buscarPorDisciplinaDeFormaPaginada(any(), any());
        }
    }
}