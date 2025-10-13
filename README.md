## 🧑‍💻 Microserviço: Cadastro de Usuários

Este microserviço é responsável pelo **gerenciamento de usuários**, incluindo cadastro, autenticação e atualização de informações. É a base para outros microserviços que dependem dos dados de usuário, como o microserviço de agendamento de tarefas.  

---

## 💻 Linguagem e Frameworks
<div style="display: inline_block"><br>
  <img align="center" alt="Java" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-plain.svg">
  <img align="center" alt="Spring Boot" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg">
  <img align="center" alt="PostgreSQL" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-plain.svg">
</div>

---

## 🔧 Funcionalidades
- Cadastro de novos usuários 📝  
- Autenticação e login com JWT 🔑  
- Atualização de dados pessoais, endereço e telefone ✏️  
- Deleção de usuários ❌  
- Integração simples com outros microserviços via REST API 🔗  

---

## 📂 Estrutura do projeto
- `controller`: Endpoints REST  
- `business`: Serviços com regras de negócio  
- `infrastructure/entity`: Entidades que representam o banco  
- `infrastructure/repository`: Repositórios Spring Data  
- `infrastructure/security`: Configuração de segurança e JWT  
- `business/converter`: Conversores DTO ↔ Entity utilizando `.builder()`  

---

## 📌 Como executar
1. Clone o repositório  
```bash
git clone https://github.com/Iarlleym/CadastroDeUsuarios.git
