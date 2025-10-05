package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

import jakarta.persistence.*; // Importa todas as anotações do JPA (para Spring Boot 3+).
import lombok.*; // Importa o Lombok.

// BLOCÃO 1: ANOTAÇÕES LOMBOK (Estrutura da Classe)
// -------------------------------------------------------------------------
@Builder
// Gera o Padrão Builder, essencial para criar a Entity de forma limpa no Converter.
@Getter
// Gera os métodos 'get' para leitura dos atributos.
@Setter
// Gera os métodos 'set' para modificação dos atributos.
@AllArgsConstructor
// Gera um construtor com todos os atributos.
@NoArgsConstructor
// Gera o construtor vazio, que é obrigatório para o JPA/Hibernate.

// BLOCÃO 2: ANOTAÇÕES JPA (Mapeamento da Tabela)
// -------------------------------------------------------------------------
@Entity
// Marca esta classe como uma Entidade JPA, ou seja, um objeto que representa uma tabela.
@Table(name = "endereco")
// Especifica que esta Entity está mapeada para a tabela chamada "endereco" no banco de dados.
public class Endereco {

    // BLOCÃO 3: CHAVE PRIMÁRIA E CONFIGURAÇÃO DE GERAÇÃO
    // -------------------------------------------------------------------------
    @Id
    // Marca o campo 'id' como a chave primária (Primary Key) da tabela.
    @GeneratedValue (strategy = GenerationType.AUTO)
    // Define a estratégia de geração de valor para a chave primária.
    // GenerationType.AUTO deixa o Hibernate escolher a melhor estratégia para o banco de dados (PostgreSQL, no seu caso).
    private Long id;

    // BLOCÃO 4: ATRIBUTOS E COLUNAS
    // -------------------------------------------------------------------------
    @Column(name = "rua")
    // Mapeia o atributo 'rua' para a coluna 'rua'.
    private String rua;

    @Column (name = "numero")
    private Long numero;

    @Column (name = "complemento", length = 30)
    // Define o nome da coluna e o tamanho máximo (30) no banco de dados.
    private String complemento;

    @Column (name = "cidade", length = 150)
    private String cidade;

    @Column (name = "estado", length = 2)
    private String estado;

    @Column (name = "cep", length = 9)
    private String cep;

    // BLOCÃO 5: CHAVE ESTRANGEIRA (RELACIONAMENTO MANUAL)
    // -------------------------------------------------------------------------
    @Column (name = "usuario_id")
    // Mapeia a coluna que armazena a Chave Estrangeira (Foreign Key - FK).
    // Esta coluna aponta para o 'id' na tabela 'usuario', definindo o relacionamento
    // de que este Endereço pertence a um Usuário específico.
    private Long usuario_id;


}