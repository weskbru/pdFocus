package com.pdfocus.application.material.service;

import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import com.pdfocus.application.material.port.entrada.BuscarMateriaisRecentesUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository; // 1. PRECISAMOS DO REPOSITÓRIO DE DISCIPLINA
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultBuscarMateriaisRecentesService implements BuscarMateriaisRecentesUseCase {

    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;

    public DefaultBuscarMateriaisRecentesService(UsuarioRepository usuarioRepository,
                                                 MaterialRepository materialRepository,
                                                 DisciplinaRepository disciplinaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialRecenteResponse> executar() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        List<Material> materiaisRecentes = materialRepository.buscar5MaisRecentesPorUsuario(usuario);

        // Para cada material encontrado, buscamos sua respectiva disciplina e criamos o DTO.
        return materiaisRecentes.stream().map(material -> {
            var disciplina = disciplinaRepository.buscarPorId(material.getDisciplinaId()).orElse(null);
            return MaterialRecenteResponse.fromDomain(material, disciplina);
        }).collect(Collectors.toList());
    }
}