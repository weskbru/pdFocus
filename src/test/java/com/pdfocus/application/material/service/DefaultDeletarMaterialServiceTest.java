package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link DefaultDeletarMaterialService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DefaultDeletarMaterialService")
public class DefaultDeletarMaterialServiceTest {

    @Mock
    private MaterialRepository materialRepositoryMock;

    @Mock
    private MaterialStoragePort materialStoragePortMock;

    @InjectMocks
    private DefaultDeletarMaterialService service;

    private final UUID ID_MATERIAL = UUID.randomUUID();
    private final UUID ID_USUARIO = UUID.randomUUID();
    private final String NOME_STORAGE = "abc-123.pdf";

    /**
     * Agrupa os testes para os cenários principais de deleção.
     */
    @Nested
    @DisplayName("Cenários de Deleção")
    class CenariosDeDelecao {

        @Test
        @DisplayName("Deve apagar o ficheiro físico e depois o registo no banco quando o material existe")
        void deveApagarFicheiroERegistoQuandoMaterialExiste() {
            // --- Arrange (Preparação) ---
            // 1. Criamos um "dublê" de Material que será retornado pela busca.
            Material materialEncontrado = mock(Material.class);

            // 2. Ensinamos o dublê a se comportar: quando o método getNomeStorage() for chamado,
            // ele deve retornar o nosso nome de ficheiro de exemplo.
            when(materialEncontrado.getNomeStorage()).thenReturn(NOME_STORAGE);

            // 3. Ensinamos o repositório a encontrar este material.
            when(materialRepositoryMock.buscarPorIdEUsuario(ID_MATERIAL, ID_USUARIO))
                    .thenReturn(Optional.of(materialEncontrado));

            // --- Act (Ação) ---
            // 4. Executamos o método que queremos testar.
            assertDoesNotThrow(() -> {
                service.executar(ID_MATERIAL, ID_USUARIO);
            });

            // --- Assert (Verificação) ---
            // 5. Verificamos se a ordem das operações está correta.
            // InOrder garante que o 'apagar' do storage foi chamado ANTES do 'deletarPorIdEUsuario' do repositório.
            InOrder inOrder = inOrder(materialStoragePortMock, materialRepositoryMock);
            inOrder.verify(materialStoragePortMock).apagar(NOME_STORAGE);
            inOrder.verify(materialRepositoryMock).deletarPorIdEUsuario(ID_MATERIAL, ID_USUARIO);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o material não for encontrado")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // Arrange: Ensinamos o repositório a não encontrar o material.
            when(materialRepositoryMock.buscarPorIdEUsuario(ID_MATERIAL, ID_USUARIO))
                    .thenReturn(Optional.empty());

            // Act & Assert: Verificamos se a exceção correta é lançada.
            assertThrows(MaterialNaoEncontradoException.class, () -> {
                service.executar(ID_MATERIAL, ID_USUARIO);
            });

            // Verificamos que, se o material não foi encontrado, nenhuma operação de deleção foi tentada.
            verify(materialStoragePortMock, never()).apagar(anyString());
            verify(materialRepositoryMock, never()).deletarPorIdEUsuario(any(), any());
        }
    }

    /**
     * Agrupa os testes para as validações de entrada do serviço.
     */
    @Nested
    @DisplayName("Validação de Entradas")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Deve lançar exceção quando o ID do material for nulo")
        void deveLancarExcecaoQuandoIdMaterialForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(null, ID_USUARIO);
            });
            verifyNoInteractions(materialRepositoryMock, materialStoragePortMock);
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
        void deveLancarExcecaoQuandoIdUsuarioForNulo() {
            assertThrows(NullPointerException.class, () -> {
                service.executar(ID_MATERIAL, null);
            });
            verifyNoInteractions(materialRepositoryMock, materialStoragePortMock);
        }
    }
}
