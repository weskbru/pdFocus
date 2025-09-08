package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultCriarDisciplinaService}.
 * Foco: Validar a lógica de negócio, incluindo a associação correta da
 * disciplina ao utilizador autenticado.
 */
@ExtendWith(MockitoExtension.class)
class DefaultCriarDisciplinaServiceTest {

    // Mocks das dependências para isolar o serviço.
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    // Instância real do serviço sob teste.
    @InjectMocks
    private DefaultCriarDisciplinaService service;

    private Usuario usuarioTeste;
    private final String NOME_DISCIPLINA_VALIDO = "Matemática Avançada";
    private final String DESCRICAO_DISCIPLINA_VALIDA = "Estudo de tópicos avançados em matemática.";

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Create", "create@email.com", "hash");
    }

    @Test
    @DisplayName("Deve criar uma disciplina associada ao utilizador logado com sucesso")
    void deveCriarDisciplinaComSucesso() {
        // Arrange
        CriarDisciplinaCommand command = new CriarDisciplinaCommand(NOME_DISCIPLINA_VALIDO, DESCRICAO_DISCIPLINA_VALIDA);

        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Simula o utilizador autenticado no sistema
            UserDetails userDetailsMock = mock(UserDetails.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            Authentication authenticationMock = mock(Authentication.class);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensina aos mocks como se comportar
            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula a ação de salvar, retornando o objeto que foi passado para ele
            when(disciplinaRepository.salvar(any(Disciplina.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            // A chamada ao serviço agora usa a nova assinatura segura, sem o usuarioId.
            Disciplina disciplinaCriada = service.executar(command);

            // Assert
            assertNotNull(disciplinaCriada);
            assertEquals(NOME_DISCIPLINA_VALIDO, disciplinaCriada.getNome());
            // A asserção mais importante: garante que a disciplina foi criada para o utilizador correto.
            assertEquals(usuarioTeste.getId(), disciplinaCriada.getUsuarioId());

            verify(disciplinaRepository, times(1)).salvar(any(Disciplina.class));
            verify(usuarioRepository, times(1)).buscarPorEmail(usuarioTeste.getEmail());
        }
    }
}

