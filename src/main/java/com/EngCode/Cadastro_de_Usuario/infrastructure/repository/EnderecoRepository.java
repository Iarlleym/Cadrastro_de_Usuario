package com.EngCode.Cadastro_de_Usuario.infrastructure.repository;


import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
