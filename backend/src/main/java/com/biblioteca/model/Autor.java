package com.biblioteca.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.*;
import org.hibernate.annotations.JdbcType;
import com.biblioteca.persistence.LocalDateJdbcType;
@Data @Entity @Table(name = "autores")
public class Autor {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @NotBlank @Size(max = 100) @Column(nullable = false, length = 100) private String nome;
 @Size(max = 50) @Column(length = 50) private String nacionalidade;
 @JdbcType(LocalDateJdbcType.class)
 @Column(name = "data_nascimento") private LocalDate dataNascimento;
 @Column(name = "criado_em", nullable = false, updatable = false) private LocalDateTime criadoEm;
 @PrePersist void registrarCriacao() { criadoEm = LocalDateTime.now(); }
}
