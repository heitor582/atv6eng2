# Revisão da entrega — Parte 2 (3 pontos)

Conferência realizada em 19/11/2026 contra `parte2-laboratorio.pdf`, especialmente os itens 5 e 6. Não é previsão ou garantia de nota; a Parte 1 não foi avaliada aqui.

## Rubrica

| Critério | Valor | Resultado da conferência |
| --- | --- | --- |
| Git/GitHub | 0,45 | **Incompleto:** histórico atual tem apenas main, sem as duas branches feature e os dois PRs/reviews exigidos. A justificativa de pair programming está registrada, mas precisa ser aceita pelo professor. |
| Modelagem | 0,45 | Autor e Livro com os campos e limites pedidos; FK obrigatória em Livro, sem coleção circular em Autor. Corrigida a preservação de datas históricas. |
| Backend Autores | 0,45 | Cinco endpoints, criação 201, consulta/edição/exclusão 200 e id inexistente 404. Validação de nome e tamanhos. |
| Backend Livros | 0,45 | Cinco endpoints, vínculo validado no POST e troca de autor no PUT. Filtro por autor como extra. |
| Testes | 0,45 | Dois testes unitários por controller, integração do vínculo e extras. Após a revisão: 10 testes passando; regressão da data também passou em UTC. |
| Frontend React | 0,45 | Quatro componentes, serviços, seis rotas e seleção de autor. npm ci, build e npm start validados; fluxo pela API através do proxy também validado. |
| Docker + CI + README | 0,30 | Arquivos presentes. Execução remota fix #2 aprovada com os três jobs. README corrigido para corresponder ao histórico real. Novas alterações locais ainda precisam de execução remota. |

## Erros encontrados e corrigidos

1. Data histórica: POST de `1839-06-21` seguido de GET retornava `1839-06-20` no H2/fuso de São Paulo. O teste de regressão falhou antes da correção e passou depois. Persistência via JDBC 4.2 mantém LocalDate e SQL DATE sem passar por java.sql.Date.
2. npm start: falhava com `options.allowedHosts[0] should be a non-empty string`. O proxy explícito em `src/setupProxy.js` substitui a opção problemática de package.json. Servidor compilou e requisições reais via porta 3000 passaram.
3. README: afirmava que as branches originais estavam preservadas, apesar da remoção do histórico. Corrigido; divisão por áreas e trabalho conjunto agora correspondem ao relato da dupla.

## Pendências antes de entregar

- **Resolver com o professor a ausência de branches/PRs/reviews.** A justificativa não substitui automaticamente um requisito obrigatório. Não fabricar revisões ou histórico retroativo. O critério inteiro vale 0,45 ponto; o desconto efetivo é decisão do professor.
- **Refazer o print dos testes diretamente no terminal** após `cd backend && mvn test`, mostrando BUILD SUCCESS e os 10 testes. A imagem 01 atual é uma captura de uma página de logs e tem apenas os 9 testes anteriores.
- **Refazer o print da API diretamente em curl, Postman ou Swagger**, mostrando criação de autor e livro vinculado (201). A imagem 06 atual apresenta logs em uma página auxiliar e ainda contém a data antiga incorreta.
- **Atualizar os prints de Autores (02 e 03)**: mostram a data anterior de 20/06/1839. O sistema corrigido preserva 21/06/1839.
- Enviar as correções locais, aguardar o CI passar novamente e atualizar a captura 07. O sucesso de fix #2 comprova a versão anterior às correções desta revisão, não os arquivos novos.
- Confirmar acesso do professor ao repositório e enviar pelo canal indicado. Nenhum node_modules, target ou build está versionado.

## Evidências de validação desta revisão

- `prints/testes-revisao.txt`: saída real do Maven com 10 testes, zero falhas, BUILD SUCCESS.
- `prints/api-revisao.txt`: chamadas reais pelo proxy do React, com criação, consulta, vínculo e exclusão; data histórica preservada.
- `npm ci` e `npm run build`: concluídos com sucesso; `npm start` compilou.
- Sem Docker local. A aceitação em PostgreSQL/containers após a mudança de persistência deverá ser confirmada pelo próximo CI.

## Bônus

Filtro por autor, listagem ordenada, CRUD completo de Autor, tratamento 409 e job de aceitação estão implementados. O bônus (exemplo de até +0,3 no enunciado) depende do professor; não deve ser contado como compensação garantida para o requisito Git.
