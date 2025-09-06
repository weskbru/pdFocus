package com.pdfocus.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Classe de configuração central para o Spring Security.
 * <p>
 * Habilita a segurança web e define a cadeia de filtros de segurança,
 * provedores de autenticação, e outros beans relacionados à segurança.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Construtor para injeção das dependências de segurança necessárias.
     *
     * @param userDetailsService O serviço para carregar os detalhes do usuário.
     * @param jwtAuthFilter O filtro customizado para processar tokens JWT.
     */
    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Define a configuração de CORS (Cross-Origin Resource Sharing) para a aplicação.
     * <p>
     * Este bean é fundamental para permitir que o frontend (rodando em um domínio diferente,
     * como http://localhost:4200) possa fazer requisições para este backend.
     * </p>
     * @return a fonte de configuração de CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Define explicitamente qual origem (frontend) tem permissão
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        // Define os métodos HTTP permitidos (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite todos os cabeçalhos na requisição
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite o envio de credenciais (como cookies ou tokens de autorização)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os endpoints da sua API ("/**")
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) que será aplicada
     * a todas as requisições HTTP. É o ponto central de configuração de segurança da web.
     *
     * @param http o objeto {@link HttpSecurity} a ser configurado.
     * @return a cadeia de filtros de segurança construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão como stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Permite acesso público aos endpoints de autenticação
                        .anyRequest().authenticated() // Exige autenticação para todas as outras requisições
                )
                .authenticationProvider(authenticationProvider()) // Registra o provedor de autenticação customizado
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro JWT antes do filtro padrão
                .build();
    }

    /**
     * Cria e configura o provedor de autenticação (Authentication Provider).
     * Este componente é responsável por conectar o serviço que busca usuários (`UserDetailsService`)
     * com o mecanismo que verifica as senhas (`PasswordEncoder`).
     *
     * @return uma instância de {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Define o bean do codificador de senhas (Password Encoder) para a aplicação.
     * Utilizamos o BCrypt, que é o padrão recomendado e muito seguro.
     *
     * @return uma instância de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe o {@link AuthenticationManager} do Spring Security como um Bean.
     * Este bean é necessário para o nosso serviço de autenticação (`DefaultAutenticarUsuarioService`)
     * para poder processar as tentativas de login.
     *
     * @param config a configuração de autenticação do Spring.
     * @return o {@link AuthenticationManager} configurado.
     * @throws Exception se ocorrer um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}