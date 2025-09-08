package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.port.entrada.ObterDisciplinaPorIdUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do caso de uso para obter uma disciplina específica pelo seu ID.
 * Garante que a disciplina retornada pertença ao usuário autenticado.
 */
@Service
public class DefaultObterDisciplinaPorIdService implements ObterDisciplinaPorIdUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constrói o serviço com suas dependências.
     * O Spring Framework injetará automaticamente as implementações corretas
     * dos repositórios (Injeção de Dependência).
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     * @param usuarioRepository A porta de saída para a persistência de usuários.
     */
    public DefaultObterDisciplinaPorIdService(
            DisciplinaRepository disciplinaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. Ele obtém a identidade do usuário
     * a partir do contexto de segurança antes de realizar a busca no repositório.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> executar(UUID id) {
        // Obtém o email do usuário a partir do principal de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Busca a entidade de domínio do usuário correspondente.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado no banco de dados."));

        // Utiliza o método de busca segura do repositório, que filtra tanto pelo
        // ID da disciplina quanto pelo ID do usuário proprietário.
        return disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId());
    }
}

