package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.core.models.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultListarMateriaisService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultListarMateriaisService")
public class DefaultListarMateriaisServiceTest {

    @Mock
    private MaterialRepository materialRepositoryMock;

    @InjectMocks
    private DefaultListarMateriaisService service;

    private final UUID ID_USUARIO = UUID.randomUUID();
    private final UUID ID_DISCIPLINA = UUID.randomUUID();

    /**
     * Agrupa os testes para os cenários principais de busca.
     */
    @Nested
    @DisplayName("Cenários de Busca")
    class CenariosDeBusca {

        @Test
        @DisplayName("Deve retornar a lista de materiais quando o repositório os encontra")
        void deveRetornarListaQuandoRepositorioEncontra() {
            // --- Arrange (Preparação) ---
            // 1. Criamos uma lista de resultado falsa para simular o retorno do repositório.
            List<Material> materiaisEsperados = Collections.singletonList(mock(Material.class));

            // 2. Ensinamos o nosso repositório falso (o mock) a se comportar:
            // "QUANDO o método 'listarPorDisciplinaEUsuario' for chamado, ENTÃO retorne a lista 'materiaisEsperados'."
            when(materialRepositoryMock.listarPorDisciplinaEUsuario(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(materiaisEsperados);

            // --- Act (Ação) ---
            // 3. Executamos o método que queremos testar.
            List<Material> resultado = service.executar(ID_DISCIPLINA, ID_USUARIO);

            // --- Assert (Verificação) ---
            // 4. Verificamos se o resultado é o que esperávamos.
            assertNotNull(resultado);
            assertEquals(materiaisEsperados, resultado);

            // 5. Verificamos se o serviço realmente chamou o repositório da forma correta.
            verify(materialRepositoryMock).listarPorDisciplinaEUsuario(ID_DISCIPLINA, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando o repositório não encontra materiais")
        void deveRetornarListaVazia() {
            // Arrange: Ensinamos o repositório a retornar uma lista vazia.
            when(materialRepositoryMock.listarPorDisciplinaEUsuario(ID_DISCIPLINA, ID_USUARIO))
                    .thenReturn(Collections.emptyList());

            // Act: Executamos o serviço.
            List<Material> resultado = service.executar(ID_DISCIPLINA, ID_USUARIO);

            // Assert: Verificamos se o resultado é de fato uma lista vazia.
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(materialRepositoryMock).listarPorDisciplinaEUsuario(ID_DISCIPLINA, ID_USUARIO);
        }
    }

    /**
     * Agrupa os testes para as validações de entrada do serviço.
     */
    @Nested
    @DisplayName("Validação de Entradas")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Deve lançar exceção quando o ID da disciplina for nulo")
        void deveLancarExcecaoQuandoDisciplinaIdForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, ID_USUARIO);
            });
            verifyNoInteractions(materialRepositoryMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoUsuarioIdForNulo() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                service.executar(ID_DISCIPLINA, null);
            });
            verifyNoInteractions(materialRepositoryMock);
        }
    }

}
