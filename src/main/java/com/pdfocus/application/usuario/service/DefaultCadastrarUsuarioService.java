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
 * Implementação padrão do caso de uso para registrar um novo usuario.
 */
@Service
public class DefaultCadastrarUsuarioService implements CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Injetando o codificador de senhas

    public DefaultCadastrarUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     * <p>
     * A operação é transacional. Primeiro, verifica se o email já está em uso.
     * Se não estiver, criptografa a senha fornecida e persiste o novo usuario.
     * </p>
     * @throws EmailJaCadastradoException se o email fornecido já existir no sistema.
     * @throws IllegalArgumentException se o comando ou seus campos essenciais forem nulos/inválidos.
     */
    @Override
    @Transactional
    public Usuario executar(CadastrarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de cadastro não pode ser nulo.");

        // Verifica se o email já existe
        usuarioRepository.buscarPorEmail(command.email())
                .ifPresent(usuarioExistente -> {
                    throw new EmailJaCadastradoException(command.email());
                });

        // Criptografa a senha em texto puro
        String senhaCriptografada = passwordEncoder.encode(command.senha());

        // Cria o novo objeto de domínio Usuario
        Usuario novoUsuario = new Usuario(
                command.nome(),
                command.email(),
                senhaCriptografada
        );

        // Salva o novo usuario no repositório
        return usuarioRepository.salvar(novoUsuario);
    }
}