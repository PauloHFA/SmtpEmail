package com.example.apiapex.controller;

import com.example.apiapex.model.Email;
import com.example.apiapex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    // Criar novo email
    @PostMapping
    public ResponseEntity<?> createEmail(@RequestBody Email email) {
        try {
            logger.info("📩 Criando novo email: {}", email);
            Email savedEmail = emailService.save(email);
            return ResponseEntity.ok(savedEmail);
        } catch (Exception e) {
            logger.error("❌ ERRO ao criar email", e);
            return ResponseEntity.status(500).body("Erro ao criar email: " + e.getMessage());
        }
    }

    // Listar todos os emails
    @GetMapping
    public ResponseEntity<?> getAllEmails() {
        try {
            logger.info("📩 Listando todos os emails...");
            List<Email> emails = emailService.findAll();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            logger.error("❌ ERRO ao listar emails", e);
            return ResponseEntity.status(500).body("Erro ao listar emails: " + e.getMessage());
        }
    }

    // Buscar email por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmailById(@PathVariable Long id) {
        try {
            logger.info("📩 Buscando email ID: {}", id);
            Optional<Email> email = emailService.findById(id);
            return email.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("⚠️ Email não encontrado ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("❌ ERRO ao buscar email por ID", e);
            return ResponseEntity.status(500).body("Erro ao buscar email: " + e.getMessage());
        }
    }

    // Atualizar email
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody Email email) {
        try {
            logger.info("✏️ Atualizando email ID: {}", id);
            Optional<Email> updatedEmail = emailService.update(id, email);
            return ResponseEntity.ok(updatedEmail);
        } catch (Exception e) {
            logger.error("❌ ERRO ao atualizar email", e);
            return ResponseEntity.status(500).body("Erro ao atualizar email: " + e.getMessage());
        }
    }

    // Enviar email com base64
    @PostMapping("/enviarbase64")
    public ResponseEntity<?> sendEmailBase64(@RequestBody Email email) {
        try {
            logger.info("📩 --- RECEBENDO REQUISIÇÃO BASE64 ---");
            logger.info("➡️ Para: {}", email.getPara());
            logger.info("➡️ Assunto: {}", email.getAssunto());
            logger.info("➡️ Nome do arquivo: {}", email.getFileName());

            String base64 = email.getFileData().contains(",")
                    ? email.getFileData().substring(email.getFileData().indexOf(",") + 1)
                    : email.getFileData();

            byte[] fileBytes = Base64.getDecoder().decode(base64);
            logger.info("✅ Base64 decodificado. Tamanho: {}", fileBytes.length);

            File tempFile = File.createTempFile("upload-", email.getFileName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
            }
            logger.info("📂 Arquivo temporário criado: {}", tempFile.getAbsolutePath());

            emailService.enviarEmailComAnexo(email.getPara(), email.getAssunto(), email.getMensagem(), tempFile);
            logger.info("📧 Email enviado com sucesso!");

            tempFile.delete();
            logger.info("🗑️ Arquivo temporário deletado.");

            return ResponseEntity.ok("Email com anexo enviado com sucesso!");
        } catch (Exception e) {
            logger.error("❌ ERRO no envio de email Base64", e);
            return ResponseEntity.status(500).body("Erro ao enviar email Base64: " + e.getMessage());
        }
    }

    // Deletar email
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmail(@PathVariable Long id) {
        try {
            logger.info("🗑️ Deletando email ID: {}", id);
            emailService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("❌ ERRO ao deletar email", e);
            return ResponseEntity.status(500).body("Erro ao deletar email: " + e.getMessage());
        }
    }

    // Enviar email simples (sem anexo)
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody Email email) {
        try {
            logger.info("📨 Enviando email simples para: {}", email.getPara());
            emailService.enviarEmail(email.getPara(), email.getAssunto(), email.getMensagem());
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (Exception e) {
            logger.error("❌ ERRO ao enviar email simples", e);
            return ResponseEntity.status(500).body("Erro ao enviar email: " + e.getMessage());
        }
    }

    // Enviar email com anexo MultipartFile
    @PostMapping("/enviararquivo")
    public ResponseEntity<?> sendEmailWithAttachment(
            @RequestParam("para") String para,
            @RequestParam("assunto") String assunto,
            @RequestParam("mensagem") String mensagem,
            @RequestParam("file") MultipartFile file) {

        try {
            logger.info("📩 Enviando email com anexo para: {}", para);
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            emailService.enviarEmailComAnexo(para, assunto, mensagem, tempFile);

            tempFile.delete();
            logger.info("🗑️ Arquivo temporário deletado.");
            return ResponseEntity.ok("Email com anexo enviado com sucesso!");
        } catch (MessagingException | IOException e) {
            logger.error("❌ ERRO ao enviar email com anexo", e);
            return ResponseEntity.status(500).body("Erro ao enviar email com anexo: " + e.getMessage());
        } catch (Exception e) {
            logger.error("❌ ERRO inesperado ao enviar email com anexo", e);
            return ResponseEntity.status(500).body("Erro inesperado: " + e.getMessage());
        }
    }
}