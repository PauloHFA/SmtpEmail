package com.example.apiapex.service;

import com.example.apiapex.model.Email;
import com.example.apiapex.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;

    public EmailService(EmailRepository emailRepository, JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    // Criar ou atualizar um Email
    public Email save(Email email) {
        return emailRepository.save(email);
    }

    // Buscar todos os Emails
    public List<Email> findAll() {
        return emailRepository.findAll();
    }

    // Buscar um Email por ID
    public Optional<Email> findById(Long id) {
        return emailRepository.findById(id);
    }

    // Deletar um Email por ID
    public void deleteById(Long id) {
        emailRepository.deleteById(id);
    }

    public void enviarEmail(String para, String assunto, String mensagem) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(para);
        email.setSubject(assunto);
        email.setText(mensagem);

        mailSender.send(email);
    }

    // Atualizar um Email (se existir)
    public Optional<Email> update(Long id, Email emailDetails) {
        return emailRepository.findById(id).map(email -> {
            email.setAssunto(emailDetails.getAssunto());
            email.setMensagem(emailDetails.getMensagem());
            return emailRepository.save(email);
        }
        );
    }
}