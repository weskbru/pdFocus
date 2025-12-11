package com.pdfocus.infra.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permite Credenciais (Cookies/Auth)
        config.setAllowCredentials(true);

        // 2. Define as origens permitidas (Frontend)
        // Use setAllowedOriginPatterns em vez de setAllowedOrigins para evitar erro com credenciais
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",
                "https://pdfocus.vercel.app",
                "https://pdfocus.com.br",
                "https://www.pdfocus.com.br"
        ));

        // 3. Define Cabeçalhos e Métodos
        config.addAllowedHeader("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

        // 4. Aplica a todas as rotas
        source.registerCorsConfiguration("/**", config);

        // 5. O Segredo: Cria o filtro e define a prioridade MÁXIMA
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // <--- Isso atropela qualquer regra de 404

        System.out.println("🚀 [CORS NUCLEAR] Filtro de Alta Prioridade Ativado!");

        return bean;
    }
}