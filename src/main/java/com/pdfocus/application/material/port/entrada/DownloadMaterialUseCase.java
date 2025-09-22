package com.pdfocus.application.material.port.entrada;

import com.pdfocus.core.models.Material;
import org.springframework.core.io.Resource;

import java.util.UUID;

/**
 * Define o contrato (Porta de Entrada) para o caso de uso de download
 * de um arquivo de material, garantindo que ele pertença ao
 * usuário autenticado.
 */
public interface DownloadMaterialUseCase {

    /**
     * DTO interno para agrupar o resultado da operação de download,
     * contendo o recurso (o arquivo) e os seus metadados.
     */
    record DownloadResult(Resource resource, Material material) {}

    /**
     * Executa a lógica de negócio para localizar e preparar um material para download.
     * A segurança é garantida pela implementação, que filtra pelo usuário logado.
     *
     * @param id O UUID do material a ser baixado.
     * @return um objeto {@link DownloadResult} contendo o arquivo e seus metadados.
     */
    DownloadResult executar(UUID id);
}