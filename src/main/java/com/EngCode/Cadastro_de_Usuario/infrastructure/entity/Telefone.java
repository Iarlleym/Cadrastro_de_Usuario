package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "telefone")
@Builder

public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "numero", length = 10)
    private String numero;
    @Column(name = "ddd",length = 3)
    private String ddd;

}
