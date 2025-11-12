package com.pdfocus.application.disciplina.service;


import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.CriarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação da interface {@link CriarDisciplinaUseCase}.
 * Responsável por criar uma nova {@link Disciplina} e persistí-la
 * utilizando o {@link DisciplinaRepository}.
 */
@Service
public class DefaultCriarDisciplinaService implements CriarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor que recebe uma instância de {@link DisciplinaRepository}
     * para realizar a persistência das disciplinas.
     *
     * @param disciplinaRepository O repositório de disciplinas.
     */
    public DefaultCriarDisciplinaService(DisciplinaRepository disciplinaRepository, UsuarioRepository usuarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. A identidade do usuário é agora
     * obtida a partir do contexto de segurança, garantindo que a nova disciplina
     * seja sempre associada ao usuário autenticado que fez a requisição.
     */
    @Override
    @Transactional
    public Disciplina executar(CriarDisciplinaCommand command) {
        // Obtém o email do usuário a partir do principal de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Busca a entidade de domínio do usuário correspondente.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado no banco de dados."));

        // Cria a nova instância da disciplina, associando-a ao usuário correto.
        Disciplina novaDisciplina = new Disciplina(
                UUID.randomUUID(),
                command.nome(),
                command.descricao(),
                usuario.getId() // Usa o ID do usuário obtido de forma segura.
        );

        // Persiste a nova disciplina.
        return disciplinaRepository.salvar(novaDisciplina);
    }
}