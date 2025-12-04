package com.pdfocus.application.dashboard.service;

import com.pdfocus.application.dashboard.dto.DashboardEstatisticasResponse;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Teste unitário para DefaultBuscarEstatisticasDashboardService.
 * Foco: Validar a lógica de agregação de dados de múltiplos repositórios
 * para um usuário autenticado, garantindo que o cálculo das estatísticas está correto.
 */
@ExtendWith(MockitoExtension.class)
class DefaultBuscarEstatisticasDashboardServiceTest {

    // Mocks (simulações) das nossas portas de saída.
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private ResumoRepository resumoRepository;
    @Mock
    private MaterialRepository materialRepository;

    // Instância real do serviço sob teste.
    @InjectMocks
    private DefaultBuscarEstatisticasDashboardService service;

    private Usuario usuarioTeste;
    private final String EMAIL_TESTE = "estatisticas@email.com";

    @BeforeEach
    void setUp() {
        // CORREÇÃO: Instanciamos o usuário com valores fixos, pois 'usuarioEntity' não existe neste contexto.
        // O construtor deve bater com a sua classe Usuario atualizada (com campos de resumo e feedback)
        usuarioTeste = new Usuario(
                UUID.randomUUID(),      // ID
                "Usuario Stats",        // Nome
                EMAIL_TESTE,            // Email
                "hash",                 // Senha
                true,                   // Ativo
                0,                      // Resumos Hoje
                LocalDate.now(),        // Data Último Uso (Resumo)
                0,                      // Feedbacks Hoje (NOVO)
                LocalDate.now()         // Data Último Feedback (NOVO)
        );
    }

    @Test
    @DisplayName("Deve calcular e retornar as estatísticas corretas para o usuário logado")
    void deveRetornarEstatisticasCorretas() {
        // Arrange (Preparação do cenário da "auditoria")
        // Simulamos o SecurityContextHolder para definir quem está "logado".
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);

            when(userDetailsMock.getUsername()).thenReturn(EMAIL_TESTE);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensinamos aos nossos "relatórios falsos" (Mocks) quais números eles devem retornar.
            when(usuarioRepository.buscarPorEmail(EMAIL_TESTE)).thenReturn(Optional.of(usuarioTeste));
            when(disciplinaRepository.countByUsuario(usuarioTeste)).thenReturn(5L); // L para indicar que é um 'long'
            when(resumoRepository.countByUsuario(usuarioTeste)).thenReturn(10L);
            when(materialRepository.countByUsuario(usuarioTeste)).thenReturn(15L);

            // Act (Ação - pedimos o relatório final)
            DashboardEstatisticasResponse resultado = service.executar();

            // Assert (Verificação - o auditor confere os números)
            assertNotNull(resultado);
            assertEquals(5L, resultado.totalDisciplinas(), "O total de disciplinas deveria ser 5.");
            assertEquals(10L, resultado.resumosCriados(), "O total de resumos criados deveria ser 10.");
            assertEquals(15L, resultado.totalMateriais(), "O total de materiais deveria ser 15.");

            // Verifica se o nosso "contador" realmente pediu os relatórios para cada departamento.
            verify(disciplinaRepository, times(1)).countByUsuario(usuarioTeste);
            verify(resumoRepository, times(1)).countByUsuario(usuarioTeste);
            verify(materialRepository, times(1)).countByUsuario(usuarioTeste);
        }
    }
}