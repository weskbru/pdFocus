package com.pdfocus.infra.controllers;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.DeletarMaterialUseCase;
import com.pdfocus.application.material.port.entrada.ListarMateriaisUseCase;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.core.models.Material;
import com.pdfocus.infra.security.AuthenticationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Importante para uploads
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gerir as operações relacionadas a Materiais de estudo.
 */
@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final UploadMaterialUseCase uploadMaterialUseCase;
    private final ListarMateriaisUseCase listarMateriaisUseCase;
    private final DeletarMaterialUseCase deletarMaterialUseCase;
    private final AuthenticationHelper authenticationHelper;

    public MaterialController(
            UploadMaterialUseCase uploadMaterialUseCase,
            ListarMateriaisUseCase listarMateriaisUseCase,
            DeletarMaterialUseCase deletarMaterialUseCase, // 3. Injetar no construtor
            AuthenticationHelper authenticationHelper) {
        this.uploadMaterialUseCase = uploadMaterialUseCase;
        this.listarMateriaisUseCase = listarMateriaisUseCase;
        this.deletarMaterialUseCase = deletarMaterialUseCase;
        this.authenticationHelper = authenticationHelper;
    }

    /**
     * Endpoint para listar todos os materiais de uma disciplina específica do utilizador autenticado.
     * Responde a requisições GET em /materiais?disciplinaId={id-da-disciplina}
     *
     * @param disciplinaId O ID da disciplina cujos materiais serão listados.
     * @return Uma lista de materiais pertencentes à disciplina e ao utilizador logado.
     */
    @GetMapping
    public ResponseEntity<List<Material>> listarPorDisciplina(@RequestParam UUID disciplinaId) {
        // Obtém o ID do utilizador autenticado
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();

        // Chama o caso de uso para obter a lista de materiais
        List<Material> materiais = listarMateriaisUseCase.executar(disciplinaId, usuarioId);

        // Retorna a lista com um status HTTP 200 OK
        return ResponseEntity.ok(materiais);
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

    /**
     * Endpoint para apagar um material existente do utilizador autenticado.
     * Responde a requisições DELETE em /materiais/{id}.
     *
     * @param id O UUID do material a ser apagado.
     * @return Resposta 204 (No Content) se a deleção for bem-sucedida.
     * O RestExceptionHandler tratará do caso 404 se o material não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        // Obtém o ID do utilizador autenticado a partir do token
        UUID usuarioId = authenticationHelper.getUsuarioAutenticado().getId();

        // Chama o caso de uso para executar a deleção
        deletarMaterialUseCase.executar(id, usuarioId);

        // Se o metodo chegar até aqui, a deleção foi um sucesso.
        // Se o material não for encontrado, uma exceção será lançada e o
        // RestExceptionHandler cuidará de retornar o 404 Not Found.
        return ResponseEntity.noContent().build();
    }
}
