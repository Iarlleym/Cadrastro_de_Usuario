package com.EngCode.Cadastro_de_Usuario.business.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TelefoneDTO {

    private String numero;
    private String ddd;

}
