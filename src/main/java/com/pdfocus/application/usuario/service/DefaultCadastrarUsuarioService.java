package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.dto.CadastrarUsuarioCommand;
import com.pdfocus.application.usuario.port.entrada.CadastrarUsuarioUseCase;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.usuario.EmailJaCadastradoException;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Implementação padrão do caso de uso {@link CadastrarUsuarioUseCase} para registrar
 * um novo usuário no sistema.
 *
 * <p>Este serviço é responsável por:
 * <ul>
 *     <li>Validar se o e-mail fornecido já está em uso;</li>
 *     <li>Criptografar a senha do usuário;</li>
 *     <li>Persistir o novo usuário no repositório.</li>
 * </ul></p>
 *
 * <p>A operação é transacional, garantindo consistência no banco de dados.</p>
 */
@Service
public class DefaultCadastrarUsuarioService implements CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor do serviço.
     *
     * @param usuarioRepository Repositório para persistência de usuários.
     * @param passwordEncoder   Codificador de senhas para criptografia segura.
     */
    public DefaultCadastrarUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executa o caso de uso de cadastro de um novo usuário.
     *
     * <p>O método realiza os seguintes passos:
     * <ol>
     *     <li>Verifica se o e-mail já está cadastrado e lança {@link EmailJaCadastradoException} se estiver;</li>
     *     <li>Criptografa a senha fornecida;</li>
     *     <li>Cria e persiste o novo usuário no repositório.</li>
     * </ol></p>
     *
     * @param command Comando {@link CadastrarUsuarioCommand} contendo os dados do usuário a ser criado.
     * @return O {@link Usuario} recém-criado e persistido.
     * @throws EmailJaCadastradoException Se o e-mail fornecido já existir no sistema.
     * @throws IllegalArgumentException Se o comando ou campos essenciais forem nulos ou inválidos.
     */
    @Override
    @Transactional
    public Usuario executar(CadastrarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de cadastro não pode ser nulo.");

        usuarioRepository.buscarPorEmail(command.email())
                .ifPresent(usuarioExistente -> {
                    throw new EmailJaCadastradoException(command.email());
                });

        String senhaCriptografada = passwordEncoder.encode(command.senha());

        Usuario novoUsuario = new Usuario(
                command.nome(),
                command.email(),
                senhaCriptografada
        );

        return usuarioRepository.salvar(novoUsuario);
    }
}
