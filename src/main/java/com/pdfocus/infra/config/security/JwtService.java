package com.pdfocus.infra.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Serviço responsável por todas as operações relacionadas a tokens JWT (JSON Web Tokens).
 *
 * <p>Essa classe centraliza a lógica de autenticação baseada em token, incluindo:
 * <ul>
 *   <li>Geração de tokens seguros para usuários autenticados</li>
 *   <li>Validação de tokens recebidos em requisições</li>
 *   <li>Extração de informações do payload (como o e-mail do usuário)</li>
 * </ul>
 *
 * <p>Os tokens gerados seguem o padrão <b>RFC 7519</b> e são assinados com um segredo
 * configurado em {@code application.properties}. O tempo de expiração também é
 * parametrizável, garantindo flexibilidade e segurança.
 *
 * <p>Pertence à camada <b>infra/config/security</b> dentro da arquitetura Hexagonal.
 * É consumido pelo filtro {@link JwtAuthenticationFilter} durante o ciclo de autenticação.
 */
@Service
public class JwtService {

    /** Chave secreta usada para assinar e validar tokens JWT. */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /** Tempo de expiração do token (em milissegundos). */
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * <p>O e-mail do usuário é armazenado como <b>subject</b> do token.
     * A assinatura utiliza a chave secreta definida nas propriedades da aplicação.
     *
     * @param userEmail e-mail do usuário autenticado.
     * @return token JWT gerado e assinado.
     */
    public String generateToken(String userEmail) {
        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Valida se um token JWT é válido para um determinado usuário.
     *
     * <p>Essa verificação inclui:
     * <ul>
     *   <li>Comparação do e-mail contido no token com o do usuário autenticado</li>
     *   <li>Verificação de expiração do token</li>
     *   <li>Validação da assinatura digital</li>
     * </ul>
     *
     * @param token token JWT a ser validado.
     * @param userEmail e-mail do usuário para verificação.
     * @return {@code true} se o token for válido, caso contrário {@code false}.
     */
    public boolean isTokenValid(String token, String userEmail) {
        final String emailFromToken = extractUsername(token);
        return (emailFromToken.equals(userEmail) && !isTokenExpired(token));
    }

    /**
     * Extrai o e-mail (subject) contido no token JWT.
     *
     * @param token token JWT.
     * @return e-mail do usuário extraído do payload.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ============================================================
    // Métodos privados auxiliares (não expostos publicamente)
    // ============================================================

    /**
     * Verifica se o token JWT já expirou.
     *
     * @param token token JWT.
     * @return {@code true} se o token estiver expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token token JWT.
     * @return data de expiração do token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai uma informação específica (claim) do token JWT.
     *
     * <p>Usa uma função resolutora para acessar dinamicamente qualquer campo do payload.
     *
     * @param token token JWT.
     * @param claimsResolver função que define qual claim deve ser retornado.
     * @param <T> tipo da informação retornada.
     * @return valor da claim desejada.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Lê e valida todas as claims de um token JWT.
     *
     * @param token token JWT.
     * @return objeto {@link Claims} contendo o payload decodificado.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Constrói e retorna a chave secreta usada para assinar tokens JWT.
     *
     * <p>A chave é decodificada a partir de Base64, conforme configurada no
     * arquivo {@code application.properties}.
     *
     * @return instância de {@link SecretKey} utilizada na assinatura dos tokens.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
