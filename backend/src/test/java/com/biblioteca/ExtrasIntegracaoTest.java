package com.biblioteca;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.biblioteca.repository.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ExtrasIntegracaoTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper mapper;
 @Autowired LivroRepository livros;
 @Autowired AutorRepository autores;
 @BeforeEach void limpar() { livros.deleteAll(); autores.deleteAll(); }
 long criar(String recurso, Map<String, Object> dados) throws Exception {
  String json=mvc.perform(post("/api/"+recurso).contentType("application/json").content(mapper.writeValueAsString(dados)))
   .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  return mapper.readTree(json).get("id").asLong();
 }
 @Test void deveExecutarFluxoCompletoAutor() throws Exception {
  long id=criar("autores", Map.of("nome","Original"));
  mvc.perform(get("/api/autores/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Original"));
  mvc.perform(put("/api/autores/"+id).contentType("application/json").content("{\"nome\":\"Atualizado\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Atualizado"));
  mvc.perform(delete("/api/autores/"+id)).andExpect(status().isOk());
  mvc.perform(get("/api/autores/"+id)).andExpect(status().isNotFound());
 }
 @Test void deveOrdenarEFiltrarLivros() throws Exception {
  long z=criar("autores",Map.of("nome","Zelia")), a=criar("autores",Map.of("nome","Ana"));
  criar("livros",Map.of("titulo","Zebra","autor",Map.of("id",z)));
  criar("livros",Map.of("titulo","Abelha","autor",Map.of("id",z)));
  criar("livros",Map.of("titulo","Outro","autor",Map.of("id",a)));
  mvc.perform(get("/api/autores")).andExpect(jsonPath("$[0].nome").value("Ana"));
  mvc.perform(get("/api/livros")).andExpect(jsonPath("$[0].titulo").value("Abelha")).andExpect(jsonPath("$[2].titulo").value("Zebra"));
  mvc.perform(get("/api/livros/autor/"+z)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
   .andExpect(jsonPath("$[0].titulo").value("Abelha")).andExpect(jsonPath("$[1].titulo").value("Zebra"));
  mvc.perform(get("/api/livros/autor/999999")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
 }
 @Test void deveProtegerAutorETrocarVinculoNoPut() throws Exception {
  long primeiro=criar("autores",Map.of("nome","Primeiro")), segundo=criar("autores",Map.of("nome","Segundo"));
  long livro=criar("livros",Map.of("titulo","Livro","autor",Map.of("id",primeiro)));
  mvc.perform(delete("/api/autores/"+primeiro)).andExpect(status().isConflict()).andExpect(jsonPath("$.message").exists());
  mvc.perform(get("/api/autores/"+primeiro)).andExpect(status().isOk());
  mvc.perform(put("/api/livros/"+livro).contentType("application/json")
   .content(mapper.writeValueAsString(Map.of("titulo","Editado","autor",Map.of("id",segundo)))))
   .andExpect(status().isOk()).andExpect(jsonPath("$.autor.id").value(segundo));
  mvc.perform(get("/api/livros/"+livro)).andExpect(jsonPath("$.autor.id").value(segundo));
  mvc.perform(delete("/api/autores/"+primeiro)).andExpect(status().isOk());
  mvc.perform(delete("/api/livros/"+livro)).andExpect(status().isOk());
  mvc.perform(delete("/api/autores/"+segundo)).andExpect(status().isOk());
 }
 @Test void deveRejeitarDadosInvalidos() throws Exception {
  mvc.perform(post("/api/autores").contentType("application/json").content("{\"nome\":\" \"}"))
   .andExpect(status().isBadRequest());
  mvc.perform(post("/api/livros").contentType("application/json").content("{\"titulo\":\"Livro\"}"))
   .andExpect(status().isBadRequest());
  mvc.perform(post("/api/livros").contentType("application/json").content("{\"titulo\":\"Livro\",\"autor\":{\"id\":999999}}"))
   .andExpect(status().isNotFound());
  mvc.perform(post("/api/livros").contentType("application/json").content("{\"titulo\":\"Livro\",\"autor\":{}}"))
   .andExpect(status().isBadRequest());
 }
}
