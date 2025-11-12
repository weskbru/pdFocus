package com.pdfocus.core.shared;

import com.pdfocus.core.exceptions.CampoNuloException;
import com.pdfocus.core.exceptions.CampoVazioException;

/**
 * Utilitário central para validações de domínio.
 * <p>
 * Fornece métodos estáticos que reforçam invariantes simples, como:
 * garantir que valores não sejam {@code null} ou vazios.
 * </p>
 *
 * <p>
 * Esse utilitário evita duplicação de lógica em entidades
 * e mantém as regras de consistência concentradas em um único ponto.
 * </p>
 *
 * @see com.pdfocus.core.exceptions.CampoNuloException
 * @see com.pdfocus.core.exceptions.CampoVazioException
 */
public final class Validador {

    // Construtor privado para impedir instanciação
    private Validador() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada.");
    }

    /**
     * Garante que o valor não seja {@code null}.
     *
     * @param valor     Valor a ser validado.
     * @param mensagem  Mensagem da exceção, caso o valor seja nulo.
     * @param <T>       Tipo genérico do valor.
     * @return O próprio valor, se válido.
     * @throws CampoNuloException se o valor for {@code null}.
     */
    public static <T> T requireNotNull(T valor, String mensagem) {
        if (valor == null) throw new CampoNuloException(mensagem);
        return valor;
    }

    /**
     * Garante que uma {@link String} não seja {@code null}, vazia ou composta apenas por espaços.
     *
     * @param valor     Texto a ser validado.
     * @param mensagem  Mensagem da exceção, caso o valor seja inválido.
     * @return O próprio valor, se válido.
     * @throws CampoNuloException  se o valor for {@code null}.
     * @throws CampoVazioException se o valor estiver vazio ou em branco.
     */
    public static String requireNotEmpty(String valor, String mensagem) {
        if (valor == null) throw new CampoNuloException(mensagem);
        if (valor.trim().isEmpty()) throw new CampoVazioException(mensagem);
        return valor;
    }
}
