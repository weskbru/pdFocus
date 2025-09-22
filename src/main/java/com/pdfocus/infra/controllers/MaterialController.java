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
 * Controller REST para gerir as operações relacionadas a Materiais de estudo.
 * A lógica de segurança e identificação do utilizador é delegada para a camada de serviço.
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
     * Endpoint para listar todos os materiais de uma disciplina específica do utilizador autenticado.
     * @param disciplinaId O ID da disciplina cujos materiais serão listados (parâmetro de query).
     * @return Uma lista de materiais.
     */
    @GetMapping
    public ResponseEntity<List<Material>> listarPorDisciplina(@RequestParam UUID disciplinaId) {
        // A lógica de descobrir o utilizador foi movida para o serviço.
        List<Material> materiais = listarMateriaisUseCase.executar(disciplinaId);
        return ResponseEntity.ok(materiais);
    }

    /**
     * Endpoint para fazer o upload de um novo material.
     * Recebe um ficheiro e o ID da disciplina como 'multipart/form-data'.
     *
     * @param ficheiro O ficheiro enviado pelo cliente.
     * @param disciplinaId O UUID da disciplina à qual o material será associado.
     * @return Resposta 201 (Created) com os metadados do material criado.
     * @throws IOException se ocorrer um erro ao ler o conteúdo do ficheiro.
     */
    @PostMapping
    public ResponseEntity<Material> uploadMaterial(
            @RequestParam("arquivo") MultipartFile ficheiro,
            @RequestParam("disciplinaId") UUID disciplinaId) throws IOException {

        // Cria o DTO de comando com os dados da requisição.
        UploadMaterialCommand command = new UploadMaterialCommand(
                ficheiro.getOriginalFilename(),
                ficheiro.getContentType(),
                ficheiro.getSize(),
                disciplinaId,
                ficheiro.getInputStream()
        );

        // Delega a execução para o caso de uso, que agora lida com a lógica de segurança.
        Material novoMaterial = uploadMaterialUseCase.executar(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/materiais/{id}")
                .buildAndExpand(novoMaterial.getId())
                .toUri();

        // No futuro, podemos criar um DTO de resposta para o Material.
        return ResponseEntity.created(location).body(novoMaterial);
    }

    /**
     * Endpoint para apagar um material existente do utilizador autenticado.
     * @param id O UUID do material a ser apagado.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        // A lógica de segurança para verificar a posse do material foi movida para o serviço.
        deletarMaterialUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ADICIONE ESTE NOVO MÉTODO:
     * Endpoint para fazer o download de um arquivo de material.
     * A segurança (verificação de posse) é garantida pela camada de serviço.
     *
     * @param id O UUID do material a ser baixado.
     * @return ResponseEntity contendo o arquivo como um recurso (Resource).
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable UUID id) {
        // 1. Chama o caso de uso para obter o arquivo e seus metadados.
        DownloadMaterialUseCase.DownloadResult result = downloadMaterialUseCase.executar(id);
        Resource resource = result.resource();
        Material material = result.material();

        // 2. Define o tipo de conteúdo (MIME type) da resposta.
        String contentType = material.getTipoArquivo();
        if (contentType == null) {
            contentType = "application/octet-stream"; // Tipo genérico
        }

        // 3. Constrói a resposta HTTP com os cabeçalhos corretos.
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                // Este cabeçalho força o navegador a abrir a caixa de diálogo "Salvar Como...".
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getNomeOriginal() + "\"")
                .body(resource);
    }
}

