package com.EngCode.Cadastro_de_Usuario.controller;

import com.EngCode.Cadastro_de_Usuario.business.UsuarioService;
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioControler {

    //Injeta a dependencia de service para usar os metodos dela.
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));//Verifica e-mail e senha

        return "Bearer " + jwtUtil.generateToken(authentication.getName()); //Recebe o email e gera um token.
    }

    //Metodo para buscar o usuário por e-mail
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    //Metodo para deletar o usuario por email - Usa Void pq não tem retorno apenas se deu certo ou não.
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) { //OBS o nome dentro das chaves {/email} tem que ser igual ao nome da variável.
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    //Método para atualizar dados do usuario já cadastrado.
    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(@RequestBody UsuarioDTO usuarioDTO, @RequestHeader("Authorization") String token) {
        // Recebe um objeto UsuarioDTO do corpo da requisição
        // e o token do usuário no cabeçalho (Authorization).
        // Em seguida, chama o service que vai tratar a atualização
        // e retorna a resposta já no formato ResponseEntity.
        return ResponseEntity.ok(usuarioService.atualizaDaddosUsuario(token, usuarioDTO));
    }

    // Método responsável por atualizar os dados de endereço de um usuário já existente.
    // Ele recebe o ID do endereço e as novas informações enviadas no corpo da requisição.
    // Em seguida, encaminha esses dados para o Service, que faz a atualização no banco
    // e retorna o endereço atualizado em formato DTO.
    @PutMapping("/endereco") // Define o endpoint PUT /endereco
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO enderecoDTO, // Corpo da requisição com os novos dados do endereço
            @RequestParam("id") Long id) {        // Parâmetro da URL com o ID do endereço a ser atualizado

        // Chama o método da camada de serviço que realiza a atualização no banco de dados
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }


    // Método responsável por atualizar os dados de telefone de um usuário já existente.
    // Assim como o método de endereço, ele recebe o ID e os novos dados enviados no corpo.
    // Depois, encaminha tudo para o Service, que trata a atualização no banco e retorna
    // o telefone atualizado em formato DTO.
    @PutMapping("/telefone") // Define o endpoint PUT /telefone
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO telefoneDTO, // Corpo da requisição com os novos dados do telefone
            @RequestParam("id") Long id) {        // Parâmetro da URL com o ID do telefone a ser atualizado

        // Chama o método da camada de serviço que realiza a atualização no banco de dados
        return ResponseEntity.ok(usuarioService.atualizaTelefones(id, telefoneDTO));
    }


}
