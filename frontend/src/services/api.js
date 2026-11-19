import axios from 'axios';
const api = axios.create({ baseURL: process.env.REACT_APP_API_URL || '/api' });
export const criarService = (recurso) => ({
 listar: () => api.get(`/${recurso}`),
 buscar: (id) => api.get(`/${recurso}/${id}`),
 criar: (dados) => api.post(`/${recurso}`, dados),
 atualizar: (id, dados) => api.put(`/${recurso}/${id}`, dados),
 deletar: (id) => api.delete(`/${recurso}/${id}`),
});
export const autorService = criarService('autores');
export const mensagemErro = (erro) => erro.response?.data?.message || 'Não foi possível concluir. Verifique os dados e a conexão com a API.';
export const livroService = { ...criarService('livros'), porAutor: (id) => api.get(`/livros/autor/${id}`) };
