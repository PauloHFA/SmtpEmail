package com.example.apiapex.controller;

import com.example.apiapex.model.Email;
import com.example.apiapex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Criar novo email
    @PostMapping
    public ResponseEntity<?> createEmail(@RequestBody Email email) {
        try {
            System.out.println("📩 Criando novo email: " + email);
            Email savedEmail = emailService.save(email);
            return ResponseEntity.ok(savedEmail);
        } catch (Exception e) {
            System.err.println("❌ ERRO ao criar email:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao criar email: " + e.getMessage());
        }
    }

    // Listar todos os emails
    @GetMapping
    public ResponseEntity<?> getAllEmails() {
        try {
            System.out.println("📩 Listando todos os emails...");
            List<Email> emails = emailService.findAll();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            System.err.println("❌ ERRO ao listar emails:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao listar emails: " + e.getMessage());
        }
    }

    // Buscar email por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmailById(@PathVariable Long id) {
        try {
            System.out.println("📩 Buscando email ID: " + id);
            Optional<Email> email = emailService.findById(id);
            return email.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        System.out.println("⚠️ Email não encontrado ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ ERRO ao buscar email por ID:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao buscar email: " + e.getMessage());
        }
    }

    // Atualizar email
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody Email email) {
        try {
            System.out.println("✏️ Atualizando email ID: " + id);
            Optional<Email> updatedEmail = emailService.update(id, email);
            return ResponseEntity.ok(updatedEmail);
        } catch (Exception e) {
            System.err.println("❌ ERRO ao atualizar email:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao atualizar email: " + e.getMessage());
        }
    }

    // Enviar email com base64
    @PostMapping("/enviarbase64")
    public ResponseEntity<?> sendEmailBase64(@RequestBody Email email) {
        try {
            System.out.println("📩 --- RECEBENDO REQUISIÇÃO BASE64 ---");
            System.out.println("➡️ Para: " + email.getPara());
            System.out.println("➡️ Assunto: " + email.getAssunto());
            System.out.println("➡️ Nome do arquivo: " + email.getFileName());

            String base64 = email.getFileData().contains(",")
                    ? email.getFileData().substring(email.getFileData().indexOf(",") + 1)
                    : email.getFileData();

            byte[] fileBytes = Base64.getDecoder().decode(base64);
            System.out.println("✅ Base64 decodificado. Tamanho: " + fileBytes.length);

            File tempFile = File.createTempFile("upload-", email.getFileName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
            }
            System.out.println("📂 Arquivo temporário criado: " + tempFile.getAbsolutePath());

            emailService.enviarEmailComAnexo(email.getPara(), email.getAssunto(), email.getMensagem(), tempFile);
            System.out.println("📧 Email enviado com sucesso!");

            tempFile.delete();
            System.out.println("🗑️ Arquivo temporário deletado.");

            return ResponseEntity.ok("Email com anexo enviado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ ERRO no envio de email Base64:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar email Base64: " + e.getMessage());
        }
    }

    // Deletar email
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmail(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deletando email ID: " + id);
            emailService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("❌ ERRO ao deletar email:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao deletar email: " + e.getMessage());
        }
    }

    // Enviar email simples (sem anexo)
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody Email email) {
        try {
            System.out.println("📨 Enviando email simples para: " + email.getPara());
            emailService.enviarEmail(email.getPara(), email.getAssunto(), email.getMensagem());
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ ERRO ao enviar email simples:");
            e.printStackTrace();
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
            System.out.println("📩 Enviando email com anexo para: " + para);
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            emailService.enviarEmailComAnexo(para, assunto, mensagem, tempFile);

            tempFile.delete();
            System.out.println("🗑️ Arquivo temporário deletado.");
            return ResponseEntity.ok("Email com anexo enviado com sucesso!");
        } catch (MessagingException | IOException e) {
            System.err.println("❌ ERRO ao enviar email com anexo:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar email com anexo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ ERRO inesperado ao enviar email com anexo:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro inesperado: " + e.getMessage());
        }
    }
}