package com.pdfocus.application.material.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.dto.MaterialRecenteResponse;
import com.pdfocus.application.material.port.entrada.BuscarMateriaisRecentesUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por implementar o caso de uso {@link BuscarMateriaisRecentesUseCase},
 * que recupera os materiais mais recentes associados ao usuário autenticado.
 *
 * <p>Essa implementação é usada para alimentar o dashboard com conteúdos
 * atualizados e relevantes, mantendo uma alta eficiência de consulta ao banco.</p>
 *
 * <p><b>Otimização de desempenho:</b> Esta classe passou por uma refatoração para
 * eliminar o problema do "N+1 select". Agora, todos os dados necessários são
 * buscados em uma única query, utilizando o método
 * {@link MaterialRepository#buscar5MaisRecentesPorUsuarioComDisciplina(Usuario)},
 * que executa um <i>JOIN FETCH</i> entre as entidades {@code Material} e {@code Disciplina}.</p>
 *
 * <p>Essa abordagem garante:
 * <ul>
 *   <li>Redução significativa do número de queries executadas;</li>
 *   <li>Maior desempenho na renderização do dashboard;</li>
 *   <li>Melhor aproveitamento do cache do Hibernate.</li>
 * </ul>
 * </p>
 */
@Service
public class DefaultBuscarMateriaisRecentesService implements BuscarMateriaisRecentesUseCase {

    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;

    /**
     * Cria uma nova instância do serviço, injetando as dependências necessárias.
     *
     * @param usuarioRepository Repositório de usuários.
     * @param materialRepository Repositório de materiais.
     * @param disciplinaRepository Repositório de disciplinas.
     */
    public DefaultBuscarMateriaisRecentesService(UsuarioRepository usuarioRepository,
                                                 MaterialRepository materialRepository,
                                                 DisciplinaRepository disciplinaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Executa a busca dos 5 materiais mais recentes do usuário autenticado.
     *
     * <p>O processo é dividido em três etapas:</p>
     * <ol>
     *     <li>Obtém o usuário atualmente autenticado a partir do contexto de segurança.</li>
     *     <li>Busca os 5 materiais mais recentes do usuário utilizando uma query otimizada com JOIN FETCH.</li>
     *     <li>Mapeia os resultados para DTOs do tipo {@link MaterialRecenteResponse}, sem disparar novas queries.</li>
     * </ol>
     *
     * <p><b>Nota:</b> Este método é transacional e marcado como {@code readOnly=true},
     * garantindo que não haverá alterações no estado do banco de dados.</p>
     *
     * @return uma lista contendo até 5 materiais recentes do usuário autenticado.
     * @throws IllegalStateException se o usuário autenticado não for encontrado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialRecenteResponse> executar() {
        // 1. Obtém o usuário autenticado
        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        // 2. Busca otimizada (JOIN FETCH)
        List<Material> materiaisRecentes = materialRepository.buscar5MaisRecentesPorUsuarioComDisciplina(usuario);

        // 3. Mapeamento direto (sem novas queries)
        return materiaisRecentes.stream()
                .map(material -> MaterialRecenteResponse.fromDomain(material, material.getDisciplina()))
                .collect(Collectors.toList());
    }
}
