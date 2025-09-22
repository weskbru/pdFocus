package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
import com.pdfocus.core.exceptions.TextoNaoPodeSerExtraidoException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Adapter para extração de texto de PDFs usando a biblioteca PDFBox 3.0.5.
 * Implementa a porta {@link TextExtractorPort}.
 */
@Component
public class PdfBoxTextExtractorAdapter implements TextExtractorPort {

    private final MaterialStoragePort materialStoragePort;

    public PdfBoxTextExtractorAdapter(MaterialStoragePort materialStoragePort) {
        this.materialStoragePort = materialStoragePort;
    }

    @Override
    public String extrairTexto(String nomeStorage) {
        if (nomeStorage == null || nomeStorage.isBlank()) {
            throw new IllegalArgumentException("Nome do arquivo não pode ser nulo ou vazio");
        }

        try (InputStream inputStream = materialStoragePort.carregar(nomeStorage).getInputStream()) {

            // ler tudo para um byte[] para usar RandomAccessRead
            byte[] pdfBytes = toByteArray(inputStream);

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                if (document.isEncrypted()) {
                    throw new TextoNaoPodeSerExtraidoException("PDF está criptografado e não pode ser processado");
                }

                if (document.getPages().getCount() == 0) {
                    return "";
                }

                PDFTextStripper textStripper = new PDFTextStripper();
                textStripper.setSortByPosition(true);
                return textStripper.getText(document);
            }

        } catch (Exception e) {
            throw new TextoNaoPodeSerExtraidoException(
                    "Erro ao extrair texto do arquivo: " + nomeStorage + " - " + e.getMessage(),
                    e
            );
        }
    }

    private byte[] toByteArray(InputStream input) throws java.io.IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
