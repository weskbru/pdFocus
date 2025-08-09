package com.pdfocus.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro customizado que intercepta todas as requisições HTTP para processar o token JWT.
 * Este filtro é executado uma vez por requisição.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extrai o cabeçalho 'Authorization' da requisição.
        final String authHeader = request.getHeader("Authorization");

        // 2. Se não houver cabeçalho ou se ele não começar com "Bearer ", continua para o próximo filtro.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token JWT do cabeçalho (removendo o prefixo "Bearer ").
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        // 4. Se temos o email e o usuario ainda não está autenticado no contexto de segurança...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // carrega os detalhes do usuario do banco de dados.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Se o token for válido...
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                // cria um objeto de autenticação e o define no contexto de segurança.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas pois já foram validadas.
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Informa ao Spring Security que o usuario está autenticado para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continua a cadeia de filtros.
        filterChain.doFilter(request, response);
    }
}