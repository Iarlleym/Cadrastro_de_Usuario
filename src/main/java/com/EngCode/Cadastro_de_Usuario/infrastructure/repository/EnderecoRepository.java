package com.EngCode.Cadastro_de_Usuario.infrastructure.repository;
// Define o pacote onde o repositório reside. 'infrastructure.repository' é o padrão
// para a camada que lida diretamente com o acesso ao banco de dados (Infraestrutura).

import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Endereco;
// Importa a Entidade JPA que este repositório irá gerenciar.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa a interface principal do Spring Data JPA.
import org.springframework.stereotype.Repository;
// Importa a anotação @Repository.

@Repository
// Marca a interface como um componente de Repositório gerenciado pelo Spring.
// Isso indica que a classe tem a função de lidar com a persistência de dados.
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
// Repositório é sempre uma interface.

    // extends JpaRepository<Endereco, Long>: Esta é a parte mágica do Spring Data JPA.
    // 1. Endereco: É a Entidade (tabela) que este repositório gerencia.
    // 2. Long: É o tipo de dado da Chave Primária (PK) da Entidade Endereco (o campo 'id').

    // FUNÇÃO: Ao estender JpaRepository, o Spring gera e implementa automaticamente
    // métodos CRUD essenciais, como save(), findById(), findAll(), delete(), etc.,
    // eliminando a necessidade de escrever classes de implementação.

    // Esta interface está vazia, mas já possui todo o CRUD por herança.
    // Se você precisasse de métodos específicos (ex: buscar por CEP), eles seriam definidos aqui.
}