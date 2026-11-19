package com.biblioteca;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") @Transactional
class IntegracaoTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 long criarAutor(String nome) throws Exception {
  String json = mvc.perform(post("/api/autores").contentType("application/json").content(mapper.writeValueAsString(java.util.Map.of("nome", nome))))
   .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  return mapper.readTree(json).get("id").asLong();
 }
 @Test void deveVincularLivroAAutor() throws Exception {
  long id=criarAutor("Machado de Assis");
  String json=mvc.perform(post("/api/livros").contentType("application/json")
   .content("{\"titulo\":\"Dom Casmurro\",\"autor\":{\"id\":"+id+"}}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.autor.id").value(id))
   .andReturn().getResponse().getContentAsString();
  mvc.perform(get("/api/livros/"+mapper.readTree(json).get("id").asLong()))
   .andExpect(status().isOk()).andExpect(jsonPath("$.autor.nome").value("Machado de Assis"));
 }
}
