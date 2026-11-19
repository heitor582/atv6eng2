package com.biblioteca.controller;
import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/autores") @CrossOrigin
public class AutorController {
 private final AutorRepository repository;
 public AutorController(AutorRepository repository) { this.repository = repository; }
 @GetMapping public List<Autor> listar() { return repository.findAllByOrderByNomeAsc(); }
 @GetMapping("/{id}") public ResponseEntity<Autor> buscar(@PathVariable Long id) {
  return ResponseEntity.of(repository.findById(id));
 }
 @PostMapping public ResponseEntity<Autor> criar(@Valid @RequestBody Autor autor) {
  autor.setId(null);
  return ResponseEntity.status(201).body(repository.save(autor));
 }
 @PutMapping("/{id}") public ResponseEntity<Autor> atualizar(@PathVariable Long id, @Valid @RequestBody Autor dados) {
  return repository.findById(id).map(autor -> {
   autor.setNome(dados.getNome()); autor.setNacionalidade(dados.getNacionalidade());
   autor.setDataNascimento(dados.getDataNascimento());
   return ResponseEntity.ok(repository.save(autor));
  }).orElseGet(() -> ResponseEntity.notFound().build());
 }
 @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
  if (!repository.existsById(id)) return ResponseEntity.notFound().build();
  repository.deleteById(id); return ResponseEntity.ok().build();
 }
}
