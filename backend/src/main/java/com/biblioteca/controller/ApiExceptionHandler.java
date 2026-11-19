package com.biblioteca.controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<Map<String, String>> integridade(DataIntegrityViolationException exception) {
  return ResponseEntity.status(409).body(Map.of("message", "Operação impedida por um vínculo existente. Para excluir um autor, exclua seus livros primeiro."));
 }
}
