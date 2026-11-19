package com.biblioteca;
import com.biblioteca.controller.LivroController;
import com.biblioteca.model.*;
import com.biblioteca.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(LivroController.class)
class LivroControllerTest {
 @Autowired MockMvc mvc;
 @MockBean LivroRepository repository;
 @MockBean AutorRepository autores;
 @Test void deveCriarComSucesso() throws Exception {
  Autor autor = new Autor(); autor.setId(1L); autor.setNome("Machado de Assis");
  when(autores.findById(1L)).thenReturn(Optional.of(autor));
  when(repository.save(any(Livro.class))).thenAnswer(i -> { Livro livro=i.getArgument(0); livro.setId(1L); return livro; });
  mvc.perform(post("/api/livros").contentType("application/json").content("{\"titulo\":\"Dom Casmurro\",\"autor\":{\"id\":1}}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.autor.nome").value("Machado de Assis"));
 }
 @Test void deveRetornar404() throws Exception { mvc.perform(get("/api/livros/999")).andExpect(status().isNotFound()); }
}
