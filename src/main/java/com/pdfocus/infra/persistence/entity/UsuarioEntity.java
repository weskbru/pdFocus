package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Entidade JPA que representa um usuário no banco de dados.
 * <p>
 * Esta classe tem uma dupla responsabilidade:
 * 1. Mapear a tabela "usuarios" para a persistência com JPA.
 * 2. Implementar a interface {@link UserDetails} do Spring Security, permitindo que
 * instâncias desta classe sejam usadas diretamente pelo framework de segurança
 * para processos de autenticação e autorização.
 * </p>
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UsuarioEntity implements UserDetails {

    /**
     * O identificador único do usuário (Chave Primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * O nome completo do usuário.
     */
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * O e-mail do usuário, que é usado como "username" para o login.
     * A restrição {@code unique = true} garante que não haja e-mails duplicados no banco.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * O hash da senha do usuário, gerado por um algoritmo como o BCrypt.
     * Este campo nunca armazena a senha em texto puro.
     */
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    /**
     * Contador de resumos realizados no dia atual.
     * Reinicia para 0 quando a dataUltimoUso for diferente de hoje.
     */
    @Column(name = "resumos_hoje", nullable = false)
    private Integer resumosHoje = 0;

    /**
     * Registra a data da última vez que o usuário consumiu um recurso limitado.
     * Usado para verificar se precisa resetar o contador.
     */
    @Column(name = "data_ultimo_uso", nullable = false)
    private LocalDate dataUltimoUso = LocalDate.now();

    // --- MÉTODOS DA INTERFACE UserDetails ---

    /**
     * Retorna as permissões (roles/authorities) concedidas ao usuário.
     * Para este projeto, como não estamos implementando um sistema de papéis (ex: ADMIN, USER),
     * retornamos uma coleção vazia.
     *
     * @return uma coleção vazia de GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     * O Spring Security espera que este método retorne a senha criptografada (hash).
     *
     * @return o hash da senha do usuário.
     */
    @Override
    public String getPassword() {
        return this.senhaHash;
    }

    /**
     * Retorna o nome de usuário usado para autenticar o usuário.
     * No nosso sistema, o e-mail é o nome de usuário.
     *
     * @return o e-mail do usuário.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indica se a conta do usuário não expirou.
     * Retornando {@code true}, a conta é considerada sempre válida.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário não está bloqueado.
     * Retornando {@code true}, o usuário é considerado sempre desbloqueado.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais do usuário (senha) não expiraram.
     * Retornando {@code true}, as credenciais são consideradas sempre válidas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário está habilitado ou desabilitado.
     * Retornando {@code true}, o usuário é considerado sempre habilitado.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}