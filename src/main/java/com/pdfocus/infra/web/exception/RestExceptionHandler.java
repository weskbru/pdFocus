package com.pdfocus.infra.web.exception;

import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.EmailJaCadastradoException;
import com.pdfocus.core.exceptions.ResumoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler de exceções global para a API REST.
 * Captura exceções específicas lançadas pelos controllers ou serviços
 * e as converte em respostas HTTP apropriadas e padronizadas.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
    /**
     * Manipula exceções de "Recurso Não Encontrado" para Disciplina e Resumo.
     * Retorna o status HTTP 404 (Not Found).
     *
     * @param ex A exceção capturada (DisciplinaNaoEncontradaException ou ResumoNaoEncontradoException).
     * @param request O contexto da requisição web.
     * @return Uma resposta HTTP 404.
     */
    @ExceptionHandler({ DisciplinaNaoEncontradaException.class, ResumoNaoEncontradoException.class})
    protected ResponseEntity<Object> handleNaoEncontrado(RuntimeException ex, WebRequest request) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());

        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Manipula exceções de argumento inválido (ex: IDs nulos), que indicam uma requisição malformada.
     * Retorna o status HTTP 400 (Bad Request).
     *
     * @param ex A exceção capturada (IllegalArgumentException ou NullPointerException).
     * @param request O contexto da requisição web.
     * @return Uma resposta HTTP 400.
     */
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    protected ResponseEntity<Object> handleArgumentoInvalido(RuntimeException ex, WebRequest request) {
        logger.warn("Requisição inválida: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    /**
     * Manipula a exceção de e-mail já cadastrado.
     * Retorna o status HTTP 409 (Conflict).
     */
    @ExceptionHandler(EmailJaCadastradoException.class)
    protected ResponseEntity<Object> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        logger.warn("Tentativa de cadastro com e-mail duplicado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
