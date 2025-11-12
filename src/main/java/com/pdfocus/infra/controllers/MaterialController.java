package com.pdfocus.infra.controllers;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.DeletarMaterialUseCase;
import com.pdfocus.application.material.port.entrada.DownloadMaterialUseCase;
import com.pdfocus.application.material.port.entrada.ListarMateriaisUseCase;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.core.models.Material;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST responsável pelas operações de materiais de estudo.
 * <p>
 * Atua como camada de orquestração entre o frontend e os casos de uso
 * de upload, listagem, download e exclusão de arquivos. Toda a lógica
 * de segurança (validação do usuário proprietário) é tratada na camada
 * de aplicação.
 * </p>
 */
@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final UploadMaterialUseCase uploadMaterialUseCase;
    private final ListarMateriaisUseCase listarMateriaisUseCase;
    private final DeletarMaterialUseCase deletarMaterialUseCase;
    private final DownloadMaterialUseCase downloadMaterialUseCase;

    public MaterialController(
            UploadMaterialUseCase uploadMaterialUseCase,
            ListarMateriaisUseCase listarMateriaisUseCase,
            DeletarMaterialUseCase deletarMaterialUseCase,
            DownloadMaterialUseCase downloadMaterialUseCase) {
        this.uploadMaterialUseCase = uploadMaterialUseCase;
        this.listarMateriaisUseCase = listarMateriaisUseCase;
        this.deletarMaterialUseCase = deletarMaterialUseCase;
        this.downloadMaterialUseCase = downloadMaterialUseCase;
    }

    /**
     * Lista todos os materiais pertencentes a uma disciplina do usuário autenticado.
     *
     * @param disciplinaId UUID da disciplina cujos materiais serão listados.
     * @return 200 (OK) com lista de {@link Material}.
     */
    @GetMapping
    public ResponseEntity<List<Material>> listarPorDisciplina(@RequestParam UUID disciplinaId) {
        List<Material> materiais = listarMateriaisUseCase.executar(disciplinaId);
        return ResponseEntity.ok(materiais);
    }

    /**
     * Realiza o upload de um novo material (arquivo PDF, DOCX, etc.).
     * <p>
     * O arquivo é recebido como multipart e os metadados são encapsulados
     * em um {@link UploadMaterialCommand} para o caso de uso responsável.
     * </p>
     *
     * @param arquivo Arquivo enviado pelo cliente.
     * @param disciplinaId UUID da disciplina associada ao material.
     * @return 201 (Created) com o material criado e o cabeçalho Location apontando para o recurso.
     * @throws IOException Se ocorrer erro ao ler o conteúdo do arquivo.
     */
    @PostMapping
    public ResponseEntity<Material> uploadMaterial(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("disciplinaId") UUID disciplinaId) throws IOException {

        UploadMaterialCommand command = new UploadMaterialCommand(
                arquivo.getOriginalFilename(),
                arquivo.getContentType(),
                arquivo.getSize(),
                disciplinaId,
                arquivo.getInputStream()
        );

        Material novoMaterial = uploadMaterialUseCase.executar(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/materiais/{id}")
                .buildAndExpand(novoMaterial.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoMaterial);
    }

    /**
     * Exclui um material existente, garantindo que ele pertença ao usuário autenticado.
     *
     * @param id UUID do material a ser removido.
     * @return 204 (No Content) se a exclusão for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarMaterialUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Permite o download de um material previamente enviado.
     * <p>
     * O arquivo é retornado como {@link Resource} com cabeçalho
     * "Content-Disposition: attachment" para forçar o download.
     * </p>
     *
     * @param id UUID do material a ser baixado.
     * @return 200 (OK) com o arquivo binário.
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable UUID id) {
        DownloadMaterialUseCase.DownloadResult result = downloadMaterialUseCase.executar(id);
        Resource resource = result.resource();
        Material material = result.material();

        String contentType = material.getTipoArquivo() != null
                ? material.getTipoArquivo()
                : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + material.getNomeOriginal() + "\"")
                .body(resource);
    }

    /**
     * Exibe o arquivo diretamente no navegador (modo inline).
     * <p>
     * Ideal para PDFs e imagens, onde o usuário pode visualizar
     * o conteúdo sem precisar baixar explicitamente.
     * </p>
     *
     * @param id UUID do material a ser visualizado.
     * @return 200 (OK) com o arquivo exibível no corpo da resposta.
     */
    @GetMapping("/{id}/visualizar")
    public ResponseEntity<Resource> visualizarMaterial(@PathVariable UUID id) {
        DownloadMaterialUseCase.DownloadResult result = downloadMaterialUseCase.executar(id);
        Resource resource = result.resource();
        Material material = result.material();

        String contentType = material.getTipoArquivo() != null
                ? material.getTipoArquivo()
                : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + material.getNomeOriginal() + "\"")
                .body(resource);
    }
}
