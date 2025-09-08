package com.pdfocus.application.material.dto;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) para representar um item na lista de materiais recentes.
 * Esta é a nossa "ficha de catálogo", contendo apenas os dados necessários para a
 * exibição no dashboard.
 */
public record MaterialRecenteResponse(
        UUID id,
        String nome,
        String nomeDisciplina,
        String dataUploadFormatada,
        String tamanhoFormatado
) {
    // Um formatador de data para manter o padrão em um só lugar.
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Método de fábrica para converter um objeto de domínio Material para este DTO.
     *
     * @param material O objeto de domínio a ser convertido.
     * @return uma nova instância de MaterialRecenteResponse.
     */
    public static MaterialRecenteResponse fromDomain(Material material, Disciplina disciplina) {

        String nomeDisciplina = disciplina != null ? disciplina.getNome() : "Sem disciplina";
        String dataFormatada = material.getDataUpload() != null ? material.getDataUpload().format(FORMATADOR_DATA) : "-";
        String tamanhoFormatado = formatarTamanho(material.getTamanho());

        return new MaterialRecenteResponse(
                material.getId(),
                material.getNomeOriginal(),
                nomeDisciplina,
                dataFormatada,
                tamanhoFormatado
        );
    }

    /**
     * Método auxiliar privado para formatar o tamanho do arquivo de bytes para uma
     * string legível (KB, MB, etc.).
     */
    private static String formatarTamanho(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}