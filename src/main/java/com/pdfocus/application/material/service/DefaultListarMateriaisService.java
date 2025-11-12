package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.ListarMateriaisUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso {@link ListarMateriaisUseCase},
 * responsável por recuperar todos os materiais associados a uma disciplina
 * específica pertencente ao usuário autenticado.
 *
 * <p>
 * Esta implementação reforça o princípio de segurança por contexto:
 * o acesso é limitado ao usuário atualmente autenticado no sistema,
 * evitando que materiais de outros usuários sejam expostos indevidamente.
 * </p>
 *
 * <p><b>Fluxo resumido:</b></p>
 * <ol>
 *   <li>Obtém o e-mail do usuário autenticado via {@link SecurityContextHolder}.</li>
 *   <li>Recupera o domínio {@link Usuario} associado ao e-mail.</li>
 *   <li>Busca os {@link Material materiais} pertencentes à disciplina e ao usuário autenticado.</li>
 * </ol>
 *
 * <p>
 * <b>Anotação técnica:</b> O método é anotado com {@code @Transactional(readOnly = true)},
 * pois apenas realiza leitura de dados sem alterar o estado do banco.
 * </p>
 */
@Service
public class DefaultListarMateriaisService implements ListarMateriaisUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor principal para injeção das dependências de repositório.
     *
     * @param materialRepository Repositório responsável pela consulta de materiais.
     * @param usuarioRepository Repositório responsável pela consulta de usuários.
     */
    public DefaultListarMateriaisService(MaterialRepository materialRepository, UsuarioRepository usuarioRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * A execução deste método obtém os materiais relacionados à disciplina informada,
     * restringindo os resultados ao usuário autenticado no contexto de segurança atual.
     * </p>
     *
     * @param disciplinaId Identificador único da disciplina cujos materiais devem ser listados.
     * @return Uma lista de {@link Material materiais} pertencentes ao usuário autenticado.
     * @throws IllegalStateException se o usuário autenticado não for encontrado no banco de dados.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Material> executar(UUID disciplinaId) {
        String email = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário autenticado não pôde ser encontrado no banco de dados."));

        return materialRepository.listarPorDisciplinaEUsuario(disciplinaId, usuario.getId());
    }
}
