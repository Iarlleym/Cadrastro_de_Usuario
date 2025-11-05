package com.EngCode.Cadastro_de_Usuario.business;

// BLOCÃO 1: IMPORTAÇÕES E FERRAMENTAS
// -------------------------------------------------------------------------

// O Feign Client: a interface que consome a API externa ViaCEP.
import com.EngCode.Cadastro_de_Usuario.infrastructure.clients.ViaCepClient;
// DTO de retorno: O formato de dados esperado da API ViaCEP.
import com.EngCode.Cadastro_de_Usuario.infrastructure.clients.ViaCepDTO;
// Sua exceção personalizada para argumentos inválidos (mapeada para HTTP 400).
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.IllegalArgumentException;

// Importações do Lombok e Spring
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Objects; // Utilitário para lidar com objetos.

@Service
// ANOTAÇÃO SPRING: Marca a classe como um componente de Serviço, contendo a lógica de negócio.
@RequiredArgsConstructor
// LOMBOK: Gera um construtor que injeta o 'ViaCepClient'.
public class ViaCepService {

    // BLOCÃO 2: INJEÇÃO DE DEPENDÊNCIA
    // -------------------------------------------------------------------------
    private final ViaCepClient viaCepClient;
    // VARIÁVEL CRÍTICA: O Feign Client, que encapsula a chamada HTTP externa.

    /**
     * MÉTODO: buscarDadosDeEndereco(String cep)
     * FUNÇÃO: Lógica de Orquestração. Recebe o CEP, valida o formato e faz a chamada HTTP.
     */
    public ViaCepDTO buscarDadosDeEndereco (String cep) {

        // ⭐️ Passo 1: VALIDAÇÃO E FORMATAÇÃO
        // Chama o método privado 'procesarCep' para limpar e validar o formato do CEP.
        // Se o formato estiver errado (letras, tamanho), a IllegalArgumentException é lançada aqui.
        String cepValidado = procesarCep(cep);

        // Passo 2: CONSUMO DE API EXTERNA
        // O Service usa o CEP limpo e validado para fazer a chamada Feign.
        // O retorno da chamada é o DTO preenchido pela API ViaCEP.
        return viaCepClient.buscarDadosDeEndereco(cepValidado);
    }

    /**
     * MÉTODO: procesarCep(String cep)
     * FUNÇÃO: Valida e formata a string do CEP (Regras de Entrada).
     * NATUREZA: Lógica de Validação Interna.
     */
    private String procesarCep (String cep) {

        // LIMPEZA: Remove espaços em branco e hífens.
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        // VALIDAÇÃO: Verifica se contém apenas dígitos E se tem o tamanho exato de 8 caracteres.
        if (!cepFormatado.matches("\\d+")
                || !Objects.equals(cepFormatado.length(), 8)) {

            // Lançamento de Exceção: Isso é capturado pelo GlobalExceptionHandler (que mapeia para HTTP 400).
            throw new IllegalArgumentException("O CEP digitado contém caracteres inválidos, favor Verificar.");
        }

        // Retorna o CEP limpo e validado.
        return cepFormatado;
    }

}