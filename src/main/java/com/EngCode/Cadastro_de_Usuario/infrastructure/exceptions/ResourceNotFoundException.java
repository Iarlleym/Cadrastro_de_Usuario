package com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    //Resource Not Found Exception - indica que um recurso não foi encontrado.

    public ResourceNotFoundException (String mensagem) {
        super(mensagem);
    }

    public ResourceNotFoundException (String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }

}
