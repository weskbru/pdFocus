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
    public Optional<DetalheDisciplinaResponse> executar(UUID id) {
        // Obtém o usuário autenticado a partir do contexto de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado."));

        // Busca a disciplina principal, garantindo que ela pertence ao usuário.
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId());

        // Se a disciplina principal não for encontrada para o usuário, retorna vazio.
        if (disciplinaOptional.isEmpty()) {
            return Optional.empty();
        }

        Disciplina disciplina = disciplinaOptional.get();

        // Busca as coleções de resumos e materiais associados.
        List<Resumo> resumos = resumoRepository.buscarPorDisciplinaEUsuario(disciplina.getId(), usuario.getId());
        List<Material> materiais = materialRepository.listarPorDisciplinaEUsuario(disciplina.getId(), usuario.getId());

        // Monta o DTO de resposta completo com todos os dados agregados.
        DetalheDisciplinaResponse response = DetalheDisciplinaResponse.fromDomain(disciplina, resumos, materiais);

        return Optional.of(response);
    }
}

