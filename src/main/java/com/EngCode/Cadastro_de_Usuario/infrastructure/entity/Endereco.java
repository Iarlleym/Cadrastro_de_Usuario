package com.EngCode.Cadastro_de_Usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endereco")


public class Endereco {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "rua")
    private String rua;
    @Column (name = "numero")
    private Long numero;
    @Column (name = "complemento", length = 30)
    private String complemento;
    @Column (name = "cidade", length = 150)
    private String cidade;
    @Column (name = "estado", length = 2)
    private String estado;
    @Column (name = "cep", length = 9)
    private String cep;
    @Column (name = "usuario_id")
    private Long usuario_id;


}
