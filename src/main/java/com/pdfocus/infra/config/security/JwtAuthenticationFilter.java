package com.pdfocus.infra.config.security;

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
 * Filtro responsável por interceptar todas as requisições HTTP e validar o token JWT.
 *
 * <p>Esse componente faz parte da cadeia de segurança do Spring e é executado
 * uma única vez por requisição ({@link OncePerRequestFilter}). Sua função é
 * identificar o cabeçalho <b>Authorization</b>, extrair o token JWT e validar
 * as credenciais do usuário.
 *
 * <p>Ao validar o token, o filtro cria uma instância de
 * {@link UsernamePasswordAuthenticationToken} e registra o usuário autenticado
 * no {@link SecurityContextHolder}, permitindo que o restante da aplicação
 * identifique a sessão ativa.
 *
 * <p>Pertence à camada <b>infra/config/security</b> dentro da arquitetura Hexagonal,
 * integrando autenticação baseada em token ao módulo de infraestrutura.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Construtor responsável por injetar os serviços de autenticação JWT e de
     * carregamento de usuários.
     *
     * @param jwtService serviço responsável pela extração e validação de tokens JWT.
     * @param userDetailsService serviço responsável por buscar informações de usuários no banco.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Executa o filtro de autenticação JWT para cada requisição HTTP.
     *
     * <p>O processo segue as etapas:
     * <ol>
     *   <li>Lê o cabeçalho <b>Authorization</b> da requisição.</li>
     *   <li>Verifica se o token JWT está presente e se é válido.</li>
     *   <li>Carrega os detalhes do usuário via {@link UserDetailsService}.</li>
     *   <li>Valida o token com o {@link JwtService}.</li>
     *   <li>Registra o usuário autenticado no contexto do Spring Security.</li>
     * </ol>
     *
     * <p>Se o token não for encontrado ou inválido, a requisição segue normalmente
     * para o próximo filtro sem autenticação.
     *
     * @param request objeto {@link HttpServletRequest} representando a requisição recebida.
     * @param response objeto {@link HttpServletResponse} representando a resposta HTTP.
     * @param filterChain cadeia de filtros da requisição.
     * @throws ServletException se ocorrer um erro durante o processamento do filtro.
     * @throws IOException se ocorrer falha de leitura ou escrita durante a requisição.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extrai o cabeçalho 'Authorization' da requisição.
        final String authHeader = request.getHeader("Authorization");

        // 2. Se não houver cabeçalho ou se ele não começar com "Bearer ", segue adiante.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token JWT do cabeçalho (removendo o prefixo "Bearer ").
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        // 4. Se há um e-mail e o contexto de segurança ainda não possui autenticação...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token e autentica o usuário.
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continua o fluxo da requisição.
        filterChain.doFilter(request, response);
    }
}
