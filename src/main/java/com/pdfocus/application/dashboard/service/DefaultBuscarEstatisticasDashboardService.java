package com.pdfocus.application.dashboard.service;

import com.pdfocus.application.dashboard.dto.DashboardEstatisticasResponse;
import com.pdfocus.application.dashboard.port.entrada.BuscarEstatisticasDashboardUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do caso de uso para buscar estatísticas.
 * O nosso "bibliotecário-chefe" que sabe onde encontrar e como contar os dados.
 */
@Service
public class DefaultBuscarEstatisticasDashboardService implements BuscarEstatisticasDashboardUseCase {

    // O bibliotecário precisa das chaves para todos os fichários.
    private final UsuarioRepository usuarioRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ResumoRepository resumoRepository;
    private final MaterialRepository materialRepository;

    public DefaultBuscarEstatisticasDashboardService(UsuarioRepository usuarioRepository,
                                                     DisciplinaRepository disciplinaRepository,
                                                     ResumoRepository resumoRepository,
                                                     MaterialRepository materialRepository) {
        this.usuarioRepository = usuarioRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.resumoRepository = resumoRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional(readOnly = true) // Boa prática para operações de apenas leitura.
    public DashboardEstatisticasResponse executar() {
        // 1. Primeiro, descobrimos para quem é o relatório (o usuário logado).
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        // 2. O bibliotecário vai a cada "fichário" e conta os registros daquele usuário.
        long totalDisciplinas = disciplinaRepository.countByUsuario(usuario);
        long resumosCriados = resumoRepository.countByUsuario(usuario);
        long totalMateriais = materialRepository.countByUsuario(usuario);

        // 3. Ele compila tudo no "relatório" final.
        return new DashboardEstatisticasResponse(
                totalDisciplinas,
                resumosCriados,
                totalMateriais
        );
    }
}