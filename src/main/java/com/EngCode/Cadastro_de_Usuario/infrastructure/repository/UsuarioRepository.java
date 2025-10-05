package com.EngCode.Cadastro_de_Usuario.infrastructure.repository;
// Define o pacote onde o repositório reside. Infraestrutura é a camada que interage
// diretamente com recursos externos, como o banco de dados.

import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;
// Importa a Entidade JPA que este repositório gerencia.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa a interface principal do Spring Data JPA.
import org.springframework.stereotype.Repository;
// Importa a anotação @Repository.
import org.springframework.transaction.annotation.Transactional;
// Importa a anotação @Transactional.

import java.util.Optional;
// Importa a classe Optional, usada para lidar com resultados que podem ser nulos.

// BLOCÃO 1: ESTRUTURA E HERANÇA
// -------------------------------------------------------------------------
@Repository
// Marca a interface como um componente de Repositório gerenciado pelo Spring.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
// Repositórios são interfaces. Ao estender JpaRepository:
// 1. Usuario: Entidade mapeada.
// 2. Long: Tipo da Chave Primária (ID) da Entidade Usuario.
// FUNÇÃO: O Spring gera automaticamente os métodos CRUD (save, findById, findAll, etc.).

    // BLOCÃO 2: QUERY METHODS (Consultas Derivadas)
    // -------------------------------------------------------------------------

    boolean existsByEmail(String email);
    // FUNÇÃO: Método de consulta derivado (Query Method). O Spring lê o nome
    // ('existsByEmail') e cria o SQL: 'SELECT COUNT(*) > 0 FROM usuario WHERE email = ?'.
    // É usado na camada Service para verificar rapidamente se o e-mail já existe.

    Optional<Usuario> findByEmail (String email);
    // FUNÇÃO: Método de consulta derivado que busca um usuário pelo e-mail.

    // Optional é uma classe do java util serve para evitar o retorno de informações nulas
    // CONCEITO: O retorno é encapsulado em Optional. Isso força o código que chama
    // a lidar explicitamente com a possibilidade de o resultado ser nulo, prevenindo
    // NullPointerExceptions (com métodos como .orElseThrow()).

    // BLOCÃO 3: OPERAÇÕES DE MODIFICAÇÃO
    // -------------------------------------------------------------------------

    @Transactional //Ajuda a não causar erro na hora de deletar
        // CONCEITO: A anotação @Transactional é NECESSÁRIA em métodos que modificam dados
        // e que foram definidos por um nome (Query Method), como este (deleteByEmail).
        // Ela garante que a operação seja executada como uma transação atômica no banco.
    void deleteByEmail (String email);
    // FUNÇÃO: Método de consulta derivado que remove um usuário com base no e-mail.
    // O 'void' indica que o método não retorna dados.
}