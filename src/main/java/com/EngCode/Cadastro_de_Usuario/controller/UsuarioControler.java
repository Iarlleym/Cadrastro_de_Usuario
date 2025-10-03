package com.EngCode.Cadastro_de_Usuario.controller;

import com.EngCode.Cadastro_de_Usuario.business.UsuarioService;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/usuario")
@RequiredArgsConstructor
public class UsuarioControler {

    //Injeta a dependencia de service para usar os metodos dela.
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario (@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }


}
