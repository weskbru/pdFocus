package com.pdfocus.infra.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Essa configuração força o Spring MVC a responder os OPTIONS
        // antes mesmo de chegar no Security ou nos Controllers.
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:4200",
                        "https://pdfocus.vercel.app",
                        "https://pdfocus.com.br",
                        "https://www.pdfocus.com.br"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);

        System.out.println("🌐 [WEB CONFIG] CORS Global do MVC ativado!");
    }
}