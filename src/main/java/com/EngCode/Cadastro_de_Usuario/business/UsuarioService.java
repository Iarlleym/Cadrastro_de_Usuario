package com.EngCode.Cadastro_de_Usuario.business;

// BLOCÃO 1: IMPORTAÇÕES DE FERRAMENTAS E DEPENDÊNCIAS
// -------------------------------------------------------------------------

// Classes da própria camada de negócio (Converter e DTOs)
import com.EngCode.Cadastro_de_Usuario.business.converter.UsuarioConverter;
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;

// Classes da camada de Infraestrutura (Entities e Repositories)
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Endereco;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Telefone;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;
import com.EngCode.Cadastro_de_Usuario.infrastructure.repository.EnderecoRepository;
import com.EngCode.Cadastro_de_Usuario.infrastructure.repository.TelefoneRepository;
import com.EngCode.Cadastro_de_Usuario.infrastructure.repository.UsuarioRepository;

// Exceções personalizadas e Utilitários de Segurança
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ConflictException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil;

// Anotações e classes do Spring
import lombok.RequiredArgsConstructor; // Lombok para injeção de dependência no construtor
import org.springframework.security.crypto.password.PasswordEncoder; // Interface para criptografia de senha
import org.springframework.stereotype.Service; // Anotação que marca a classe como Service

@Service
// Marca a classe como um componente de Serviço (camada de lógica de negócio) gerenciado pelo Spring.
@RequiredArgsConstructor
// Lombok: Gera um construtor com argumentos obrigatórios (para todas as variáveis 'final' abaixo).
public class UsuarioService {

    // BLOCÃO 2: INJEÇÃO DE DEPENDÊNCIAS (As ferramentas de trabalho do Service)
    // -------------------------------------------------------------------------
    private final UsuarioRepository usuarioRepository; // Acesso ao banco de dados (Entidade Usuario)
    private final UsuarioConverter usuarioConverter; // Ferramenta para mapear DTOs para Entities
    private final PasswordEncoder passwordEncoder; // Ferramenta para criptografar senhas
    private final JwtUtil jwtUtil; // Utilitário para manipulação de Tokens JWT
    private final EnderecoRepository enderecoRepository; // Acesso ao banco de dados (Entidade Endereco)
    private final TelefoneRepository telefoneRepository; // Acesso ao banco de dados (Entidade Telefone)

    /**
     * MÉTODO: salvaUsuario(UsuarioDTO)
     * FUNÇÃO: Lógica principal para cadastrar um novo usuário.
     * GARANTE: Validação de e-mail e criptografia de senha.
     */
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        // Validação: Lança ConflictException (HTTP 409) se o e-mail já existir.
        emailExiste(usuarioDTO.getEmail());

        // Segurança: Criptografa a senha antes de passar para a Entity.
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        // Mapeamento: Converte DTO (dados de entrada) para Entity (formato do banco).
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        // Persistência: Salva a nova Entity no banco de dados.
        usuario = usuarioRepository.save(usuario);

        // Retorno: Converte a Entity salva (agora com ID) de volta para DTO para a resposta da API.
        return usuarioConverter.paraUsuarioDTO(usuario);
    }


    /**
     * MÉTODO: emailExiste(String)
     * FUNÇÃO: Valida se um e-mail já existe no banco, lançando uma exceção se for duplicado.
     * CONCEITO: Garantia de Integridade de Dados.
     */
    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);

            // Se o repository retornar TRUE, lançamos uma exceção de Conflito.
            if (existe){
                throw new ConflictException("E-mail já cadastrado." + email);
            }

        } catch (ConflictException e) {
            // Garante que o erro original seja preservado no log (rastreabilidade).
            throw new ConflictException("E-mail já cadastrado.", e.getCause());
        }
    }

    /**
     * MÉTODO: verificaEmailExistente(String)
     * FUNÇÃO: Executa a consulta real no banco usando o Repository.
     * CONCEITO: Abstração da lógica de consulta.
     */
    public boolean verificaEmailExistente(String email) {
        // Usa o método de consulta derivado do Spring Data JPA.
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * MÉTODO: buscarUsuarioPorEmail(String)
     * FUNÇÃO: Busca um usuário por e-mail e retorna o DTO correspondente.
     * CONCEITO: Uso de Optional e orElseThrow para tratamento de "não encontrado".
     */
    public UsuarioDTO buscarUsuarioPorEmail (String email) {
        try {
            // Tenta buscar. Se o Optional estiver vazio, lança a exceção ResourceNotFound.
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("E-mail não encontrado: " + email)));

        } catch (ResourceNotFoundException e) {
            // Relança a exceção para que o Controller possa tratá-la e retornar o HTTP 404.
            throw new ResourceNotFoundException("E-mail não encontrado:" + email);
        }
    }

    /**
     * MÉTODO: deletaUsuarioPorEmail(String)
     * FUNÇÃO: Remove um usuário do banco.
     */
    public void deletaUsuarioPorEmail (String email) { // Void pois não há retorno de dados.
        // O Repository faz a remoção. O método no Repository exige @Transactional.
        usuarioRepository.deleteByEmail(email);
    }

    /**
     * MÉTODO: atualizaDaddosUsuario(String, UsuarioDTO)
     * FUNÇÃO: Atualiza os dados do usuário autenticado (PATCH).
     * SEGURANÇA: Usa o JWT para identificar o usuário.
     */
    public UsuarioDTO atualizaDaddosUsuario (String token, UsuarioDTO usuarioDTO) {

        // 1. Extrai o e-mail (identidade) do usuário logado a partir do Token.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // 2. Trata a senha: Se foi enviada, criptografa; se não, passa 'null' para manter a antiga.
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        // 3. Busca a Entity existente para ter os dados atuais.
        Usuario ususarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException ("E-mail não Localizado."));

        // 4. Mesclagem (PATCH): Usa o Converter para criar uma nova Entity mesclando novos dados e mantendo os antigos (se vieram null).
        Usuario usuario = usuarioConverter.updateDeUsuario(usuarioDTO, ususarioEntity);

        // 5. Salva a nova Entity mesclada e retorna o DTO.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    /**
     * MÉTODO: atualizaEndereco(Long, EnderecoDTO)
     * FUNÇÃO: Atualiza um endereço específico pelo ID (PATCH).
     */
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        // 1. Busca o Endereco existente pelo ID.
        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado: " + idEndereco));

        // 2. Mesclagem: Usa o Converter para aplicar o PATCH.
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);

        // 3. Salva e retorna o DTO.
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    /**
     * MÉTODO: atualizaTelefones(Long, TelefoneDTO)
     * FUNÇÃO: Atualiza um telefone específico pelo ID (PATCH).
     */
    public TelefoneDTO atualizaTelefones(Long idTelefone, TelefoneDTO telefoneDTO) {
        // 1. Busca o Telefone existente pelo ID.
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado:" + idTelefone));

        // 2. Mesclagem: Usa o Converter para aplicar o PATCH.
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);

        // 3. Salva e retorna o DTO.
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    /**
     * MÉTODO: cadastraEndereco(String, EnderecoDTO)
     * FUNÇÃO: Adiciona um novo endereço ao usuário logado (associação).
     * SEGURANÇA: Usa o Token para garantir que o endereço seja adicionado ao usuário correto.
     */
    public EnderecoDTO cadastraEndereco (String token, EnderecoDTO enderecoDTO) {

        // 1. Extrai a identidade do usuário logado.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // 2. Busca a Entity do usuário.
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não encontrado: " + email));

        // 3. Conversão e Associação: Converte DTO para Entity e insere a Chave Estrangeira (FK) do Usuário.
        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());

        // 4. Salva o novo Endereço.
        Endereco enderecoEntity = enderecoRepository.save(endereco);

        // 5. Retorna o DTO do novo Endereço.
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    /**
     * MÉTODO: cadastraTelefone(String, TelefoneDTO)
     * FUNÇÃO: Adiciona um novo telefone ao usuário logado.
     * SEGURANÇA: Usa o Token para obter o ID do usuário.
     */
    public TelefoneDTO cadastraTelefone (String token, TelefoneDTO telefoneDTO) {

        // 1. Extrai a identidade do usuário logado.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // 2. Busca a Entity do usuário.
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não encontrado: " + email));

        // 3. Conversão e Associação: Converte DTO para Entity e insere a Chave Estrangeira (FK) do Usuário.
        Telefone telefone = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());

        // 4. Salva o novo Telefone.
        Telefone telefoneEntity = telefoneRepository.save(telefone);

        // 5. Retorna o DTO do novo Telefone.
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }
}