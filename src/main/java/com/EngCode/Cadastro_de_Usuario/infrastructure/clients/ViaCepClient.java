package com.EngCode.Cadastro_de_Usuario.infrastructure.clients;

// Importa a classe DTO que será o formato de retorno da API ViaCEP.
// OBS: Assumimos que a classe ViaCepDTO (o retorno JSON da API) existe em algum lugar do projeto.
// import com.EngCode.Cadastro_de_Usuario.infrastructure.clients.ViaCepDTO;

// Importações do Feign Client e Spring Web

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 🔹 Interface responsável por fazer a comunicação com a API externa ViaCEP
 * usando o Feign Client.
 *
 * FUNÇÃO: Simplificar a chamada HTTP para o serviço de consulta de CEP,
 * tratando-a como uma simples chamada de método Java.
 */

// BLOCÃO 1: CONFIGURAÇÃO DO FEIGN CLIENT PARA API EXTERNA
// -------------------------------------------------------------------------
@FeignClient(name = "via-cep", url = "${viacep.url}")
// ANOTAÇÃO FEIGN: Marca esta interface como um cliente REST.
// name = "via-cep": Nome lógico do serviço dentro do Spring.
// url = "${viacep.url}": Endereço base da API ViaCEP, puxado do application.properties
// (ex: viacep.url=https://viacep.com.br/ws).
public interface ViaCepClient {
    // Feign Clients são sempre interfaces.

    // BLOCÃO 2: MÉTODO DE BUSCA DE ENDEREÇO
    // -------------------------------------------------------------------------

    /**
     * 🔹 Método responsável por buscar os dados de um endereço a partir do CEP informado.
     *
     * Exemplo de chamada HTTP que o Feign gera:
     * GET https://viacep.com.br/ws/{cep}/json/
     *
     * @param cep - o CEP a ser consultado.
     * @return objeto ViaCepDTO com todos os dados do endereço (rua, cidade, estado, etc.).
     */
    @GetMapping("/ws/{cep}/json/")
    // Mapeia o método Java para uma requisição HTTP GET. O {cep} na URL é substituído pelo @PathVariable.
    ViaCepDTO buscarDadosDeEndereco(
            @PathVariable("cep") String cep          // @PathVariable: Indica que o valor de 'cep' deve ser inserido na URL.
            // NOTA: Os parâmetros 'email' e 'token' do comentário foram removidos
            // e substituídos pelo CEP, que é o parâmetro correto para esta API.
    );
}