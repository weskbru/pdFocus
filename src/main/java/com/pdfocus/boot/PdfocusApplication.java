package com.pdfocus.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

/**
 * Classe principal responsavel pela inicializacao e configuracao base da aplicacao Pdfocus.
 *
 * <p>Esta classe atua como ponto de entrada da aplicacao Spring Boot e contem
 * as anotacoes essenciais para a deteccao automatica de componentes, repositorios
 * e entidades. Em uma arquitetura modularizada, esta configuracao garante que o
 * Spring consiga identificar e gerenciar corretamente cada camada do sistema.</p>
 *
 * <p><strong>Arquitetura Modular:</strong></p>
 * <ul>
 *     <li>{@link ComponentScan} - Detecta classes anotadas com {@code @Component}, {@code @Service}, {@code @Controller} e similares.</li>
 *     <li>{@link EnableJpaRepositories} - Habilita e localiza interfaces de repositorios JPA.</li>
 *     <li>{@link EntityScan} - Define o pacote base para a varredura de entidades JPA.</li>
 * </ul>
 *
 * <p>Essa combinacao e conhecida como a "Trindade da configuracao Spring" — o nucleo
 * que conecta dominio, persistencia e camada de aplicacao.</p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.pdfocus")
@EnableJpaRepositories(basePackages = "com.pdfocus.infra.persistence.repository")
@EntityScan(basePackages = "com.pdfocus.infra.persistence.entity")
public class PdfocusApplication {

    /**
     * Ponto de entrada da aplicacao Pdfocus.
     * <p>Responsavel por inicializar o contexto do Spring Boot e iniciar
     * todos os componentes registrados.</p>
     *
     * @param args Argumentos de linha de comando utilizados na inicializacao.
     */
    public static void main(String[] args) {
        SpringApplication.run(PdfocusApplication.class, args);
    }

    /**
     * Define um {@link RestTemplate} como bean gerenciado pelo Spring.
     * <p>Esse bean pode ser injetado em servicos que precisem realizar
     * requisicoes HTTP para APIs externas.</p>
     *
     * @return uma instancia configurada de {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}