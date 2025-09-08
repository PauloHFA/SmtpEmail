package com.example.apiapex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Data
@Entity
@Getter
@Setter
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String para;
    private String assunto;
    private String mensagem;

    private String fileName;
    private String fileData;
}