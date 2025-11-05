package com.EngCode.Cadastro_de_Usuario.controller;

// BLOCÃO 1: IMPORTAÇÕES E FERRAMENTAS
// -------------------------------------------------------------------------

import com.EngCode.Cadastro_de_Usuario.business.UsuarioService;
import com.EngCode.Cadastro_de_Usuario.business.ViaCepService; // Importação do novo serviço ViaCEP
import com.EngCode.Cadastro_de_Usuario.business.dto.EnderecoDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.TelefoneDTO;
import com.EngCode.Cadastro_de_Usuario.business.dto.UsuarioDTO;
import com.EngCode.Cadastro_de_Usuario.infrastructure.clients.ViaCepDTO; // DTO de retorno da ViaCEP
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.JwtUtil;
import com.EngCode.Cadastro_de_Usuario.infrastructure.security.SecurityConfig;

// Adição das importações do Swagger (OpenAPI 3)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


import org.springframework.web.bind.annotation.*;

// BLOCÃO 2: ESTRUTURA E INJEÇÃO DE DEPENDÊNCIA
// -------------------------------------------------------------------------

@RestController
// Marca a classe como um Controller que lida com requisições REST (retorna JSON/XML).
@RequestMapping("/usuario")
// Define o caminho base para todos os endpoints desta classe (ex: /usuario/login).
@RequiredArgsConstructor
// Lombok: Gera o construtor necessário para a Injeção de Dependência dos campos 'final'.
@Tag(name = "Usuário", description = "Endpoints de Cadastro, Login e Gerenciamento de Usuários")
// SWAGGER: Define a tag principal para agrupar as rotas.
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
// SWAGGER: Indica globalmente que a maioria das rotas requer o Token JWT.
public class UsuarioControler {

    // As dependências são injetadas automaticamente pelo Spring.
    private final UsuarioService usuarioService; // Coordena a lógica de negócio.
    private final AuthenticationManager authenticationManager; // Gerencia o processo de Login/Senha.
    private final JwtUtil jwtUtil; // Ferramenta para gerar o Token JWT.
    private final ViaCepService viaCepService; // Novo serviço injetado para a consulta de CEP.

    // BLOCÃO 3: ENDPOINTS DE CADASTRO E LOGIN (Públicos)
    // -------------------------------------------------------------------------

    @PostMapping
    // SWAGGER: Documentação do endpoint de Cadastro.
    @Operation(summary = "Cadastrar Novo Usuário", description = "Cria um novo usuário na base de dados (Rota pública).")
    @ApiResponse(responseCode = "200", description = "Usuário Salvo Com Sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados Inválidos (JSON malformado).")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado (Conflito).")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        // @RequestBody: Pega o JSON do corpo da requisição e o converte em um objeto UsuarioDTO.
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    // SWAGGER: Documentação do endpoint de Login.
    @Operation(summary = "Login de Usuário", description = "Autentica o usuário e retorna o Token JWT.")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido. Retorna o Token JWT no formato 'Bearer ...'.")
    @ApiResponse(responseCode = "401", description = "Credenciais Inválidas (Usuário/Senha incorretos).")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        // Cria o objeto necessário para o Spring Security verificar as credenciais.
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());

        // authenticationManager.authenticate: Tenta autenticar.
        Authentication authentication = authenticationManager.authenticate(token);

        // Gera o Token JWT usando o e-mail (ou username) do usuário autenticado.
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    // BLOCÃO 4: ENDPOINTS DE CONSULTA E REMOÇÃO (Protegidos)
    // -------------------------------------------------------------------------

    @GetMapping
    // SWAGGER: Documentação do endpoint de Busca por E-mail.
    @Operation(summary = "Buscar Usuário por E-mail", description = "Retorna os dados do usuário especificado. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Usuário Encontrado.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Ausente ou Inválido).")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        // @RequestParam("email"): Pega o valor da URL.
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    // SWAGGER: Documentação do endpoint de Deleção.
    @Operation(summary = "Deletar Usuário por E-mail", description = "Remove o registro do usuário especificado. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Usuário Deletado Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        // @PathVariable: Pega o valor diretamente da URL.

        usuarioService.deletaUsuarioPorEmail(email);

        return ResponseEntity.ok().build();
    }

    // BLOCÃO 5: ENDPOINTS DE ATUALIZAÇÃO (Protegidos)
    // -------------------------------------------------------------------------

    @PutMapping
    // SWAGGER: Documentação do endpoint de Atualização Principal.
    @Operation(summary = "Atualizar Dados Principais do Usuário", description = "Atualiza nome, e-mail ou senha do usuário logado. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Dados Atualizados Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(
            @RequestBody UsuarioDTO usuarioDTO, // Novos dados do usuário
            @RequestHeader("Authorization") String token) {
        // @RequestHeader: Captura o Token JWT.

        return ResponseEntity.ok(usuarioService.atualizaDaddosUsuario(token, usuarioDTO));
    }

    @PutMapping("/endereco")
    // SWAGGER: Documentação do endpoint de Atualização de Endereço.
    @Operation(summary = "Atualizar Endereço Existente", description = "Atualiza um endereço específico do usuário logado pelo ID do endereço. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Endereço Atualizado Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO enderecoDTO,
            @RequestParam("id") Long id) { // ID do endereço a ser atualizado.

        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }

    @PutMapping("/telefone")
    // SWAGGER: Documentação do endpoint de Atualização de Telefone.
    @Operation(summary = "Atualizar Telefone Existente", description = "Atualiza um telefone específico do usuário logado pelo ID do telefone. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Telefone Atualizado Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "404", description = "Telefone não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO telefoneDTO,
            @RequestParam("id") Long id) { // ID do telefone a ser atualizado.

        return ResponseEntity.ok(usuarioService.atualizaTelefones(id, telefoneDTO));
    }

    // BLOCÃO 6: ENDPOINTS DE CRIAÇÃO DE RECURSOS ANINHADOS (Protegidos)
    // -------------------------------------------------------------------------

    @PostMapping("/endereco")
    // SWAGGER: Documentação do endpoint de Criação de Endereço Aninhado.
    @Operation(summary = "Adicionar Novo Endereço", description = "Adiciona um novo endereço à conta do usuário logado. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Endereço Cadastrado Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(
            @RequestBody EnderecoDTO enderecoDTO,
            @RequestHeader("Authorization") String token) {
        // @RequestHeader("Authorization") String token: Captura o Token JWT.

        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, enderecoDTO));
    }


    @PostMapping("/telefone")
    // SWAGGER: Documentação do endpoint de Criação de Telefone Aninhado.
    @Operation(summary = "Adicionar Novo Telefone", description = "Adiciona um novo telefone à conta do usuário logado. Requer Token.")
    @ApiResponse(responseCode = "200", description = "Telefone Cadastrado Com Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não Autorizado (Token Inválido).")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(
            @RequestBody TelefoneDTO telefoneDTO,
            @RequestHeader("Authorization") String token) {
        // Recebe o token JWT para identificar o proprietário do telefone.

        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, telefoneDTO));
    }

    // BLOCÃO 7: ENDPOINT DE CONSUMO DE API EXTERNA (ViaCEP)
    // -------------------------------------------------------------------------

    @GetMapping ("/endereco/{cep}")
    // SWAGGER: Documentação do endpoint de consulta ViaCEP.
    @Operation(summary = "Consultar Endereço por CEP", description = "Busca e retorna dados de endereço utilizando a API ViaCEP. Não requer autenticação.")
    @ApiResponse(responseCode = "200", description = "Endereço encontrado e retornado com sucesso.")
    @ApiResponse(responseCode = "400", description = "CEP inválido (Formato incorreto ou caracteres ilegais).")
    @ApiResponse(responseCode = "404", description = "CEP não encontrado na base de dados da ViaCEP.")
    @ApiResponse(responseCode = "500", description = "Erro de Servidor (Falha na comunicação com a ViaCEP).")
    public ResponseEntity <ViaCepDTO> buscarDadosDeCep (@PathVariable ("cep") String cep) {
        // @PathVariable ("cep"): Pega a variável de caminho (o CEP) da URL.

        // Chama o serviço ViaCEP para executar a validação e a chamada Feign.
        return ResponseEntity.ok(viaCepService.buscarDadosDeEndereco(cep));
    }
}