package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Builder


public class Usuario implements UserDetails { //implementa UserDatails para validar o usuário de acesso de login e senha.


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "nome" , length = 100)
    private String nome;
    @Column (name = "email", length = 100)
    private String email;
    @Column (name = "senha")
    private String senha;
    //@OneToMany faz o relacionamento para vários itens da tabela
    //@OneToOne faz o relacionamento para um único ítem da tabela
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Endereco> enderecos;
    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn (name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;

    //Métodos obrigatorios do UserDetails implementados, coloca a senha e o e-mail para login.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha ;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
