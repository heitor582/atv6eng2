package com.biblioteca.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Entity @Table(name = "livros")
public class Livro {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @NotBlank @Size(max = 200) @Column(nullable = false, length = 200) private String titulo;
 @Size(max = 20) @Column(length = 20) private String isbn;
 @Column(name = "ano_publicacao") private Integer anoPublicacao;
 @Size(max = 50) @Column(length = 50) private String genero;
 @NotNull @ManyToOne(fetch = FetchType.EAGER, optional = false)
 @JoinColumn(name = "autor_id", nullable = false) private Autor autor;
 @Column(name = "criado_em", nullable = false, updatable = false) private LocalDateTime criadoEm;
 @PrePersist void registrarCriacao() { criadoEm = LocalDateTime.now(); }
}
