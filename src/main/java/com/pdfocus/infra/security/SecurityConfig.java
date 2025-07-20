package com.pdfocus.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração central para o Spring Security.
 * Define as regras de segurança da aplicação, como quais endpoints são
 * públicos ou protegidos, e configura beans essenciais como o PasswordEncoder.
 */
@Configuration      // Indica que esta é uma classe de configuração do Spring.
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define a cadeia de filtros de segurança que será aplicada a todas as
     * requisições HTTP que chegam à aplicação.
     *
     * @param http o objeto HttpSecurity a ser configurado.
     * @return a cadeia de filtros de segurança construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a proteção CSRF. Essencial para APIs REST stateless.
                .csrf(csrf -> csrf.disable())

                // Configura o gerenciamento de sessão para ser STATELESS.
                // A API não guardará estado de sessão do usuário no servidor.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Define as regras de autorização para os endpoints HTTP.
                .authorizeHttpRequests(auth -> auth
                        // POR ENQUANTO, vamos permitir todas as requisições.
                        // Mais tarde, vamos especificar quais rotas são públicas e quais são protegidas.
                        // Ex: .requestMatchers("/auth/**").permitAll()
                        //     .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )

                .build();
    }

    /**
     * Cria um bean do tipo PasswordEncoder que será usado em toda a aplicação
     * para criptografar e verificar senhas.
     *
     * @return uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}