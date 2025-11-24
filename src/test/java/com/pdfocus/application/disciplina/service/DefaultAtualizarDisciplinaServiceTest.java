package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
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
 * Testes unitários para a classe {@link DefaultAtualizarDisciplinaService}.
 * Foco: Validar a lógica de segurança que verifica a posse da disciplina
 * antes de executar a operação de atualização.
 */
@ExtendWith(MockitoExtension.class)
class DefaultAtualizarDisciplinaServiceTest {

    // Mocks das dependências para isolar o serviço.
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    // Instância real do serviço sob teste.
    @InjectMocks
    private DefaultAtualizarDisciplinaService service;

    private Usuario usuarioTeste;
    private Disciplina disciplinaExistente;
    private AtualizarDisciplinaCommand comandoAtualizacao;

    @BeforeEach
    void setUp() {
        usuarioTeste = new Usuario(UUID.randomUUID(), "Usuario Update", "update@email.com", "hash", usuarioEntity.getResumosHoje(), usuarioEntity.getDataUltimoUso());
        disciplinaExistente = new Disciplina(UUID.randomUUID(), "Nome Antigo", "Descrição Antiga", usuarioTeste.getId());
        comandoAtualizacao = new AtualizarDisciplinaCommand("Nome Novo", "Descrição Nova");
    }

    @Test
    @DisplayName("Deve atualizar a disciplina com sucesso se ela pertence ao usuário logado")
    void deveAtualizarDisciplinaComSucesso() {
        // Arrange (Preparação do cenário de sucesso)
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Simula o usuário autenticado no sistema.
            UserDetails userDetailsMock = mock(UserDetails.class);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            Authentication authenticationMock = mock(Authentication.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            // Ensina aos mocks como se comportar.
            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula que a verificação de posse FOI BEM-SUCEDIDA.
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaExistente.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.of(disciplinaExistente));
            // Simula a ação de salvar, retornando a própria entidade (comportamento comum de save).
            when(disciplinaRepository.salvar(any(Disciplina.class))).thenReturn(disciplinaExistente);


            // Act (Ação)
            Disciplina resultado = service.executar(disciplinaExistente.getId(), comandoAtualizacao);


            // Assert (Verificação)
            assertNotNull(resultado);
            // Verifica se os setters foram chamados e os valores foram atualizados.
            assertEquals("Nome Novo", resultado.getNome());
            assertEquals("Descrição Nova", resultado.getDescricao());

            // Verifica se as interações esperadas com os mocks aconteceram.
            verify(disciplinaRepository, times(1)).findByIdAndUsuarioId(disciplinaExistente.getId(), usuarioTeste.getId());
            verify(disciplinaRepository, times(1)).salvar(disciplinaExistente);
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar disciplina que não pertence ao usuário")
    void deveLancarExcecaoAoAtualizarDisciplinaDeOutroUsuario() {
        // Arrange (Preparação do cenário de FALHA de segurança)
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            // Configura o usuário logado...
            UserDetails userDetailsMock = mock(UserDetails.class);
            when(userDetailsMock.getUsername()).thenReturn(usuarioTeste.getEmail());
            Authentication authenticationMock = mock(Authentication.class);
            when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
            SecurityContext securityContextMock = mock(SecurityContext.class);
            when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            when(usuarioRepository.buscarPorEmail(usuarioTeste.getEmail())).thenReturn(Optional.of(usuarioTeste));
            // Simula que a verificação de posse FALHOU (repositório não encontrou a disciplina para este usuário)
            when(disciplinaRepository.findByIdAndUsuarioId(disciplinaExistente.getId(), usuarioTeste.getId()))
                    .thenReturn(Optional.empty());


            // Act & Assert
            // Verifica se o nosso serviço corretamente lança a exceção de segurança.
            assertThrows(DisciplinaNaoEncontradaException.class, () -> {
                service.executar(disciplinaExistente.getId(), comandoAtualizacao);
            });


            // A VERIFICAÇÃO MAIS IMPORTANTE:
            // Garante que o método para salvar NUNCA foi chamado se a verificação de posse falhou.
            verify(disciplinaRepository, never()).salvar(any(Disciplina.class));
        }
    }
}
