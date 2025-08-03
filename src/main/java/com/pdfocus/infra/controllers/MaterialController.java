package com.pdfocus.infra.controllers;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.core.models.Material;
import com.pdfocus.infra.security.AuthenticationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Importante para uploads
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

/**
 * Controller REST para gerir as operações relacionadas a Materiais de estudo.
 */
@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final UploadMaterialUseCase uploadMaterialUseCase;
    private final AuthenticationHelper authenticationHelper;

    public MaterialController(UploadMaterialUseCase uploadMaterialUseCase, AuthenticationHelper authenticationHelper) {
        this.uploadMaterialUseCase = uploadMaterialUseCase;
        this.authenticationHelper = authenticationHelper;
    }

    /**
     * Endpoint para fazer o upload de um novo material e associá-lo a uma disciplina.
     * Responde a requisições POST em /materiais/{disciplinaId}/upload
     *
     * @param disciplinaId O ID da disciplina à qual o material será associado.
     * @param ficheiro O ficheiro enviado na requisição (multipart/form-data).
     * @return Resposta 201 (Created) com os metadados do novo material.
     * @throws IOException se ocorrer um erro ao ler o conteúdo do ficheiro.
     */
    @PostMapping("/{disciplinaId}/upload")
    public ResponseEntity<Material> uploadMaterial(
            @PathVariable UUID disciplinaId,
            @RequestParam("ficheiro") MultipartFile ficheiro) throws IOException {

        // 1. Obtém o ID do usuário autenticado a partir do token.
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();

        // 2. Cria o DTO de comando com os dados do ficheiro recebido.
        UploadMaterialCommand command = new UploadMaterialCommand(
                ficheiro.getOriginalFilename(),
                ficheiro.getContentType(),
                ficheiro.getSize(),
                disciplinaId,
                ficheiro.getInputStream()
        );

        // 3. Executa o caso de uso.
        Material novoMaterial = uploadMaterialUseCase.executar(command, usuarioId);

        // 4. Constrói a URI para o novo recurso (opcional, mas boa prática).
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/materiais/{id}") // Supondo um futuro endpoint para obter um material por ID
                .buildAndExpand(novoMaterial.getId())
                .toUri();

        // 5. Retorna a resposta 201 Created.
        return ResponseEntity.created(location).body(novoMaterial);
    }
}
