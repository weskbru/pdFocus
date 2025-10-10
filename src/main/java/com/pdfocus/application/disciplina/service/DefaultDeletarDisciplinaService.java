package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.DeletarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
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
    private final ResumoRepository resumoRepository;
    private final MaterialRepository materialRepository;

    // ✅ ATUALIZAR CONSTRUTOR
    public DefaultDeletarDisciplinaService(
            DisciplinaRepository disciplinaRepository,
            UsuarioRepository usuarioRepository,
            ResumoRepository resumoRepository,
            MaterialRepository materialRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.resumoRepository = resumoRepository;
        this.materialRepository = materialRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. Ele primeiro busca o utilizador
     * logado e, em seguida, verifica a posse da disciplina antes de a apagar.
     */
    @Override
    @Transactional
    public void executar(UUID id) {
        // 1. Verificar usuário (já existe)
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado."));

        // 2. Verificar se disciplina existe e pertence ao usuário
        if (disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId()).isEmpty()) {
            throw new DisciplinaNaoEncontradaException(id);
        }

        // 3. DELETAR RESUMOS PRIMEIRO
        resumoRepository.deletarTodosPorDisciplinaId(id);

        // 4. DELETAR MATERIAIS DEPOIS
        materialRepository.deletarTodosPorDisciplinaId(id);

        // 5. AGORA DELETAR A DISCIPLINA (sem foreign key errors)
        disciplinaRepository.deletarPorId(id);

        System.out.println("Disciplina deletada com sucesso: " + id);
    }
}