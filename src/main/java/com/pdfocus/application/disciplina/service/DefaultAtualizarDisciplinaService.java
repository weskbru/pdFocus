package com.pdfocus.application.disciplina.service;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.application.disciplina.port.entrada.AtualizarDisciplinaUseCase;
import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso para atualizar uma disciplina.
 * Este serviço orquestra a busca, validação e persistência da disciplina atualizada.
 */
@Service
public class DefaultAtualizarDisciplinaService implements AtualizarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constrói o serviço com a dependência do repositório de disciplinas.
     *
     * @param disciplinaRepository A porta de saída para a persistência de disciplinas.
     */
    public DefaultAtualizarDisciplinaService(DisciplinaRepository disciplinaRepository, UsuarioRepository usuarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este método executa a atualização de uma disciplina em uma única transação.
     * Ele primeiro busca a disciplina existente pelo seu ID. Se encontrada, cria uma nova
     * instância de domínio com os dados atualizados (o que aciona as validações
     * do domínio) e então a persiste.
     * </p>
     *
     * @param id      O ID da disciplina a ser atualizada. Não pode ser nulo.
     * @param command O comando contendo os novos dados. Não pode ser nulo.
     * @return Um {@link Optional} contendo a {@link Disciplina} atualizada se a operação
     * foi bem-sucedida, ou um Optional vazio se a disciplina original não foi encontrada.
     * @throws IllegalArgumentException se o ID ou o comando forem nulos.
     */
    @Override
    @Transactional
    public Disciplina executar(UUID id, AtualizarDisciplinaCommand command) {
        // 1. verifica a identidade do usuário logado
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado no banco de dados."));

        // 2. Ele busca a disciplina, mas com uma condição de segurança crucial:
        Disciplina disciplinaExistente = disciplinaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(id));

        // 3. Com a posse confirmada, ele usa as novas "ferramentas" (setters)
        disciplinaExistente.setNome(command.nome());
        disciplinaExistente.setDescricao(command.descricao());

        // 4. Salva (persiste) a disciplina atualizada. O JPA é inteligente
        return disciplinaRepository.salvar(disciplinaExistente);
    }
}
