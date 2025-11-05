package com.EngCode.Cadastro_de_Usuario.infrastructure.security;

// BLOCÃO 1: IMPORTAÇÕES ESSENCIAIS
// -------------------------------------------------------------------------
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType; // Tipo de esquema de segurança (para Swagger)
import io.swagger.v3.oas.annotations.security.SecurityScheme; // Anotação para definir o esquema de segurança (para Swagger)
import org.springframework.beans.factory.annotation.Autowired; // Injeção de dependência via construtor
import org.springframework.context.annotation.Bean; // Marca o método como um bean gerenciado pelo Spring
import org.springframework.context.annotation.Configuration; // Marca a classe como uma fonte de configuração
import org.springframework.http.HttpMethod; // Enum para métodos HTTP (POST, GET, etc.)
import org.springframework.security.authentication.AuthenticationManager; // Gerenciador de autenticação
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Configuração do gerenciador
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Construtor de regras de segurança HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita o Spring Security
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Para desativar o CSRF
import org.springframework.security.config.http.SessionCreationPolicy; // Política de criação de sessão
import org.springframework.security.core.userdetails.UserDetailsService; // Interface para carregar dados do usuário
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementação de criptografia
import org.springframework.security.crypto.password.PasswordEncoder; // Interface de criptografia
import org.springframework.security.web.SecurityFilterChain; // A cadeia de filtros de segurança
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro de autenticação padrão do Spring

// BLOCÃO 2: CONFIGURAÇÃO DA CLASSE E SWAGGER
// -------------------------------------------------------------------------
@Configuration
// ANOTAÇÃO SPRING: Indica que esta classe contém definições de beans (@Bean) de configuração.
@EnableWebSecurity
// HABILITA SEGURANÇA: Ativa o processamento de segurança do Spring Security.
@SecurityScheme(name = SecurityConfig.SECURITY_SCHEME, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
// SWAGGER/OPENAPI: Define como o JWT deve ser inserido na documentação.
// name = "bearerAuth" (do campo SECURITY_SCHEME): Nome que será usado para referenciar o esquema de segurança (ex: na anotação @SecurityRequirement).
// type = SecuritySchemeType.HTTP e scheme = "bearer": Informa ao Swagger que este é um esquema de segurança baseado em token HTTP (Bearer Token).
public class SecurityConfig {

    // Constante que define o nome do esquema de segurança para o Swagger.
    public static final String SECURITY_SCHEME = "bearerAuth";

    // Instâncias de JwtUtil e UserDetailsService injetadas pelo Spring
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor para injeção das dependências 'final'.
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // BLOCÃO 3: CADEIA DE FILTROS DE SEGURANÇA (O Coração da Configuração)
    // -------------------------------------------------------------------------

    @Bean
    // BEAN: Define a cadeia principal de filtros de segurança que o Spring usará.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Cria uma instância do nosso filtro personalizado.
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa a proteção CSRF (Cross-Site Request Forgery) porque não estamos usando sessões nem cookies (padrão em APIs RESTful).
                .authorizeHttpRequests(authorize -> authorize
                        // Permissões: Define quais rotas são públicas (permitAll()) e quais são privadas (authenticated()).

                        // Rotas Públicas: Permite acesso à documentação do Swagger.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "swagger-ui.html").permitAll()

                        // Rotas Públicas: Permite acesso aos endpoints de Login e Cadastro (POST /usuario).
                        .requestMatchers("/usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()

                        // Rotas Públicas: Permite acesso à consulta de CEP (Geralmente rota aberta para o front-end).
                        .requestMatchers(HttpMethod.GET, "/usuario/endereco/**").permitAll()

                        // Rotas Privadas (Padrão): Requer Token JWT para qualquer outra rota que comece com /usuario/.
                        .requestMatchers("/usuario/**").authenticated()

                        // Padrão Geral: Requer Token JWT para todas as requisições não mapeadas acima.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // CONFIGURAÇÃO CRÍTICA PARA JWT: Define a política de sessão como STATELESS.
                        // Significa que o servidor não criará ou usará sessões HTTP (cookies). A autenticação será feita apenas pelo Token JWT em cada requisição.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Adiciona nosso filtro JWT antes do filtro de autenticação padrão do Spring.
                // Isso garante que o Token seja validado antes que o Spring tente buscar o usuário no banco de dados.
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Retorna a configuração do filtro de segurança construída
        return http.build();
    }


    // BLOCÃO 4: DEFINIÇÃO DE BEANS DE CRIPTOGRAFIA E AUTENTICAÇÃO
    // -------------------------------------------------------------------------

    @Bean
    // BEAN: Configura o PasswordEncoder (Ferramenta de Criptografia).
    public PasswordEncoder passwordEncoder() {
        // FUNÇÃO: Define o algoritmo de criptografia. BCrypt é o padrão e mais seguro para senhas.
        return new BCryptPasswordEncoder();
    }

    @Bean
    // BEAN: Configura o AuthenticationManager (Gerenciador de Login).
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // FUNÇÃO: É o componente central que lida com o processo de autenticação (receber login/senha e validar).
        return authenticationConfiguration.getAuthenticationManager();
    }
}