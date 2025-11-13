package com.pdfocus.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.pdfocus")
@EnableJpaRepositories(basePackages = "com.pdfocus.infra.persistence.repository")
@EntityScan(basePackages = "com.pdfocus.infra.persistence.entity")
public class PdfocusApplication {
    public static void main(String[] args) {
        SpringApplication.run(PdfocusApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}