package com.EngCode.Cadastro_de_Usuario.business.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EnderecoDTO {

    private String rua;
    private long numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
}
