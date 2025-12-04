package com.pdfocus.infra.web.handler;

import com.pdfocus.core.exceptions.LimiteFeedbackExcedidoException;
import com.pdfocus.core.exceptions.LimiteResumoExcedidoException;
import com.pdfocus.core.exceptions.ValorInvalidoException; // <--- Importante!
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.usuario.EmailJaCadastradoException;
import com.pdfocus.core.exceptions.resumo.ResumoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções para a API REST do PDFocus.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * [NOVO] Manipula erros de validação de domínio (ValorInvalidoException).
     * Retorna o motivo exato do erro no corpo da resposta (JSON).
     */
    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<Object> handleValorInvalido(ValorInvalidoException ex) {
        logger.warn("Erro de validação de domínio: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Dados Inválidos");
        body.put("message", ex.getMessage()); // <--- Aqui vai aparecer "Mensagem deve ter pelo menos 10 caracteres"

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Manipula exceções de "Recurso Não Encontrado".
     */
    @ExceptionHandler({ DisciplinaNaoEncontradaException.class, ResumoNaoEncontradoException.class})
    protected ResponseEntity<Object> handleNaoEncontrado(RuntimeException ex, WebRequest request) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Manipula exceções genéricas de argumento inválido (IllegalArgumentException).
     * AGORA RETORNA JSON COM O MOTIVO.
     */
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    protected ResponseEntity<Object> handleArgumentoInvalido(RuntimeException ex, WebRequest request) {
        logger.warn("Requisição inválida (Genérico): {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro na Requisição");
        body.put("message", ex.getMessage()); // <--- Agora vamos ver o erro no console!

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Manipula a exceção de e-mail já cadastrado.
     */
    @ExceptionHandler(EmailJaCadastradoException.class)
    protected ResponseEntity<Object> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        logger.warn("Tentativa de cadastro com e-mail duplicado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Manipula falhas de autenticação.
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("Falha na autenticação: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
    }

    /**
     * Manipula a exceção de limites excedidos (Resumo ou Feedback).
     */
    @ExceptionHandler({ LimiteFeedbackExcedidoException.class, LimiteResumoExcedidoException.class })
    public ResponseEntity<Object> handleLimitesExcedidos(RuntimeException ex) {

        logger.warn("Limite de uso atingido: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
        body.put("error", "Too Many Requests");
        body.put("message", ex.getMessage());

        if (ex instanceof LimiteResumoExcedidoException) {
            body.put("code", "LIMIT_RESUMO_PREMIUM");
        } else {
            body.put("code", "LIMIT_FEEDBACK");
        }

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(body);
    }
}