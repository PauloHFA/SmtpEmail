# 📧 API de Envio de E-mails (ApiApex)

Esta aplicação Spring Boot tem como objetivo enviar e-mails utilizando o serviço **Gmail SMTP**.  
Ela expõe um endpoint REST para envio de mensagens, podendo ser acessado localmente ou via **ngrok**.

---

## 🚀 Tecnologias utilizadas
- Java 17+
- Spring Boot
- Spring Mail (`spring-boot-starter-mail`)
- Ngrok

---

## 📦 Dependências necessárias

No arquivo **`pom.xml`**, certifique-se de adicionar:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

⚙️ Configuração do application.properties

Configure o arquivo src/main/resources/application.properties:

spring.application.name=ApiApex

# Configuração do servidor SMTP do Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL@gmail.com
spring.mail.password=APP_PASSWORD_GMAIL

# Propriedades adicionais
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

🔑 Atenção:
Não utilize a senha real da sua conta Google.
É necessário gerar uma Senha de App no Painel de Segurança do Gmail
 → "Senhas de App".
Copie e cole a senha gerada no campo spring.mail.password.

📮 Endpoint de Envio de E-mail

O controller expõe o endpoint:

@PostMapping("/send")
public ResponseEntity<String> sendEmail(@RequestBody Email email) {
    try {
        emailService.enviarEmail(email.getPara(), email.getAssunto(), email.getMensagem());
        return ResponseEntity.ok("Email enviado com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro ao enviar email: " + e.getMessage());
    }
}

Exemplo de requisição
POST http://localhost:8080/emails/send -vai ser substituido pela url do ngrok caso esteja utilizando.
Content-Type: application/json

{
  "para": "destinatario@gmail.com",
  "assunto": "Teste API",
  "mensagem": "Olá, este é um teste de envio de e-mail pela ApiApex!"
}


Resposta esperada:

"Email enviado com sucesso!"

🌍 Expor a API com Ngrok

Instale o ngrok:
Download

Execute sua aplicação localmente:

mvn spring-boot:run


Abra um túnel ngrok para a porta do Spring Boot (geralmente 8080):

ngrok http 8080


O ngrok irá fornecer uma URL pública, por exemplo:

Forwarding    https://1234-5678.ngrok-free.app -> http://localhost:8080


Agora você pode acessar sua API externamente em:

https://1234-5678.ngrok-free.app/emails/send

✅ Resumo dos passos necessários

Adicionar as dependências (spring-boot-starter-mail e spring-boot-starter-web).

Configurar o application.properties com e-mail e senha de app do Gmail.

Subir a aplicação (mvn spring-boot:run).

Usar o ngrok para expor o endpoint publicamente.

Testar a API enviando uma requisição POST para /send.
