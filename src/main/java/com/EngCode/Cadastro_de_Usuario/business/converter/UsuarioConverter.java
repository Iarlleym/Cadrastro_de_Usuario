package com.EngCode.Cadastro_de_Usuario.business.converter;

// BLOCÃO 1: IMPORTAÇÕES E CONFIGURAÇÃO
// -------------------------------------------------------------------------

// Importa os DTOs (Objetos de Transferência de Dados) — O formato que a API usa para receber e enviar dados.
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;

// Importa as Entities (Entidades) — O formato que o banco de dados (JPA/Hibernate) usa.
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Endereco;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Telefone;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;

import org.springframework.stereotype.Component; // Importa a anotação @Component.

import java.util.ArrayList; // Importa classe para criar listas.
import java.util.List;      // Importa interface de Listas.

@Component
// Esta anotação diz ao Spring: "Gerencie este objeto. Ele pode ser injetado (usado)
// em outras classes, como a UsuarioService." É o princípio da Injeção de Dependência.
public class UsuarioConverter {

    // =====================================================================
    // DTO → ENTITY (CONVERSÃO DE ENTRADA: Da API para o Banco)
    // =====================================================================

    /**
     * MÉTODO: paraUsuario(UsuarioDTO)
     * FUNÇÃO: Converte o objeto de entrada da API (DTO) para o objeto de persistência (Entity).
     * CONCEITO: Uso do Padrão Builder (Lombok) para criar o objeto de forma segura e legível.
     */
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                // Conversão Aninhada: Chama o método auxiliar para converter a lista de endereços.
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                // Conversão Aninhada: Chama o método auxiliar para converter a lista de telefones.
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    /**
     * MÉTODO: paraListaEndereco(List<EnderecoDTO>)
     * FUNÇÃO: Transforma uma lista de DTOs de Endereço em uma lista de Entities.
     * CONCEITO: Coordenador do loop de conversão de listas.
     */
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {
        List<Endereco> enderecos = new ArrayList<>();
        // O laço 'for-each' percorre cada EnderecoDTO na lista de entrada.
        for (EnderecoDTO enderecoDTO : enderecoDTOS) {
            // Chama o método paraEndereco para converter o item único e o adiciona à nova lista.
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;
    }

    /**
     * MÉTODO: paraEndereco(EnderecoDTO)
     * FUNÇÃO: Converte um único EnderecoDTO para a Endereco Entity.
     * CONCEITO: Mapeamento simples de item único.
     */
    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                // ... Mapeamento de todos os campos ...
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    /**
     * MÉTODO: paraListaTelefone(List<TelefoneDTO>)
     * FUNÇÃO: Converte uma lista de DTOs de Telefone em uma lista de Entities.
     * CONCEITO: Uso de Java Streams (forma moderna) para o loop de conversão.
     */
    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS) {
        // Usa o .stream().map() para aplicar o método paraTelefone a cada item da lista.
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    /**
     * MÉTODO: paraTelefone(TelefoneDTO)
     * FUNÇÃO: Converte um único TelefoneDTO para a Telefone Entity.
     */
    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    // =====================================================================
    // ENTITY → DTO (CONVERSÃO DE SAÍDA: Do Banco para a API)
    // =====================================================================

    /**
     * MÉTODO: paraUsuarioDTO(Usuario)
     * FUNÇÃO: Converte a Entity (que veio do banco) para o DTO (que será enviado como resposta da API).
     * CONCEITO: Garante que a resposta da API tenha o formato desejado, sem expor todos os dados do banco.
     */
    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        // ... (Uso do Builder para montar o DTO de saída)
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha()) // ⚠ Atenção: Senha é exposta aqui, o que não é ideal em produção.
                .enderecos(paraListaEnderecoDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    // ... (Métodos de Conversão de Saída para Endereço e Telefone - Lógica inversa) ...

    /** Converte lista de Endereco (Entity) para lista de EnderecoDTO. */
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {
        List<EnderecoDTO> enderecosDTO = new ArrayList<>();
        // Uso do for-each para converter cada Endereco Entity em DTO.
        for (Endereco endereco : enderecos) {
            enderecosDTO.add(paraEnderecoDTO(endereco));
        }
        return enderecosDTO;
    }

    /** Converte Endereco (Entity) em EnderecoDTO. Adiciona o ID para referência. */
    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .id(endereco.getId()) // Inclui o ID gerado pelo banco.
                .rua(endereco.getRua())
                // ... (Mapeamento dos campos) ...
                .build();
    }

    /** Converte lista de Telefone (Entity) para lista de TelefoneDTO. */
    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefones) {
        // Uso de Streams para conversão de lista.
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }

    /** Converte Telefone (Entity) em TelefoneDTO. Adiciona o ID para referência. */
    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId()) // Inclui o ID gerado pelo banco.
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    // =====================================================================
    // MÉTODOS DE ATUALIZAÇÃO (UPDATE)
    // =====================================================================

    /**
     * MÉTODO: updateDeUsuario(UsuarioDTO, Usuario)
     * FUNÇÃO: Cria uma nova Entity de Usuário para o update, aplicando a lógica de "se nulo, manter o valor antigo".
     * CONCEITO: Lógica de PATCH (atualização parcial). Garante que campos não enviados no DTO (se vierem como 'null')
     * não sobrescrevam dados existentes no banco.
     */
    public Usuario updateDeUsuario (UsuarioDTO usuarioDTO, Usuario usuario) {
        return Usuario.builder()
                // Operador Ternário: (Condição) ? (Se Verdadeiro) : (Se Falso)
                // Se o nome do DTO não for null, usa o novo nome. Senão, mantém o nome antigo (usuario.getNome()).
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
                .id(usuario.getId()) // O ID deve sempre ser mantido.
                // ... (Aplica a mesma lógica para senha e email) ...
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
                .enderecos(usuario.getEnderecos()) // Mantém as listas existentes.
                .telefones(usuario.getTelefones()) // Mantém as listas existentes.
                .build();
    }

    /**
     * MÉTODO: updateEndereco(EnderecoDTO, Endereco)
     * FUNÇÃO: Atualiza os dados de um Endereço, aplicando a lógica de atualização parcial (PATCH).
     */
    public Endereco updateEndereco(EnderecoDTO enderecoDTO, Endereco endereco) {
        return Endereco.builder()
                .id(endereco.getId()) // ID do endereço existente.
                // Aplica a lógica de "se nulo, manter o valor existente" para cada campo.
                .rua(enderecoDTO.getRua() != null ? enderecoDTO.getRua() : endereco.getRua())
                .estado(enderecoDTO.getEstado() != null ? enderecoDTO.getEstado() : endereco.getEstado())
                // ... (Lógica de PATCH para todos os campos) ...
                .build();
    }

    /**
     * MÉTODO: updateTelefone(TelefoneDTO, Telefone)
     * FUNÇÃO: Atualiza os dados de um Telefone, aplicando a lógica de atualização parcial (PATCH).
     */
    public Telefone updateTelefone(TelefoneDTO telefoneDTO, Telefone telefone) {
        return Telefone.builder()
                .id(telefone.getId())
                .ddd(telefoneDTO.getDdd() != null ? telefoneDTO.getDdd() : telefone.getDdd())
                .numero(telefoneDTO.getNumero() != null ? telefoneDTO.getNumero() : telefone.getNumero())
                .build();
    }

    // =====================================================================
    // MÉTODOS DE RELACIONAMENTO (USO AVANÇADO)
    // =====================================================================
    // Estes métodos parecem ser auxiliares para adicionar itens a um usuário existente,
    // onde o ID do usuário é passado separadamente.

    public Endereco paraEnderecoEntity (EnderecoDTO enderecoDTO, Long idUsuario) {
        return Endereco.builder()
                // ... (Mapeamento de campos) ...
                .usuario_id(idUsuario) // Associa o Endereço à chave estrangeira (FK) do Usuário.
                .build();
    }

    public Telefone paraTelefoneEntity (TelefoneDTO telefoneDTO, Long idTelefone) {
        return Telefone.builder()
                // ... (Mapeamento de campos) ...
                .usuario_id(idTelefone) // Associa o Telefone à chave estrangeira (FK) do Usuário.
                .build();
    }
}