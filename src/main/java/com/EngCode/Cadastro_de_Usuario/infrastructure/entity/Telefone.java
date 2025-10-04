package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "telefone")


public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "numero", length = 10)
    private String numero;
    @Column(name = "ddd",length = 3)
    private String ddd;
    @Column (name = "usuario_id")
    private Long usuario_id;


}
