📧 ApiApex - API de Envio de E-mails
ApiApex é uma aplicação Spring Boot que permite o envio de e-mails através do serviço Gmail SMTP. A aplicação expõe um endpoint REST que pode ser acessado localmente ou via ngrok para integração com sistemas externos.

📋 Índice
Funcionalidades

Tecnologias Utilizadas

Pré-requisitos

Configuração

Instalação e Execução

Uso da API

Expondo com Ngrok

Estrutura do Projeto

✨ Funcionalidades
Envio de e-mails através do protocolo SMTP do Gmail

API RESTful com endpoint único para envio de mensagens

Integração simples com outras aplicações

Possibilidade de exposição pública via ngrok

Tratamento de erros e respostas padronizadas

🚀 Tecnologias Utilizadas
Java 17+

Spring Boot

Spring Mail (spring-boot-starter-mail)

Spring Web (spring-boot-starter-web)

Ngrok (para exposição pública)

📋 Pré-requisitos
Antes de executar a aplicação, certifique-se de ter instalado:

JDK 17 ou superior

Maven 3.6+

Conta no Gmail

Ngrok (opcional, para exposição pública)

⚙️ Configuração
1. Configuração do Gmail
Por questões de segurança, não utilize sua senha real do Gmail. Em vez disso, gere uma Senha de App:

Acesse Google Account Security

Ative a verificação em duas etapas (se ainda não estiver ativada)

Na seção "Como fazer login no Google", selecione "Senhas de app"

Clique em "Selecionar app" → "E-mail" e "Selecionar dispositivo" → "Outro"

Digite um nome para a aplicação (ex: "ApiApex") e clique em "Gerar"

Copie a senha gerada (16 caracteres)

2. Configuração da Aplicação
Edite o arquivo src/main/resources/application.properties:

properties
# Configuração básica da aplicação
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
Substitua SEU_EMAIL@gmail.com pelo seu e-mail do Gmail e SUA_SENHA_DE_APP_GERADA pela senha de aplicativo que você gerou.

🛠 Instalação e Execução
Executando localmente
Clone o repositório:

bash
git clone <url-do-repositorio>
cd ApiApex
Compile o projeto:

bash
mvn clean compile
Execute a aplicação:

bash
mvn spring-boot:run
A aplicação estará disponível em http://localhost:8080.

Executando com Ngrok
Para expor sua API local publicamente:

Instale o Ngrok seguindo as instruções em ngrok.com/download

Execute sua aplicação Spring Boot como descrito acima

Em outro terminal, execute:

bash
ngrok http 8080
O Ngrok fornecerá uma URL pública como:

text
Forwarding    https://1234-5678.ngrok-free.app -> http://localhost:8080
Use esta URL para acessar sua API externamente

📮 Uso da API
Endpoint
POST /emails/send

Corpo da Requisição
Envie um JSON com a seguinte estrutura:

json
{
  "para": "destinatario@exemplo.com",
  "assunto": "Assunto do E-mail",
  "mensagem": "Conteúdo da mensagem em texto simples ou HTML"
}
Exemplo de Requisição
bash
curl -X POST \
  http://localhost:8080/emails/send \
  -H "Content-Type: application/json" \
  -d '{
    "para": "destinatario@gmail.com",
    "assunto": "Teste API ApiApex",
    "mensagem": "Olá, este é um teste de envio de e-mail pela ApiApex!"
  }'
Se estiver usando Ngrok, substitua a URL:

bash
curl -X POST \
  https://1234-5678.ngrok-free.app/emails/send \
  -H "Content-Type: application/json" \
  -d '{
    "para": "destinatario@gmail.com",
    "assunto": "Teste API ApiApex",
    "mensagem": "Olá, este é um teste de envio de e-mail pela ApiApex!"
  }'
Respostas da API
Sucesso (200 OK):

json
"Email enviado com sucesso!"
Erro (500 Internal Server Error):

json
"Erro ao enviar email: [mensagem de erro específica]"

📁 Estrutura do Projeto
text

src/
└── main/
    ├── java/
    │   └── com/
    │       └── example/
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
