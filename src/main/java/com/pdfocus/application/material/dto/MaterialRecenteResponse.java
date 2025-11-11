package com.pdfocus.application.material.dto;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) que representa um material exibido na seção
 * "Materiais Recentes" do dashboard do usuário.
 *
 * <p>
 * Atua como uma <b>projeção de leitura</b> (read model), otimizando o
 * transporte de dados entre a camada de aplicação e a camada de apresentação.
 * Contém apenas as informações necessárias para exibição — sem detalhes
 * internos de domínio.
 * </p>
 *
 * <p><b>Características:</b></p>
 * <ul>
 *   <li>Imutável e serializável por padrão (graças ao uso de {@code record}).</li>
 *   <li>Formata a data e o tamanho do arquivo para exibição legível.</li>
 *   <li>Evita dependências diretas de entidades de persistência.</li>
 * </ul>
 *
 * @param id Identificador único do material.
 * @param nome Nome original do arquivo de material.
 * @param nomeDisciplina Nome da disciplina associada (ou “Sem disciplina” se não houver).
 * @param dataUploadFormatada Data do upload formatada no padrão {@code dd/MM/yyyy}.
 * @param tamanhoFormatado Tamanho do arquivo formatado em KB, MB etc.
 */
public record MaterialRecenteResponse(
        UUID id,
        String nome,
        String nomeDisciplina,
        String dataUploadFormatada,
        String tamanhoFormatado
) {
    // Formatador centralizado para exibir datas no padrão brasileiro.
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Cria uma instância deste DTO a partir de um objeto de domínio {@link Material}.
     *
     * <p>Este método aplica formatação de data e tamanho, e extrai o nome da
     * disciplina caso ela esteja associada.</p>
     *
     * @param material O material de domínio a ser convertido.
     * @param disciplina A disciplina associada ao material (pode ser nula).
     * @return uma instância imutável de {@link MaterialRecenteResponse}.
     */
    public static MaterialRecenteResponse fromDomain(Material material, Disciplina disciplina) {
        String nomeDisciplina = disciplina != null ? disciplina.getNome() : "Sem disciplina";
        String dataFormatada = material.getDataUpload() != null
                ? material.getDataUpload().format(FORMATADOR_DATA)
                : "-";
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
     * Converte o tamanho de um arquivo em bytes para uma representação legível.
     *
     * <p>Exemplo: 1048576 bytes → {@code "1.0 MB"}</p>
     *
     * @param bytes o tamanho bruto em bytes.
     * @return uma string formatada com unidade apropriada.
     */
    private static String formatarTamanho(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
