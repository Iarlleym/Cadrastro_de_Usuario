package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

// BLOCÃO 1: IMPORTAÇÕES
// -------------------------------------------------------------------------

import jakarta.persistence.*; // Anotações de Persistência (JPA/Hibernate) para mapear o banco.
import lombok.*; // Anotações do Lombok (Builder, Getters, Setters).
import org.springframework.security.core.GrantedAuthority; // Interface de permissões do Spring Security.
import org.springframework.security.core.userdetails.UserDetails; // Interface essencial para o Login/Autenticação.

import java.util.Collection; // Usado por GrantedAuthority.
import java.util.List; // Usado para listas de Endereços e Telefones.

// BLOCÃO 2: ANOTAÇÕES LOMBOK E JPA
// -------------------------------------------------------------------------
@Builder
// Gera o Padrão Builder, usado no Converter para criar a Entity de forma limpa.
@Getter
// Gera os métodos 'get' para leitura dos atributos.
@Setter
// Gera os métodos 'set' para modificação dos atributos.
@AllArgsConstructor
// Gera um construtor com todos os atributos.
@NoArgsConstructor
// Gera o construtor vazio, obrigatório para o JPA/Hibernate.

@Entity
// Marca a classe como uma Entidade JPA, indicando que ela representa uma tabela no banco.
@Table(name = "usuario")
// Especifica que esta Entity está mapeada para a tabela chamada "usuario".

public class Usuario implements UserDetails {
    // Implementa a interface UserDetails. Isso é OBRIGATÓRIO para que o Spring Security
    // possa carregar os dados de login (e-mail e senha) e autenticar o usuário.

    // BLOCÃO 3: CHAVE PRIMÁRIA E ATRIBUTOS BÁSICOS
    // -------------------------------------------------------------------------
    @Id
    // Marca o campo 'id' como a chave primária (Primary Key - PK) da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Define a estratégia de geração de valor da PK. IDENTITY é ideal para PostgreSQL,
    // indicando que o banco de dados gerencia o auto-incremento.
    private Long id;

    @Column(name = "nome" , length = 100)
    // Mapeia o atributo 'nome' para a coluna 'nome', com tamanho 100.
    private String nome;

    @Column (name = "email", length = 100)
    // Mapeia o atributo 'email' (usado como login) para a coluna 'email'.
    private String email;

    @Column (name = "senha")
    // Mapeia o atributo 'senha'. Aqui é onde o hash criptografado é armazenado.
    private String senha;

    // BLOCÃO 4: RELACIONAMENTOS UM-PARA-MUITOS (One-to-Many)
    // -------------------------------------------------------------------------

    @OneToMany(cascade = CascadeType.ALL)
    // Define o relacionamento: Um Usuario pode ter Muitos Enderecos.
    // cascade = CascadeType.ALL significa que se você deletar o Usuario, todos os Enderecos relacionados também serão deletados.
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    // Define a Chave Estrangeira (FK). "usuario_id" é o nome da coluna na tabela Endereco que guarda o ID do Usuario (referenciando o 'id' desta classe).
    private List<Endereco> enderecos;

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn (name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;
    // O mesmo relacionamento e regras de cascade se aplicam à lista de Telefones.

    // BLOCÃO 5: IMPLEMENTAÇÃO DA INTERFACE USERDETAILS (SEGURANÇA)
    // -------------------------------------------------------------------------

    // Métodos obrigatórios do UserDetails implementados, usados no fluxo de login/autenticação.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a lista de autoridades/permissões do usuário (Ex: "ROLE_ADMIN", "ROLE_USER").
        // Aqui está vazio (List.of()), indicando que as permissões básicas ainda não foram implementadas, mas a estrutura está pronta.
        return List.of();
    }

    @Override
    public String getPassword() {
        // Método que o Spring Security usa para obter a senha (o hash criptografado) para comparação durante o login.
        return senha ;
    }

    @Override
    public String getUsername() {
        // Método que o Spring Security usa para obter o identificador único (o login) do usuário.
        // Aqui, o 'email' está sendo usado como o nome de usuário (username).
        return email;
    }

    // NOTA: Há outros métodos da UserDetails que, por padrão, retornam 'true' (ex: isAccountNonExpired).
    // O Spring Security lida com a implementação padrão, mas você pode sobrescrevê-los se precisar de regras específicas (ex: bloquear conta).
}