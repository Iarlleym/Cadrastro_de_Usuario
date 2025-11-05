package com.EngCode.Cadastro_de_Usuario.controller;

// BLOCÃO 1: IMPORTAÇÕES ESSENCIAIS
// -------------------------------------------------------------------------

// Importa suas Exceções Personalizadas (ResourceNotFound, Conflict, Unauthorized).
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ConflictException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.IllegalArgumentException; // Sua nova exceção de validação
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.UnauthorizedException;

// Importações do Spring para tratamento de exceções REST
import org.springframework.http.HttpStatus; // Classe que representa os códigos HTTP (404, 409, 401, etc.)
import org.springframework.http.ResponseEntity; // Usado para criar a resposta HTTP completa (código + corpo/mensagem)
import org.springframework.web.bind.annotation.ControllerAdvice; // Anotação que habilita o tratamento global
import org.springframework.web.bind.annotation.ExceptionHandler; // Anotação que mapeia métodos a tipos de exceção

// BLOCÃO 2: ESTRUTURA GERAL
// -------------------------------------------------------------------------

@ControllerAdvice
// ANOTAÇÃO PRINCIPAL: Habilita esta classe a "ouvir" e interceptar exceções
// lançadas em QUALQUER @Controller da sua aplicação. Garante o tratamento de erros GLOBAL.
public class GlobalExceptionHandler {

    // BLOCÃO 3: TRATAMENTO DE ResourceNotFoundException (HTTP 404 - Não Encontrado)
    // -------------------------------------------------------------------------

    @ExceptionHandler(ResourceNotFoundException.class)
    // Mapeia: Executa este método sempre que uma ResourceNotFoundException for lançada.
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        // FUNÇÃO: Captura a exceção, pega a mensagem de erro que foi definida nela.

        // Retorna: Mensagem da exceção com o Status HTTP 404 (NOT_FOUND).
        return new ResponseEntity<>(resourceNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    // BLOCÃO 4: TRATAMENTO DE ConflictException (HTTP 409 - Conflito)
    // -------------------------------------------------------------------------

    @ExceptionHandler(ConflictException.class)
    // Mapeia: Executa este método quando uma ConflictException for lançada (ex: e-mail duplicado).
    public ResponseEntity<String> handlerConflictException(ConflictException conflictException) {
        // FUNÇÃO: Captura a exceção de Conflito.

        // Retorna: Mensagem da exceção com o Status HTTP 409 (CONFLICT).
        return new ResponseEntity<>(conflictException.getMessage(), HttpStatus.CONFLICT);
    }

    // BLOCÃO 5: TRATAMENTO DE UnauthorizedException (HTTP 401 - Não Autorizado)
    // -------------------------------------------------------------------------

    @ExceptionHandler(UnauthorizedException.class)
    // Mapeia: Executa este método quando uma UnauthorizedException for lançada (falha de JWT/permissão).
    public ResponseEntity<String> handlerUnauthorizedException(UnauthorizedException unauthorizedException) {
        // FUNÇÃO: Captura a exceção de Não Autorizado.

        // Retorna: Mensagem da exceção com o Status HTTP 401 (UNAUTHORIZED).
        return new ResponseEntity<>(unauthorizedException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // BLOCÃO 6: TRATAMENTO DE IllegalArgumentException (HTTP 400 - Requisição Inválida)
    // -------------------------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    // Mapeia: Executa este método quando uma IllegalArgumentException for lançada (geralmente por falha em validações de entrada, como o CEP).
    public ResponseEntity<String> handlerIllegalArgumentException (IllegalArgumentException illegalArgumentException) {
        // FUNÇÃO: Captura a exceção de Argumento Inválido (validação de formato).

        // Retorna: Mensagem da exceção com o Status HTTP 400 (BAD_REQUEST). Este é o código correto para falhas de validação de input.
        return new ResponseEntity<> (illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}