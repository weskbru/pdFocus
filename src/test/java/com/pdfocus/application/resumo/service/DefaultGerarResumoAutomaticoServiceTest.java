package com.pdfocus.application.resumo.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.exceptions.TextoNaoPodeSerExtraidoException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Resumo;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultGerarResumoAutomaticoServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @Mock
    private TextExtractorPort textExtractorPort;

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
                null, // título vazio para teste automático
                null  // conteúdo vazio para teste automático
        );
    }

    @Test
    void deveGerarResumoAutomaticoComTextoExtraidoDoPDF() {
        // Given
        String textoExtraido = "Este é o texto extraído do PDF com conteúdo relevante para o resumo.";

        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.buscarPorId(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(material.getNomeStorage()))
                .thenReturn(textoExtraido);

        // When
        Resumo resumo = service.executar(command, usuarioId);

        // Then
        assertNotNull(resumo);
        assertEquals("Resumo - documento.pdf", resumo.getTitulo());
        assertEquals(textoExtraido, resumo.getConteudo());
        assertEquals(disciplina, resumo.getDisciplina());
        assertEquals(materialId, resumo.getMaterialId());
        assertEquals(usuarioId, resumo.getUsuarioId());

        verify(materialRepository).buscarPorIdEUsuario(materialId, usuarioId);
        verify(disciplinaRepository).buscarPorId(disciplinaId);
        verify(textExtractorPort).extrairTexto(material.getNomeStorage());
    }

    @Test
    void deveUsarTituloCustomizadoQuandoFornecido() {

        String textoExtraido = "Texto do PDF";
        String tituloCustomizado = "Meu Resumo Personalizado";

        CriarResumoDeMaterialCommand commandComTitulo = new CriarResumoDeMaterialCommand(
                materialId, disciplinaId, tituloCustomizado, null
        );

        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.buscarPorId(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(any()))
                .thenReturn(textoExtraido);

        // When
        Resumo resumo = service.executar(commandComTitulo, usuarioId);

        // Then
        assertEquals(tituloCustomizado, resumo.getTitulo());
    }

    @Test
    void deveUsarConteudoCustomizadoQuandoFornecido() {
        // Given
        String conteudoCustomizado = "Conteúdo personalizado pelo usuário";

        CriarResumoDeMaterialCommand commandComConteudo = new CriarResumoDeMaterialCommand(
                materialId, disciplinaId, null, conteudoCustomizado
        );

        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.buscarPorId(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        // NOTA: textExtractorPort NÃO deve ser chamado quando conteúdo é fornecido

        // When
        Resumo resumo = service.executar(commandComConteudo, usuarioId);

        // Then
        assertEquals(conteudoCustomizado, resumo.getConteudo());
        verify(textExtractorPort, never()).extrairTexto(any());
    }

    @Test
    void deveLancarExcecaoQuandoMaterialNaoForEncontrado() {
        // Given
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.empty());

        // When & Then
        MaterialNaoEncontradoException exception = assertThrows(
                MaterialNaoEncontradoException.class,
                () -> service.executar(command, usuarioId)
        );

        assertTrue(exception.getMessage().contains(materialId.toString()));
        verify(disciplinaRepository, never()).buscarPorId(any());
        verify(textExtractorPort, never()).extrairTexto(any());
    }

    @Test
    void deveLancarExcecaoQuandoDisciplinaNaoForEncontrada() {
        // Given
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.buscarPorId(disciplinaId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                DisciplinaNaoEncontradaException.class,
                () -> service.executar(command, usuarioId)
        );

        verify(textExtractorPort, never()).extrairTexto(any());
    }

    @Test
    void deveLancarExcecaoQuandoErroNaExtracaoDeTexto() {
        // Given
        when(materialRepository.buscarPorIdEUsuario(materialId, usuarioId))
                .thenReturn(Optional.of(material));
        when(disciplinaRepository.buscarPorId(disciplinaId))
                .thenReturn(Optional.of(disciplina));
        when(textExtractorPort.extrairTexto(material.getNomeStorage()))
                .thenThrow(new TextoNaoPodeSerExtraidoException("PDF corrompido"));

        // When & Then
        assertThrows(
                TextoNaoPodeSerExtraidoException.class,
                () -> service.executar(command, usuarioId)
        );
    }
}