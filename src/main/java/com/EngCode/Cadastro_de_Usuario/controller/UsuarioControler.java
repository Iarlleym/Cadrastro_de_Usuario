package com.EngCode.Cadastro_de_Usuario.controller;

// BLOCÃO 1: IMPORTAÇÕES E FERRAMENTAS
// -------------------------------------------------------------------------

import com.EngCode.Cadastro_de_Usuario.business.UsuarioService; // Lógica de negócio (Service)
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO; // DTOs de dados de entrada/saída
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil; // Utilitário de JWT

// Importações do Spring para API REST e Segurança
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // Classe para encapsular a resposta HTTP (Status Code, Body)
import org.springframework.security.authentication.AuthenticationManager; // Gerenciador de Autenticação do Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Objeto para autenticar login/senha
import org.springframework.security.core.Authentication; // Objeto que representa o usuário autenticado
import org.springframework.web.bind.annotation.*; // Anotações REST (Controller, RequestMapping, PostMapping, etc.)

// BLOCÃO 2: ESTRUTURA E INJEÇÃO DE DEPENDÊNCIA
// -------------------------------------------------------------------------

@RestController
// Marca a classe como um Controller que lida com requisições REST (retorna JSON/XML).
@RequestMapping("/usuario")
// Define o caminho base para todos os endpoints desta classe (ex: /usuario/login).
@RequiredArgsConstructor
// Lombok: Gera o construtor necessário para a Injeção de Dependência dos campos 'final'.
public class UsuarioControler {

    // As dependências são injetadas automaticamente pelo Spring.
    private final UsuarioService usuarioService; // Coordena a lógica de negócio.
    private final AuthenticationManager authenticationManager; // Gerencia o processo de Login/Senha.
    private final JwtUtil jwtUtil; // Ferramenta para gerar o Token JWT.

    // BLOCÃO 3: ENDPOINTS DE CADASTRO E LOGIN (Públicos)
    // -------------------------------------------------------------------------

    @PostMapping
    // Mapeia requisições HTTP POST para o caminho base (/usuario). Usado para criar um novo recurso.
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        // @RequestBody: Pega o JSON do corpo da requisição e o converte em um objeto UsuarioDTO.

        // Chama o Service para executar a lógica de negócio (validação, criptografia, persistência).
        // ResponseEntity.ok() retorna o Status HTTP 200 (OK) junto com o DTO do usuário criado.
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    // Mapeia requisições HTTP POST para o caminho /usuario/login.
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        // Cria o objeto necessário para o Spring Security verificar as credenciais.
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());

        // authenticationManager.authenticate: Tenta autenticar. Se falhar, lança exceção
        // (que é capturada pelo Spring Security e retorna erro 401).
        Authentication authentication = authenticationManager.authenticate(token);

        // Gera o Token JWT usando o e-mail (ou username) do usuário autenticado.
        // O prefixo "Bearer " é o padrão esperado pelos clientes HTTP.
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    // BLOCÃO 4: ENDPOINTS DE CONSULTA E REMOÇÃO (Geralmente Protegidos)
    // -------------------------------------------------------------------------

    @GetMapping
    // Mapeia requisições HTTP GET para o caminho base (/usuario). Usado para buscar recursos.
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        // @RequestParam("email"): Pega o valor da URL (ex: /usuario?email=teste@mail.com).

        // Chama o Service e retorna o DTO encontrado. Se o Service lançar 404, o Spring trata.
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    // Mapeia requisições HTTP DELETE para /usuario/{email}. Usado para remover recursos.
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        // @PathVariable: Pega o valor diretamente da URL (a parte {email}).

        // Chama o Service para executar a remoção no banco.
        usuarioService.deletaUsuarioPorEmail(email);

        // ResponseEntity.ok().build() retorna o Status HTTP 200 (OK) sem corpo (Void).
        return ResponseEntity.ok().build();
    }

    // BLOCÃO 5: ENDPOINTS DE ATUALIZAÇÃO (PUT/PATCH)
    // -------------------------------------------------------------------------

    @PutMapping
    // Mapeia requisições HTTP PUT para /usuario. Usado para atualizar o recurso principal.
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(
            @RequestBody UsuarioDTO usuarioDTO, // Novos dados do usuário
            @RequestHeader("Authorization") String token) {
        // @RequestHeader: Captura o valor do cabeçalho "Authorization" (onde o JWT está).

        // Chama o Service, passando o token (para identificar o usuário) e os dados de atualização.
        return ResponseEntity.ok(usuarioService.atualizaDaddosUsuario(token, usuarioDTO));
    }

    @PutMapping("/endereco")
    // Mapeia PUT /usuario/endereco. Atualiza um recurso aninhado específico.
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO enderecoDTO,
            @RequestParam("id") Long id) { // ID do endereço a ser atualizado (passado na URL).

        // Passa o ID e os novos dados para o Service.
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }

    @PutMapping("/telefone")
    // Mapeia PUT /usuario/telefone. Atualiza um recurso aninhado específico.
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO telefoneDTO,
            @RequestParam("id") Long id) { // ID do telefone a ser atualizado.

        // Passa o ID e os novos dados para o Service.
        return ResponseEntity.ok(usuarioService.atualizaTelefones(id, telefoneDTO));
    }

    // BLOCÃO 6: ENDPOINTS DE CRIAÇÃO DE RECURSOS ANINHADOS (Após Login)
    // -------------------------------------------------------------------------

    @PostMapping("/endereco")
    // Mapeia POST /usuario/endereco. Adiciona um novo endereço ao usuário LOGADO.
    public ResponseEntity<EnderecoDTO> cadastraEndereco(
            @RequestBody EnderecoDTO enderecoDTO,
            @RequestHeader("Authorization") String token) {
        // Precisa do Token para saber QUEM é o usuário que está adicionando o endereço.

        // Chama o Service que irá extrair o ID do usuário a partir do token e salvar.
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, enderecoDTO));
    }


    @PostMapping("/telefone")
    // Mapeia POST /usuario/telefone. Adiciona um novo telefone ao usuário LOGADO.
    public ResponseEntity<TelefoneDTO> cadastraTelefone(
            @RequestBody TelefoneDTO telefoneDTO,
            @RequestHeader("Authorization") String token) {
        // Recebe o token JWT para identificar o proprietário do telefone.

        // Chama o Service para salvar o telefone associado ao usuário.
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, telefoneDTO));
    }
}