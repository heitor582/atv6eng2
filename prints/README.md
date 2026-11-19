# Evidências da atividade

Dupla: Gustavo Lopes Oliveira e Heitor Canhestro Vieira Moreira.

- `01-testes-build-success.jpg`: captura de uma página que apresenta o trecho final do log real de testes; 9 testes, sem falhas. Log completo: `testes-completo.txt`.
- `02-autores-lista.jpg`: lista de Autores.
- `03-autores-formulario.jpg`: formulário de edição de Autor.
- `04-livros-lista.jpg`: lista de Livros com o nome do autor.
- `05-livros-formulario.jpg`: formulário de edição de Livro com seleção de autor.
- `06-api-livro-vinculado.jpg`: captura de uma página com requisições e respostas reais HTTP 201; transcrição em `api.txt`.
- `07-github-actions-falha.jpg`: execução real do Actions antes da correção do lockfile. **Não é evidência de CI aprovado.** Substituir após enviar a correção e concluir uma execução com sucesso.

As imagens de testes e API exibem os logs em páginas locais, não emulando um terminal. Os HTMLs usados para apresentá-los também estão nesta pasta. As telas usam o build React real conectado à API Java real no perfil de teste (H2 em memória). Não houve execução local de containers nem validação de PostgreSQL nesta captura.

O lockfile foi corrigido com TypeScript 4.9.5 explícito. A instalação limpa `npm ci`, o comando Jest com `--passWithNoTests` (sem suíte frontend) e o build React passaram localmente após a correção. É necessário enviar `frontend/package.json` e `frontend/package-lock.json` ao GitHub para disparar outra execução.

A justificativa de pair programming e alternância de papéis está no README principal. Nenhum commit ou push foi criado nesta etapa.
