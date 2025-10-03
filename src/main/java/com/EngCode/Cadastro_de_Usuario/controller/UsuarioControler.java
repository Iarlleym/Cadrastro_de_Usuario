package com.EngCode.Cadastro_de_Usuario.controller;

import com.EngCode.Cadastro_de_Usuario.business.UsuarioService;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.entity.Usuario;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/usuario")
@RequiredArgsConstructor
public class UsuarioControler {

    //Injeta a dependencia de service para usar os metodos dela.
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario (@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login (@RequestBody UsuarioDTO usuarioDTO){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));//Verifica e-mail e senha

        return "Bearer " + jwtUtil.generateToken(authentication.getName()); //Recebe o email e gera um token.
    }

    //Metodo para buscar o usuário por e-mail
    @GetMapping
    public ResponseEntity<Usuario> buscaUsuarioPorEmail (@RequestParam ("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    //Metodo para deletar o usuario por email - Usa Void pq não tem retorno apenas se deu certo ou não.
    @DeleteMapping ("/{email}")
    public ResponseEntity <Void> deletaUsuarioPorEmail (@PathVariable String email) { //OBS o nome dentro das chaves {/email} tem que ser igual ao nome da variável.
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }


}
