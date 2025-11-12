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
 * Implementação do caso de uso {@link BuscarEstatisticasDashboardUseCase} para coletar
 * estatísticas do dashboard do usuário.
 *
 * <p>Este serviço atua como um "bibliotecário-chefe", responsável por:
 * <ul>
 *     <li>Identificar o usuário autenticado;</li>
 *     <li>Consultar os repositórios de disciplinas, resumos e materiais;</li>
 *     <li>Agregar os dados e retornar um relatório consolidado.</li>
 * </ul></p>
 *
 * <p>É anotado com {@link Service} e {@link Transactional(readOnly = true)} para garantir
 * operações de leitura seguras e consistentes.</p>
 */
@Service
public class DefaultBuscarEstatisticasDashboardService implements BuscarEstatisticasDashboardUseCase {

    private final UsuarioRepository usuarioRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ResumoRepository resumoRepository;
    private final MaterialRepository materialRepository;

    /**
     * Construtor do serviço.
     *
     * @param usuarioRepository    Repositório de usuários.
     * @param disciplinaRepository Repositório de disciplinas.
     * @param resumoRepository     Repositório de resumos.
     * @param materialRepository   Repositório de materiais.
     */
    public DefaultBuscarEstatisticasDashboardService(UsuarioRepository usuarioRepository,
                                                     DisciplinaRepository disciplinaRepository,
                                                     ResumoRepository resumoRepository,
                                                     MaterialRepository materialRepository) {
        this.usuarioRepository = usuarioRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.resumoRepository = resumoRepository;
        this.materialRepository = materialRepository;
    }

    /**
     * Executa o caso de uso de busca de estatísticas do dashboard.
     *
     * <p>O método realiza os seguintes passos:
     * <ol>
     *     <li>Recupera o usuário autenticado a partir do contexto de segurança;</li>
     *     <li>Consulta os repositórios para contar disciplinas, resumos e materiais;</li>
     *     <li>Retorna um {@link DashboardEstatisticasResponse} com os dados consolidados.</li>
     * </ol></p>
     *
     * @return Um {@link DashboardEstatisticasResponse} contendo o total de disciplinas,
     * resumos criados e materiais do usuário.
     * @throws IllegalStateException Se o usuário autenticado não for encontrado no repositório.
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardEstatisticasResponse executar() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        long totalDisciplinas = disciplinaRepository.countByUsuario(usuario);
        long resumosCriados = resumoRepository.countByUsuario(usuario);
        long totalMateriais = materialRepository.countByUsuario(usuario);

        return new DashboardEstatisticasResponse(
                totalDisciplinas,
                resumosCriados,
                totalMateriais
        );
    }
}
