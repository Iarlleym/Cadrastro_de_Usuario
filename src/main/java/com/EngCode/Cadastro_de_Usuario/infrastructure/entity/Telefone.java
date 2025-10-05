package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

import jakarta.persistence.*; // Importa todas as anotações do JPA (para Spring Boot 3+).
import lombok.*; // Importa o Lombok, essencial para código conciso.

// BLOCÃO 1: ANOTAÇÕES LOMBOK E ESTRUTURA
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
// Gera o construtor vazio, que é OBRIGATÓRIO para o JPA/Hibernate.

// BLOCÃO 2: ANOTAÇÕES JPA (Mapeamento da Tabela)
// -------------------------------------------------------------------------
@Entity
// Marca esta classe como uma Entidade JPA, indicando que ela está mapeada para uma tabela no banco.
@Table(name = "telefone")
// Especifica o nome exato da tabela no banco de dados (neste caso, "telefone").
public class Telefone {

    // BLOCÃO 3: CHAVE PRIMÁRIA E CONFIGURAÇÃO
    // -------------------------------------------------------------------------
    @Id
    // Marca o campo 'id' como a chave primária (Primary Key - PK) da tabela.
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Define a estratégia de geração de valor para a PK. AUTO deixa o Hibernate escolher a mais eficiente para o banco (PostgreSQL).
    private Long id;

    // BLOCÃO 4: ATRIBUTOS E COLUNAS
    // -------------------------------------------------------------------------
    @Column(name = "numero", length = 10)
    // Mapeia o atributo 'numero' para a coluna 'numero' e define o tamanho máximo no banco.
    private String numero;

    @Column(name = "ddd",length = 3)
    // Mapeia o atributo 'ddd' para a coluna 'ddd' e define o tamanho máximo.
    private String ddd;

    // BLOCÃO 5: CHAVE ESTRANGEIRA (RELACIONAMENTO)
    // -------------------------------------------------------------------------
    @Column (name = "usuario_id")
    // Mapeia a coluna que armazena a Chave Estrangeira (Foreign Key - FK).
    // Esta coluna liga este registro de Telefone ao ID do Usuário correspondente (relacionamento Um para Muitos).
    private Long usuario_id;


}