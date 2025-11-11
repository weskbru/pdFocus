package com.pdfocus.infra.web.handler;

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

/**
 * Handler global de exceções para a API REST do PDFocus.
 *
 * <p>Captura exceções lançadas pelos controllers e serviços e as converte
 * em respostas HTTP apropriadas, garantindo padronização na comunicação
 * de erros para os clientes da API.</p>
 *
 * <p>Registra logs de warnings para monitoramento e depuração.</p>
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Manipula exceções de "Recurso Não Encontrado" para Disciplina e Resumo.
     *
     * <p>Retorna HTTP 404 (Not Found) e registra o aviso no log.</p>
     *
     * @param ex A exceção capturada ({@link DisciplinaNaoEncontradaException} ou {@link ResumoNaoEncontradoException}).
     * @param request O contexto da requisição web.
     * @return Uma resposta HTTP 404 sem corpo.
     */
    @ExceptionHandler({ DisciplinaNaoEncontradaException.class, ResumoNaoEncontradoException.class})
    protected ResponseEntity<Object> handleNaoEncontrado(RuntimeException ex, WebRequest request) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Manipula exceções de argumento inválido (ex: IDs nulos ou parâmetros incorretos).
     *
     * <p>Retorna HTTP 400 (Bad Request) e registra o aviso no log.</p>
     *
     * @param ex A exceção capturada ({@link IllegalArgumentException} ou {@link NullPointerException}).
     * @param request O contexto da requisição web.
     * @return Uma resposta HTTP 400 sem corpo.
     */
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    protected ResponseEntity<Object> handleArgumentoInvalido(RuntimeException ex, WebRequest request) {
        logger.warn("Requisição inválida: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    /**
     * Manipula a exceção de e-mail já cadastrado.
     *
     * <p>Retorna HTTP 409 (Conflict) e envia a mensagem de erro no corpo da resposta.</p>
     *
     * @param ex A exceção capturada ({@link EmailJaCadastradoException}).
     * @return Uma resposta HTTP 409 com a mensagem de erro.
     */
    @ExceptionHandler(EmailJaCadastradoException.class)
    protected ResponseEntity<Object> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        logger.warn("Tentativa de cadastro com e-mail duplicado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Manipula falhas de autenticação, como login ou senha inválidos.
     *
     * <p>Retorna HTTP 401 (Unauthorized) e envia uma mensagem genérica de erro no corpo da resposta.</p>
     *
     * @param ex A exceção capturada ({@link AuthenticationException}).
     * @return Uma resposta HTTP 401 com mensagem de erro genérica.
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("Falha na autenticação: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
    }
}
