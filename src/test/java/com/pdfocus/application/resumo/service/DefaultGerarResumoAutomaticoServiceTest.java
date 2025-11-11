package com.pdfocus.application.resumo.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
// --- 1. IMPORT NECESSÁRIO ---
import com.pdfocus.application.resumo.port.saida.ResumidorIAPort;
// --- FIM DA CORREÇÃO ---
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException;
import com.pdfocus.core.exceptions.resumo.TextoNaoPodeSerExtraidoException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Resumo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt; // <-- Import para o mock da IA
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes para DefaultGerarResumoAutomaticoService.
 * (Pilar 4) - Corrigido para "zombar" (mockar) ResumoRepository e ResumidorIAPort.
 */
@ExtendWith(MockitoExtension.class)
public class DefaultGerarResumoAutomaticoServiceTest {

    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private TextExtractorPort textExtractorPort;
    @Mock
    private ResumoRepository resumoRepository;

    // --- 2. ADICIONAR O MOCK QUE FALTAVA ---
    @Mock
    private ResumidorIAPort resumidorIAPort;
    // --- FIM DA CORREÇÃO ---

    @InjectMocks
    private DefaultGerarResumoAutomaticoService service;

    private UUID usuarioId;
    private UUID materialId;
    private UUID disciplinaId;
    private Material material;
    private Disciplina disciplina;
    private CriarResumoDeMaterialCommand command;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        materialId = UUID.randomUUID();
        disciplinaId = UUID.randomUUID();

        material = Material.criar(
                materialId,
                "documento.pdf",
                "uuid-arquivo.pdf",
                "application/pdf",
                1024L,
                usuarioId,
                disciplinaId,
                OffsetDateTime.now()
        );

        disciplina = new Disciplina(disciplinaId, "Matemática", "Descrição da disciplina", usuarioId);

        command = new CriarResumoDeMaterialCommand(
                materialId,
                disciplinaId,
                null,
                null
        );
    }

    private void mockSalvarResumo() {
        when(resumoRepository.salvar(any(Resumo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Deve gerar resumo automático com texto extraído do PDF")
    void deveGerarResumoAutomaticoComTextoExtraidoDoPDF() {
        // Given
        String textoExtraido = "Este é o texto extraído do PDF com conteúdo relevante para o resumo.";
        String resumoDaIA = "Resumo gerado pela IA.";

        // Usamos o nome correto do MaterialRepository (que não é o mesmo do DisciplinaRepository)
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));

        when(disciplinaRepository.findById(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(anyString()))
                .thenReturn(textoExtraido);

        // --- 3. "ENSINAR" O MOCK DA IA ---
        when(resumidorIAPort.resumir(anyString(), anyInt()))
                .thenReturn(resumoDaIA);
        // --- FIM DA CORREÇÃO ---

        mockSalvarResumo();

        // When
        Resumo resumo = service.executar(command, usuarioId); // Linha 107 (no log)

        // Then
        assertNotNull(resumo);
        assertEquals("Resumo - documento.pdf", resumo.getTitulo());
        assertEquals(resumoDaIA, resumo.getConteudo()); // Deve ser o conteúdo da IA
        verify(textExtractorPort).extrairTexto(material.getNomeStorage());
        verify(resumidorIAPort).resumir(textoExtraido, 300); // Verifica se a IA foi chamada
        verify(resumoRepository).salvar(any(Resumo.class));
    }

    @Test
    @DisplayName("Deve usar título customizado quando fornecido")
    void deveUsarTituloCustomizadoQuandoFornecido() {
        // Given
        String textoExtraido = "Texto do PDF";
        String resumoDaIA = "Resumo gerado pela IA.";
        String tituloCustomizado = "Meu Resumo Personalizado";

        CriarResumoDeMaterialCommand commandComTitulo = new CriarResumoDeMaterialCommand(
                materialId, disciplinaId, tituloCustomizado, null
        );

        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));

        when(disciplinaRepository.findById(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(anyString()))
                .thenReturn(textoExtraido);

        // --- 3. "ENSINAR" O MOCK DA IA ---
        when(resumidorIAPort.resumir(anyString(), anyInt()))
                .thenReturn(resumoDaIA);
        // --- FIM DA CORREÇÃO ---

        mockSalvarResumo();

        // When
        Resumo resumo = service.executar(commandComTitulo, usuarioId); // Linha 140 (no log)

        // Then
        assertEquals(tituloCustomizado, resumo.getTitulo());
        assertEquals(resumoDaIA, resumo.getConteudo());
        verify(resumoRepository).salvar(any(Resumo.class));
    }

    // (Testes que não falharam, mas que precisam dos nomes corretos)

    @Test
    @DisplayName("Deve usar conteúdo customizado quando fornecido")
    void deveUsarConteudoCustomizadoQuandoFornecido() {
        String conteudoCustomizado = "Conteúdo personalizado pelo usuário";
        CriarResumoDeMaterialCommand commandComConteudo = new CriarResumoDeMaterialCommand(
                materialId, disciplinaId, null, conteudoCustomizado
        );
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.findById(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        mockSalvarResumo();

        Resumo resumo = service.executar(commandComConteudo, usuarioId);

        assertEquals(conteudoCustomizado, resumo.getConteudo());
        verify(textExtractorPort, never()).extrairTexto(any());
        verify(resumidorIAPort, never()).resumir(anyString(), anyInt());
        verify(resumoRepository).salvar(any(Resumo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando material não for encontrado")
    void deveLancarExcecaoQuandoMaterialNaoForEncontrado() {
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(
                MaterialNaoEncontradoException.class,
                () -> service.executar(command, usuarioId)
        );
        verify(resumoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando disciplina não for encontrada")
    void deveLancarExcecaoQuandoDisciplinaNaoForEncontrada() {
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.findById(disciplinaId))
                .thenReturn(Optional.empty());

        assertThrows(
                DisciplinaNaoEncontradaException.class,
                () -> service.executar(command, usuarioId)
        );
        verify(resumoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando erro na extração de texto")
    void deveLancarExcecaoQuandoErroNaExtracaoDeTexto() {
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.findById(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(anyString()))
                .thenThrow(new TextoNaoPodeSerExtraidoException("PDF corrompido"));

        assertThrows(
                TextoNaoPodeSerExtraidoException.class,
                () -> service.executar(command, usuarioId)
        );
        verify(resumoRepository, never()).salvar(any());
    }
}