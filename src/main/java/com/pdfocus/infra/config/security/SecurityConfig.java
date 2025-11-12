package com.pdfocus.infra.config.security;

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
 * Classe de configuração central responsável pela segurança da aplicação Pdfocus.
 *
 * <p>Essa classe integra o módulo de autenticação e autorização do Spring Security
 * com a arquitetura da aplicação, definindo filtros, provedores e políticas de acesso.
 *
 * <p>Seu objetivo é proteger os endpoints da API e garantir a comunicação segura
 * entre o frontend (Angular) e o backend (Spring Boot) através de autenticação JWT.
 *
 * <p>Pertence à camada <b>infra/config/security</b> dentro da arquitetura Hexagonal.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Construtor responsável por injetar as dependências principais do módulo de segurança.
     *
     * @param userDetailsService serviço responsável por carregar usuários a partir da base de dados.
     * @param jwtAuthFilter filtro responsável por interceptar requisições e validar tokens JWT.
     */
    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configura as permissões de CORS (Cross-Origin Resource Sharing) da aplicação.
     *
     * <p>Permite que o frontend hospedado em um domínio diferente (ex: <code>http://localhost:4200</code>)
     * consiga se comunicar com este backend, garantindo compatibilidade com navegadores modernos.
     *
     * @return configuração de CORS aplicada a todos os endpoints.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // CORREÇÃO: Permite qualquer origem (incluindo localhost e a futura URL do Vercel)
        // Usamos setAllowedOriginPatterns em vez de setAllowedOrigins para compatibilidade
        // com setAllowCredentials(true)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) aplicada a todas as requisições HTTP.
     *
     * <p>Essa configuração determina:
     * <ul>
     *   <li>Quais endpoints são públicos</li>
     *   <li>Quais rotas exigem autenticação JWT</li>
     *   <li>Como o Spring gerencia sessões (modo stateless)</li>
     * </ul>
     *
     * @param http configuração do {@link HttpSecurity} a ser aplicada.
     * @return a cadeia de filtros de segurança configurada.
     * @throws Exception se ocorrer falha na configuração do Spring Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/feedback",
                                "/feedback/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Cria e registra o provedor de autenticação principal da aplicação.
     *
     * <p>Responsável por integrar o {@link UserDetailsService} com o {@link PasswordEncoder},
     * garantindo a verificação segura das credenciais do usuário.
     *
     * @return instância de {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Define o codificador de senhas utilizado pelo sistema.
     *
     * <p>O algoritmo <b>BCrypt</b> é usado por ser resistente a ataques de força bruta
     * e recomendado como padrão de mercado para aplicações seguras.
     *
     * @return instância de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe o {@link AuthenticationManager} como bean para uso nos serviços de autenticação.
     *
     * <p>Permite que componentes como {@code DefaultAutenticarUsuarioService} acessem
     * o mecanismo de autenticação configurado pelo Spring Security.
     *
     * @param config configuração base do {@link AuthenticationConfiguration}.
     * @return instância configurada de {@link AuthenticationManager}.
     * @throws Exception caso ocorra erro ao obter o gerenciador de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
