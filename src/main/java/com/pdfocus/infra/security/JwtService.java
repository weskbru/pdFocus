package com.pdfocus.infra.security;

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
 * Serviço responsável por todas as operações relacionadas a JSON Web Tokens (JWT).
 * Inclui geração, validação e extração de informações dos tokens.
 */
@Service
public class JwtService {

    // Injeta a chave secreta e o tempo de expiração do application.properties
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Gera um token JWT para um usuário autenticado.
     *
     * @param userEmail O e-mail do usuário, que será o "subject" do token.
     * @return O token JWT como uma String.
     */
    public String generateToken(String userEmail) {
        return Jwts.builder()
                .subject(userEmail) // Define o "dono" do token
                .issuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Data de expiração
                .signWith(getSignInKey()) // Assina o token com a chave secreta
                .compact();
    }

    /**
     * Valida se um token JWT é válido para um determinado e-mail de usuário.
     *
     * @param token O token a ser validado.
     * @param userEmail O e-mail do usuário para comparação.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean isTokenValid(String token, String userEmail) {
        final String emailFromToken = extractUsername(token);
        return (emailFromToken.equals(userEmail) && !isTokenExpired(token));
    }

    /**
     * Extrai o e-mail (subject) de um token JWT.
     *
     * @param token O token JWT.
     * @return O e-mail do usuário contido no token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- Métodos privados de suporte ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Prepara a chave secreta para ser usada na assinatura do token
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}