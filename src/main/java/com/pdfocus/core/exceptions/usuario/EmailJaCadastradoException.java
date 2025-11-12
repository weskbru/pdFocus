package com.pdfocus.core.exceptions.usuario;

/**
 * Exceção lançada ao tentar cadastrar um usuário com um e-mail
 * que já existe no sistema.
 */
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado.");
    }
}
