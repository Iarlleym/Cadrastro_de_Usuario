package com.EngCode.Cadastro_de_Usuario.business;

import com.EngCode.Cadastro_de_Usuario.business.converter.UsuarioConverter;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;
import com.EngCode.Cadastro_de_Usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    /**
     * Método para salvar um usuário no banco de dados.
     *
     * Fluxo do método:
     * 1) Recebe um UsuarioDTO (geralmente vindo de uma requisição da API).
     * 2) Converte o DTO para a Entity Usuario usando o UsuarioConverter.
     * 3) Salva a Entity no banco com o repository.
     * 4) Converte a Entity salva de volta para DTO e retorna.
     *
     * Esse padrão (DTO -> Entity -> salvar -> Entity -> DTO) mantém a separação
     * entre a camada de apresentação (API) e a camada de persistência (banco),
     * facilitando manutenção e testes.
     */

    public UsuarioDTO salvaUsuario (UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

}
