package com.biblioteca;
import com.biblioteca.controller.AutorController;
import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(AutorController.class)
class AutorControllerTest {
 @Autowired MockMvc mvc;
 @MockBean AutorRepository repository;
 @Test void deveCriarComSucesso() throws Exception {
  when(repository.save(any(Autor.class))).thenAnswer(i -> { Autor a=i.getArgument(0); a.setId(1L); return a; });
  mvc.perform(post("/api/autores").contentType("application/json").content("{\"nome\":\"Machado de Assis\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
 }
 @Test void deveRetornar404() throws Exception { mvc.perform(get("/api/autores/999")).andExpect(status().isNotFound()); }
}
