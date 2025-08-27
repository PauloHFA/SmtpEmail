# 📧 ApiApex - API de Envio de E-mails

**ApiApex** é uma aplicação Spring Boot que permite o envio de e-mails através do serviço **Gmail SMTP**.  
A aplicação expõe um endpoint REST que pode ser acessado localmente ou via **ngrok** para integração com sistemas externos.

---

## 📋 Índice

- [✨ Funcionalidades](#-funcionalidades)  
- [🚀 Tecnologias Utilizadas](#-tecnologias-utilizadas)  
- [📋 Pré-requisitos](#-pré-requisitos)  
- [⚙️ Configuração](#-configuração)  
- [🛠 Instalação e Execução](#-instalação-e-execução)  
- [📮 Uso da API](#-uso-da-api)  
- [🌍 Expondo com Ngrok](#-expondo-com-ngrok)  
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)  

---

## ✨ Funcionalidades

- Envio de e-mails através do protocolo SMTP do Gmail  
- API RESTful com endpoint exclusivo para envio de mensagens  
- Integração simples com outras aplicações  
- Possibilidade de exposição pública via ngrok  
- Tratamento de erros e respostas padronizadas  

---

## 🚀 Tecnologias Utilizadas

- Java 17+  
- Spring Boot  
- Spring Mail (`spring-boot-starter-mail`)  
- Spring Web (`spring-boot-starter-web`)  
- Ngrok (para exposição pública)  

---

## 📋 Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- JDK 17 ou superior  
- Maven 3.6+  
- Conta no Gmail  
- Ngrok (opcional, para exposição pública)  

---

## ⚙️ Configuração

### Configuração do Gmail

Por questões de segurança, **não utilize sua senha real do Gmail**. Em vez disso, gere uma **Senha de App**:

1. Acesse a [Segurança da Conta do Google](https://myaccount.google.com/security)  
2. Ative a verificação em duas etapas (se ainda não estiver ativado)  
3. Na seção "Senhas de app", clique em "Selecionar aplicativo" → "E-mail" e "Selecionar dispositivo" → "Outro"  
4. Digite um nome para a aplicação (ex: "ApiApex") e clique em "Gerar"  
5. Copie a senha gerada (16 caracteres)  

### Configuração da Aplicação

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Nome da aplicação e porta
spring.application.name=ApiApex
server.port=8080

# Configuração do servidor SMTP do Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL@gmail.com
spring.mail.password=SUA_SENHA_DE_APP_GERADA

# Propriedades adicionais do SMTP
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

Substitua SEU_EMAIL@gmail.com pelo seu e-mail do Gmail e SUA_SENHA_DE_APP_GERADA pela senha de aplicativo gerada.

🛠 Instalação e Execução
Executando localmente

Clone o repositório:

git clone git@github.com:PauloHFA/ApiApex.git
cd ApiApex


Compile o projeto:

mvn clean compile


Execute a aplicação:

mvn spring-boot:run


A aplicação estará disponível em: http://localhost:8080

📮 Uso da API
Endpoint POST /emails/send

Corpo da Requisição:

{
  "para": "destinatario@exemplo.com",
  "assunto": "Assunto do E-mail",
  "mensagem": "Conteúdo da mensagem em texto simples ou HTML"
}


Exemplo de requisição local com curl:

curl -X POST http://localhost:8080/emails/send \
-H "Content-Type: application/json" \
-d '{
  "para": "destinatario@gmail.com",
  "assunto": "Teste API ApiApex",
  "mensagem": "Olá, este é um teste de envio de e-mail pela ApiApex!"
}'


Se estiver usando ngrok, substitua a URL pelo link fornecido, por exemplo:

curl -X POST https://1234-5678.ngrok-free.app/emails/send \
-H "Content-Type: application/json" \
-d '{
  "para": "destinatario@gmail.com",
  "assunto": "Teste API ApiApex",
  "mensagem": "Olá, este é um teste de envio de e-mail pela ApiApex!"
}'

Respostas da API

Sucesso (200 OK):

"E-mail enviado com sucesso!"


Erro (500 - Erro Interno do Servidor):

"Erro ao enviar email: [mensagem de erro específico]"

🌍 Expondo com Ngrok

Instale o Ngrok

Execute a aplicação Spring Boot

Em outro terminal, execute:

ngrok http 8080


O Ngrok fornecerá uma URL pública, por exemplo:

Forwarding    https://1234-5678.ngrok-free.app -> http://localhost:8080


Agora você pode acessar sua API externamente:

https://1234-5678.ngrok-free.app/emails/send

📁 Estrutura do Projeto
src/
└── main/
    ├── java/
    │   └── com/
    │       └── exemplo/
    │           └── apiapex/
    │               ├── ApiApexApplication.java
    │               ├── controller/
    │               │   └── EmailController.java
    │               ├── model/
    │               │   └── Email.java
    │               ├── service/
    │               │   └── EmailService.java
    │               └── config/
    │                   └── MailConfig.java
    └── resources/
        └── application.properties

