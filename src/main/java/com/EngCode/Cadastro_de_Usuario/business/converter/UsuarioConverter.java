package com.EngCode.Cadastro_de_Usuario.business.converter;

// Importamos os DTOs (objetos que vêm da camada de negócio/API)
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;

// Importamos as Entities (objetos que representam tabelas do banco de dados)
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Endereco;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Telefone;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component // Diz para o Spring gerenciar essa classe (poder injetar em Services, por exemplo)
public class UsuarioConverter {

    /**
     * Converte um UsuarioDTO (vindo da API ou camada de serviço)
     * para um Usuario (Entity que será persistida no banco).
     *
     * - Usamos o @Builder do Lombok para criar a Entity.
     * - Aqui já convertemos também a lista de endereços.
     * - Obs: A senha normalmente deve ser encriptada antes de salvar.
     */
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())               // pega nome do DTO e joga na Entity
                .email(usuarioDTO.getEmail())             // pega email
                .senha(usuarioDTO.getSenha())             // pega senha (ideal aplicar encoder)
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos())) // converte lista de endereços
                .telefones(paraListaTelefone(usuarioDTO.getTelefones())) // converte lista de tefones
                // ⚠ Aqui você poderia também adicionar telefones se quiser
                // .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    /**
     * Converte uma lista de EnderecoDTO em uma lista de Endereco (Entity).
     *
     * Obs: poderia ser feito com stream (map), mas aqui usamos for para ficar mais claro.
     */
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {
        List<Endereco> enderecos = new ArrayList<>();
        for (EnderecoDTO enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;
        // Outra forma mais curta (Java 16+):
        // return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    /**
     * Converte um único EnderecoDTO em Endereco (Entity).
     *
     * - Usamos o builder para preencher cada campo.
     * - O ideal é que cada campo do DTO corresponda a um campo da Entity.
     */
    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    /**
     * Converte uma lista de TelefoneDTO em uma lista de Telefone (Entity).
     *
     * Aqui já usamos a forma compacta com streams.
     */
    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    /**
     * Converte um único TelefoneDTO em Telefone (Entity).
     */
    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    // =====================================================================
    // ENTITY → DTO
    // =====================================================================

    /**
     * Converte um Usuario (Entity vinda do banco)
     * para um UsuarioDTO (usado na API/camada de negócio).
     */
    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha()) // ⚠ normalmente não se retorna senha no DTO
                .enderecos(paraListaEnderecoDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    /** Converte lista de Endereco (Entity) para lista de EnderecoDTO. */
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {
        List<EnderecoDTO> enderecosDTO = new ArrayList<>();
        for (Endereco endereco : enderecos) {
            enderecosDTO.add(paraEnderecoDTO(endereco));
        }
        return enderecosDTO;
    }

    /** Converte Endereco (Entity) em EnderecoDTO. */
    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    /** Converte lista de Telefone (Entity) para lista de TelefoneDTO. */
    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefones) {
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }

    /** Converte Telefone (Entity) em TelefoneDTO. */
    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    //Método para comparar os dados do usuario
    public Usuario updateDeUsuario (UsuarioDTO usuarioDTO, Usuario usuario) {
        // Cria um novo objeto Usuario com base nos dados recebidos.
        // Se o campo do DTO for diferente de null, pega ele.
        // Caso contrário, mantém o valor que já existia no banco.
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
                .id(usuario.getId()) // O ID nunca muda, é fixo.
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
                .enderecos(usuario.getEnderecos()) // Mantém os endereços já cadastrados
                .telefones(usuario.getTelefones()) // Mantém os telefones já cadastrados
                .build();
    }


}
