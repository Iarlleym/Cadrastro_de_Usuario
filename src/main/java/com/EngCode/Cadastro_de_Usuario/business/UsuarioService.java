package com.EngCode.Cadastro_de_Usuario.business;

import com.EngCode.Cadastro_de_Usuario.business.converter.UsuarioConverter;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ConflictException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.repository.UsuarioRepository;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    //Injeção de Dependencias
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Salva um novo usuário no banco de dados.
     *
     * Fluxo resumido:
     * 1) Verifica se o e-mail já existe no banco (evita duplicidade).
     * 2) Criptografa a senha do usuário antes de salvar.
     * 3) Converte o DTO para a Entity para persistência.
     * 4) Salva a Entity no banco.
     * 5) Converte a Entity salva de volta para DTO e retorna.
     *
     * Esse método garante segurança (senha criptografada) e mantém a separação
     * entre camada de apresentação (DTO) e persistência (Entity).
     */
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail()); // Verifica duplicidade de e-mail
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha())); // Criptografa a senha
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO); // Converte DTO -> Entity
        usuario = usuarioRepository.save(usuario); // Salva no banco
        return usuarioConverter.paraUsuarioDTO(usuario); // Converte Entity -> DTO e retorna
    }


    // Método que verifica se um e-mail já existe no banco de dados.
    // Esse método é útil na hora de cadastrar um novo usuário,
    // para garantir que não haja duplicidade de e-mails.
    public void emailExiste(String email) {
        try {
            // Chama outro método que consulta o banco para saber se o e-mail já existe
            boolean existe = verificaEmailExistente(email);

            // Se já existe, lançamos uma exceção personalizada
            // ConflictException indica conflito de dados (HTTP 409)
            if (existe){
                throw new ConflictException("E-mail já cadastrado." + email);
            }

        } catch (ConflictException e) {
            // Capturamos a exceção e relançamos, incluindo a causa original.
            // Isso ajuda a manter rastreabilidade do erro.
            throw new ConflictException("E-mail já cadastrado.", e.getCause());
        }
    }

    // Método auxiliar que faz a verificação real no banco de dados.
    // Ele usa o repository para verificar se existe algum usuário com o e-mail informado.
    // Retorna true se existir, false se não existir.
    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    //Cria o método de busca por e-mail
    public Usuario buscarUsuarioPorEmail (String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("E-mail não encontrado." + email)); //Usa o orElseThrow para não usar um bloco try catch e usa a exception criada

    }

    //Cria método para deletar por email
    public void deletaUsuarioPorEmail (String email) { //Usa Void pq não tem retorno.
        usuarioRepository.deleteByEmail(email);

    }

    public UsuarioDTO atualizaDaddosUsuario (String token, UsuarioDTO usuarioDTO) {

        // Extrai o e-mail do usuário a partir do token JWT.
        // Isso evita que seja necessário enviar o e-mail na requisição.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Caso o usuário tenha informado uma nova senha,
        // ela é criptografada. Se não, permanece nula para ser tratada depois.
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        // Busca o usuário no banco de dados pelo e-mail obtido do token.
        // Se não encontrar, lança exceção personalizada de "não encontrado".
        Usuario ususarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException ("E-mail não Localizado."));

        // Faz a mesclagem dos dados:
        // os novos dados vindos do DTO substituem os antigos,
        // e os campos nulos mantêm os valores já salvos no banco.
        Usuario usuario = usuarioConverter.updateDeUsuario(usuarioDTO, ususarioEntity);

        // Salva o usuário atualizado no banco
        // e retorna o objeto convertido para DTO novamente.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }



}
