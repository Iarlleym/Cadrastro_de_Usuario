## 🧑‍💻 Microserviço: Cadastro de Usuários

Este microserviço é a peça central para o **gerenciamento de usuários**, abrangendo cadastro, autenticação com JWT e a manutenção completa de informações pessoais, endereços e telefones. Ele serve como a base fundamental para outros serviços que necessitam de dados de usuário, como sistemas de agendamento ou notificação.

Além de suas funcionalidades essenciais, este serviço integra-se de forma inteligente com a **API externa ViaCEP** para auto-completar dados de endereço, otimizando a experiência do usuário e garantindo a qualidade das informações.

---

## 💻 Linguagem e Frameworks
<div style="display: inline_block"><br>
  <img align="center" alt="Java" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-plain.svg">
  <img align="center" alt="Spring Boot" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg">
  <img align="center" alt="Spring Security" height="30" width="40" src="https://img.shields.io/badge/Spring_Security-66BB6A?style=for-the-badge&logo=spring-security&logoColor=white">
  <img align="center" alt="OpenFeign" height="30" width="40" src="https://img.shields.io/badge/OpenFeign-007FFF?style=for-the-badge&logo=spring&logoColor=white">
  <img align="center" alt="PostgreSQL" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-plain.svg">
</div>

---

## 🔧 Funcionalidades

- Cadastro de novos usuários 📝
- Autenticação e login seguro com JWT 🔑
- Atualização completa de dados pessoais, endereços e telefones ✏️
- Deleção de usuários ❌
- **Consulta de Endereços via ViaCEP**:
    * Consumo da API externa ViaCEP para preenchimento automático de dados de endereço a partir de um CEP.
    * Validação de formato do CEP antes da consulta.
- Integração eficiente com outros microsserviços via REST API e **Spring Cloud OpenFeign** 🔗

---

## 📂 Estrutura do Projeto

- `controller`: Camada RESTful para os endpoints da API.
- `business`: Contém a lógica de negócio principal do serviço, incluindo o gerenciamento de usuários e a integração com a ViaCEP.
- `infrastructure/entity`: Definições das entidades persistidas no banco de dados.
- `infrastructure/repository`: Interfaces Spring Data JPA para acesso a dados.
- `infrastructure/security`: Configuração do Spring Security e gerenciamento de JWT.
- `infrastructure/clients`: **Definição do `FeignClient` para consumo da API ViaCEP.**
- `infrastructure/exceptions`: Definição das exceções personalizadas da aplicação.
- `business/converter`: Conversores entre DTOs e Entidades, utilizando o padrão `builder()`.

---

## ⚙️ Configurações Essenciais

Para o correto funcionamento do microsserviço, as seguintes variáveis de ambiente ou configurações no `application.properties`/`application.yml` são necessárias:

### Configuração do Banco de Dados PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update # ou validate, none, create-drop para desenvolvimento
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Configuração do JWT (para Spring Security)

```properties
jwt.secret=sua_chave_secreta_aqui_deve_ser_forte_e_longa # Use uma string longa e complexa
jwt.expiration=3600000 # Tempo de expiração do token em milissegundos (ex: 1 hora)
```

### Configuração do Feign Client (ViaCEP)

```properties
viacep.url=[https://viacep.com.br/](https://viacep.com.br/)
```

---

## 📌 Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Iarlleym/CadastroDeUsuarios.git](https://github.com/Iarlleym/CadastroDeUsuarios.git)
    cd CadastroDeUsuarios
    ```
2.  **Configure o Banco de Dados:**
      * Certifique-se de ter um servidor PostgreSQL rodando localmente (ou use um Docker).
      * Crie um banco de dados conforme a `spring.datasource.url` configurada (ex: `cadastro_db`).
      * Atualize as credenciais de banco de dados (`username` e `password`) no seu `application.properties`/`application.yml`.
3.  **Configure as Variáveis de Ambiente:**
      * Preencha o `jwt.secret` com uma chave secreta forte.
      * Verifique se a `viacep.url` está configurada corretamente (geralmente `https://viacep.com.br/`).
4.  **Execute a Aplicação:**
      * Você pode usar sua IDE (IntelliJ IDEA, Eclipse) ou via Maven:
        ```bash
        ./mvnw spring-boot:run
        ```

---

## 🚀 Acesso à Documentação da API (Swagger UI)

Após iniciar o serviço, você pode acessar a documentação interativa da API (OpenAPI/Swagger UI) no seu navegador:

[http://localhost:8080/swagger-ui.html](http://localhost:8081/swagger-ui.html)

**(Nota: Assumindo que o serviço está rodando na porta 8080, ou a porta que você configurou.)**
