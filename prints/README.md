# Evidências da atividade

Dupla: Gustavo Lopes Oliveira e Heitor Canhestro Vieira Moreira.

- `01-testes-build-success.jpg`: captura de uma página que apresenta o trecho final do log real de testes; 9 testes, sem falhas. Log completo: `testes-completo.txt`.
- `02-autores-lista.jpg`: lista de Autores.
- `03-autores-formulario.jpg`: formulário de edição de Autor.
- `04-livros-lista.jpg`: lista de Livros com o nome do autor.
- `05-livros-formulario.jpg`: formulário de edição de Livro com seleção de autor.
- `06-api-livro-vinculado.jpg`: captura de uma página com requisições e respostas reais HTTP 201; transcrição em `api.txt`.
- `07-github-actions-sucesso.jpg`: captura direta do GitHub Actions, execução `fix #2`, commit `3969e53`, com os três jobs aprovados.
- `08-github-actions-historico.jpg`: captura direta da lista de execuções, mostrando o sucesso da correção e a falha anterior.

As imagens de testes e API exibem os logs em páginas locais, não emulando um terminal. Os HTMLs auxiliares foram retirados desta pasta. As telas usam o build React real conectado à API Java real no perfil de teste (H2 em memória). Não houve execução local de containers nem validação de PostgreSQL nesta captura.

O lockfile foi corrigido com TypeScript 4.9.5 explícito. A instalação limpa `npm ci`, o comando Jest com `--passWithNoTests` (sem suíte frontend) e o build React passaram localmente após a correção. A correção foi enviada e a execução remota passou: https://github.com/heitor582/atv6eng2/actions/runs/44512325632 . Os jobs de backend, frontend e aceitação em containers foram aprovados; esses containers executaram no runner do GitHub, não no computador local.

A justificativa de pair programming e alternância de papéis está no README principal. Nenhum commit ou push foi criado nesta etapa.

## Revisão final de 19/11/2026

Veja `../REVISAO-ENTREGA.md`. `testes-revisao.txt` registra 10 testes passando e `api-revisao.txt` confirma a correção da data histórica. As imagens 01 e 06 ainda são apresentações de logs antigos, não capturas do terminal/ferramenta de API; devem ser substituídas antes da entrega. As imagens 02 e 03 mostram a data anterior à correção. A imagem 07 comprova a execução fix #2, anterior às mudanças da revisão final.
