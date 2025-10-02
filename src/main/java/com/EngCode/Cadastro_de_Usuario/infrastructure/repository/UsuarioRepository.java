package com.engcode.aprendendospring.infrastructure.repository;

import com.engcode.aprendendospring.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail (String email); //Optional é uma classe do java util serve para evitar o retorno de informações nulas

    @Transactional //Ajuda a não causar erro na hora de deletar
    void deleteByEmail (String email);


}
