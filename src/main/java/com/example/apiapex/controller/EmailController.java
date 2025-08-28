package com.example.apiapex.controller;

import com.example.apiapex.model.Email;
import com.example.apiapex.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Criar novo email
    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody Email email) {
        Email savedEmail = emailService.save(email);
        return ResponseEntity.ok(savedEmail);
    }

    // Listar todos os emails
    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        List<Email> emails = emailService.findAll();
        return ResponseEntity.ok(emails);
    }

    // Buscar email por ID
    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Optional<Email> email = emailService.findById(id);
        return email.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar email
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Email>> updateEmail(@PathVariable Long id, @RequestBody Email email) {
        Optional<Email> updatedEmail = emailService.update(id, email);
        return ResponseEntity.ok(updatedEmail);
    }

    // Deletar email
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Enviar email simples (sem anexo)
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {
        try {
            emailService.enviarEmail(email.getPara(), email.getAssunto(), email.getMensagem());
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar email: " + e.getMessage());
        }
    }

    // Enviar email com anexo
    @PostMapping("/enviararquivo")
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("para") String para,
            @RequestParam("assunto") String assunto,
            @RequestParam("mensagem") String mensagem,
            @RequestParam("file") MultipartFile file) {

        try {
            // Salva o arquivo temporariamente no disco
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            // Chama o service para enviar o e-mail com o anexo
            emailService.enviarEmailComAnexo(para, assunto, mensagem, tempFile);

            // Deleta o arquivo temporário depois do envio
            tempFile.delete();

            return ResponseEntity.ok("Email com anexo enviado com sucesso!");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar email com anexo: " + e.getMessage());
        }
    }
}