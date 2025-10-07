package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.DetalheDisciplinaResponse;
import com.pdfocus.application.disciplina.port.entrada.ObterDisciplinaPorIdUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do caso de uso para obter a visão detalhada de uma disciplina,
 * incluindo suas coleções associadas de resumos e materiais.
 */
@Service
public class DefaultObterDisciplinaPorIdService implements ObterDisciplinaPorIdUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final ResumoRepository resumoRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constrói o serviço com todas as dependências de repositório necessárias
     * para agregar os dados de uma disciplina.
     *
     * @param disciplinaRepository Repositório para acesso aos dados de Disciplina.
     * @param resumoRepository     Repositório para acesso aos dados de Resumo.
     * @param materialRepository   Repositório para acesso aos dados de Material.
     * @param usuarioRepository    Repositório para acesso aos dados de Usuario.
     */
    public DefaultObterDisciplinaPorIdService(DisciplinaRepository disciplinaRepository,
                                              ResumoRepository resumoRepository,
                                              MaterialRepository materialRepository,
                                              UsuarioRepository usuarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.resumoRepository = resumoRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método orquestra a busca da disciplina principal e, em seguida,
     * busca as listas de resumos e materiais associados, garantindo que todas as
     * consultas sejam filtradas pelo usuário autenticado.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DetalheDisciplinaResponse> executar(UUID id, Pageable pageable) {

        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado."));

        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId());

        if (disciplinaOptional.isEmpty()) {
            return Optional.empty();
        }

        Disciplina disciplina = disciplinaOptional.get();


        List<Resumo> todosOsResumos = resumoRepository.buscarPorDisciplinaEUsuario(disciplina.getId(), usuario.getId());


        // Em vez de buscar uma lista, agora chamamos o novo método paginado.
        Page<Material> paginaDeMateriais = materialRepository.buscarPorDisciplinaDeFormaPaginada(disciplina.getId(), pageable);

        // A construção da resposta agora usa a página de materiais
        DetalheDisciplinaResponse response = DetalheDisciplinaResponse.fromDomain(disciplina, todosOsResumos, paginaDeMateriais);

        return Optional.of(response);
    }
}

