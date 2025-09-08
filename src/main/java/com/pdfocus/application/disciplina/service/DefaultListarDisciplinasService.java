package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.ListarDisciplinasUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * Implementação padrão do caso de uso para listar as disciplinas de um usuário.
 * Este serviço orquestra a validação da entrada e delega a busca ao repositório.
 */
@Service
public class DefaultListarDisciplinasService implements ListarDisciplinasUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constrói o serviço com a dependência do repositório de disciplinas.
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     */
    public DefaultListarDisciplinasService(DisciplinaRepository disciplinaRepository, UsuarioRepository usuarioRepository) {
        this.disciplinaRepository = Objects.requireNonNull(disciplinaRepository);
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository);
    }
    /**
     * {@inheritDoc}
     * <p>
     * Este metodo executa a busca de forma transacional e somente leitura para otimização.
     * Ele valida se o ID do usuário não é nulo antes de delegar a chamada para o repositório.
     * </p>
     *
     * @return Uma lista de {@link Disciplina} pertencentes ao usuário.
     * @throws IllegalArgumentException se o {@code usuarioId} for nulo.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> executar() {
        // 1. O "arquivista" verifica a identidade do solicitante.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        // 2. Com a identidade confirmada, ele busca apenas os arquivos daquele usuário.
        return disciplinaRepository.listaTodasPorUsuario(usuario.getId());
    }

}
