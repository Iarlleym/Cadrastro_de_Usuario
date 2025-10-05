package com.EngCode.Cadastro_de_Usuario.infrastructure.repository;
// Define o pacote onde o repositório reside. Este é o local padrão para a camada
// que gerencia o acesso ao banco de dados (Infraestrutura).

import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Telefone;
// Importa a Entidade JPA que este repositório irá gerenciar.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa a interface principal do Spring Data JPA.
import org.springframework.stereotype.Repository;
// Importa a anotação @Repository.

@Repository
// Marca a interface como um componente de Repositório gerenciado pelo Spring.
// Isso a torna elegível para ser injetada (usada) na UsuarioService.
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
// Repositórios no Spring Data JPA são sempre interfaces.

    // extends JpaRepository<Telefone, Long>: Esta herança é o coração do Spring Data JPA.
    // 1. Telefone: É a Entidade (tabela) que este repositório está vinculado.
    // 2. Long: É o tipo de dado da Chave Primária (PK) da Entidade Telefone (o campo 'id').

    // FUNÇÃO: Ao estender JpaRepository, o Spring gera automaticamente as implementações
    // para métodos CRUD essenciais como save(), findById(), findAll(), delete(), etc.
    // Assim, você não precisa escrever código SQL ou JDBC.

    // A interface está vazia, mas já possui todas as funcionalidades CRUD por herança.
    // Métodos específicos (como buscar por DDD) seriam definidos aqui, se necessário.
}