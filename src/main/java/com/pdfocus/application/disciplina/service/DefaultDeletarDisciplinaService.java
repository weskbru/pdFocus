package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.DeletarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação do caso de uso para apagar uma disciplina existente.
 * Garante que apenas o utilizador proprietário possa realizar a operação.
 */
@Service
public class DefaultDeletarDisciplinaService implements DeletarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;

    public DefaultDeletarDisciplinaService(DisciplinaRepository disciplinaRepository, UsuarioRepository usuarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. Ele primeiro busca o utilizador
     * logado e, em seguida, verifica a posse da disciplina antes de a apagar.
     */
    @Override
    @Transactional
    public void executar(UUID id) {
        // Obtém o email do utilizador a partir do principal de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Busca a entidade de domínio do utilizador correspondente.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado na base de dados."));

        // Antes de apagar, primeiro verificamos se a disciplina existe E pertence ao utilizador.
        // Se a busca não retornar nada, significa que ou a disciplina não existe, ou o
        // utilizador não tem permissão para a apagar. Em ambos os casos, lançamos uma exceção.
        if (disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId()).isEmpty()) {
            throw new DisciplinaNaoEncontradaException(id);
        }

        // Apenas se a verificação de posse for bem-sucedida, a operação de apagar é executada.
        disciplinaRepository.deletarPorId(id);
    }
}
