package com.biblioteca.controller;
import com.biblioteca.model.*;
import com.biblioteca.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api/livros") @CrossOrigin
public class LivroController {
 private final LivroRepository repository;
 private final AutorRepository autores;
 public LivroController(LivroRepository repository, AutorRepository autores) { this.repository=repository; this.autores=autores; }
 private Autor resolverAutor(Autor autor) {
  if (autor.getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o id do autor.");
  return autores.findById(autor.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));
 }
 @GetMapping public List<Livro> listar() { return repository.findAllByOrderByTituloAsc(); }
 @GetMapping("/autor/{autorId}") public List<Livro> porAutor(@PathVariable Long autorId) { return repository.findByAutorIdOrderByTituloAsc(autorId); }
 @GetMapping("/{id}") public ResponseEntity<Livro> buscar(@PathVariable Long id) { return ResponseEntity.of(repository.findById(id)); }
 @PostMapping public ResponseEntity<Livro> criar(@Valid @RequestBody Livro livro) {
  livro.setId(null); livro.setAutor(resolverAutor(livro.getAutor()));
  return ResponseEntity.status(201).body(repository.save(livro));
 }
 @PutMapping("/{id}") public ResponseEntity<Livro> atualizar(@PathVariable Long id, @Valid @RequestBody Livro dados) {
  return repository.findById(id).map(livro -> {
   livro.setTitulo(dados.getTitulo()); livro.setIsbn(dados.getIsbn());
   livro.setAnoPublicacao(dados.getAnoPublicacao()); livro.setGenero(dados.getGenero());
   livro.setAutor(resolverAutor(dados.getAutor()));
   return ResponseEntity.ok(repository.save(livro));
  }).orElseGet(() -> ResponseEntity.notFound().build());
 }
 @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
  if (!repository.existsById(id)) return ResponseEntity.notFound().build();
  repository.deleteById(id); return ResponseEntity.ok().build();
 }
}
