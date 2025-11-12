package com.pdfocus.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

/**
 * Classe principal responsável pela inicialização e configuração base da aplicação Pdfocus.
 *
 * <p>Esta classe atua como ponto de entrada da aplicação Spring Boot e contém
 * as anotações essenciais para a detecção automática de componentes, repositórios
 * e entidades. Em uma arquitetura modularizada, esta configuração garante que o
 * Spring consiga identificar e gerenciar corretamente cada camada do sistema.</p>
 *
 * <p><strong>Arquitetura Modular:</strong></p>
 * <ul>
 *     <li>{@link ComponentScan} - Detecta classes anotadas com {@code @Component}, {@code @Service}, {@code @Controller} e similares.</li>
 *     <li>{@link EnableJpaRepositories} - Habilita e localiza interfaces de repositórios JPA.</li>
 *     <li>{@link EntityScan} - Define o pacote base para a varredura de entidades JPA.</li>
 * </ul>
 *
 * <p>Essa combinação é conhecida como a "Trindade da configuração Spring" — o núcleo
 * que conecta domínio, persistência e camada de aplicação.</p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.pdfocus")
@EnableJpaRepositories(basePackages = "com.pdfocus.infra.persistence.repository")
@EntityScan(basePackages = "com.pdfocus.infra.persistence.entity")
public class PdfocusApplication {

    /**
     * Ponto de entrada da aplicação Pdfocus.
     * <p>Responsável por inicializar o contexto do Spring Boot e iniciar
     * todos os componentes registrados.</p>
     *
     * @param args Argumentos de linha de comando utilizados na inicialização.
     */
    public static void main(String[] args) {
        SpringApplication.run(PdfocusApplication.class, args);
    }

    /**
     * Define um {@link RestTemplate} como bean gerenciado pelo Spring.
     * <p>Esse bean pode ser injetado em serviços que precisem realizar
     * requisições HTTP para APIs externas.</p>
     *
     * @return uma instância configurada de {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
