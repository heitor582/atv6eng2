# Biblioteca Web

Sistema acadêmico para cadastrar autores e livros. Um autor pode escrever vários livros; cada livro pertence a um autor. O relacionamento JPA fica somente em `Livro`, evitando referências circulares no JSON.

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Backend | Java 17, Spring Boot 3.2.12, Maven, Web, Data JPA, Validation, Lombok |
| Frontend | React 18, React Router 6, Axios, react-scripts 5, Node 20 |
| Banco | PostgreSQL 15; H2 no perfil de testes |
| Testes | JUnit 5, Mockito, MockMvc, SpringBootTest |
| Infraestrutura | Docker Compose, Nginx, GitHub Actions |

## Divisão do trabalho

| Área | Trabalho realizado pela dupla |
| --- | --- |
| Autores | Estrutura, banco, entidade, repository, CRUD, testes e telas de Autores |
| Livros | Entidade, repository, CRUD, testes de integração e telas com seleção de autor |
| Arquivos comuns e extras | Rotas, serviços HTTP, Docker, CI, documentação, filtro e tratamento de conflitos |

Dupla: **Gustavo Lopes Oliveira e Heitor Canhestro Vieira Moreira** (mesma da atividade 5). Ambos trabalharam em pair programming, alternando implementação e revisão em tempo real. A entrega atual está consolidada na `main`. O histórico original foi removido durante a reorganização; as branches `feature/autores`, `feature/livros` e `feature/extras` não fazem parte do histórico atual.

## Estrutura

```text
backend/
  pom.xml, Dockerfile
  src/main/java/com/biblioteca/{model,repository,controller}
  src/main/resources/application.properties
  src/test/java/com/biblioteca/
  src/test/resources/application-test.properties
frontend/
  package.json, package-lock.json, Dockerfile, nginx.conf
  public/index.html
  src/{components,services}
  src/App.js, src/App.css
.github/workflows/ci-cd.yml
docker-compose.yml
```

## Executar com Docker

Requer Docker Engine/Desktop iniciado e Docker Compose v2:

```sh
docker compose up -d --build
```

Acesse o frontend em http://localhost:3000 e a API em http://localhost:8080/api/autores. PostgreSQL usa a porta 5432, banco `biblioteca_db`, usuário e senha locais `postgres`. O volume `postgres_data` preserva os dados entre reinicializações. Estas credenciais são destinadas ao laboratório local.

```sh
docker compose logs -f backend
docker compose down
```

## Desenvolvimento local

Requer Java 17, Maven, Node.js 20 e npm. Inicie o banco:

```sh
docker compose up -d postgres
cd backend
mvn spring-boot:run
```

Em outro terminal, a partir da raiz:

```sh
cd frontend
npm ci
npm start
```

O servidor React usa `src/setupProxy.js` para encaminhar `/api` para `127.0.0.1:8080`. Em Docker, o Nginx encaminha para o serviço backend. Opcionalmente configure `REACT_APP_API_URL` no ambiente do build. O backend aceita `DB_URL`, `DB_USER` e `DB_PASSWORD` para configuração do banco.

## Testes e build

```sh
cd backend
mvn test
mvn test -Dtest='*ControllerTest'
mvn test -Dtest=IntegracaoTest
```

Os testes de integração usam H2 e não exigem PostgreSQL. Há pelo menos dois testes por controller (criação 201 e id inexistente 404) e integração do vínculo Livro → Autor.

```sh
cd frontend
npm ci
npm test -- --watchAll=false --passWithNoTests
npm run build
```

O frontend não possui suíte Jest própria nesta etapa; `--passWithNoTests` segue a orientação do laboratório. O workflow executa testes Java e build React em push/PR para `main`, além de permitir execução manual.

## API

| Método | Caminho | Resposta |
| --- | --- | --- |
| POST | `/api/autores`, `/api/livros` | 201 |
| GET | `/api/autores`, `/api/livros` | 200, ordenados por nome/título |
| GET | `/api/autores/{id}`, `/api/livros/{id}` | 200 ou 404 |
| PUT | `/api/autores/{id}`, `/api/livros/{id}` | 200 ou 404 |
| DELETE | `/api/autores/{id}`, `/api/livros/{id}` | 200 ou 404 |

Campos inválidos retornam 400. Um livro requer um autor existente; id de autor inexistente retorna 404. O PUT aceita os mesmos campos do POST e atualiza também o autor. Exclua os livros antes de excluir seu autor.

```sh
curl -i -X POST http://localhost:8080/api/autores \
  -H 'Content-Type: application/json' \
  -d '{"nome":"Machado de Assis","nacionalidade":"Brasileira","dataNascimento":"1839-06-21"}'

# Substitua 1 pelo id retornado acima.
curl -i -X POST http://localhost:8080/api/livros \
  -H 'Content-Type: application/json' \
  -d '{"titulo":"Dom Casmurro","isbn":"978-8535910667","anoPublicacao":1899,"genero":"Romance","autor":{"id":1}}'
```

## GitHub e evidências da entrega

### Justificativa para a ausência de Pull Requests e reviews formais

A dupla **Gustavo Lopes Oliveira e Heitor Canhestro Vieira Moreira** realizou a atividade em **pair programming**, alternando os papéis de **driver** (responsável pela implementação) e **navigator** (responsável pelo acompanhamento e revisão em tempo real). As decisões e a revisão do código foram feitas em conjunto durante o desenvolvimento, em vez de ocorrerem por Pull Requests e comentários de revisão no GitHub.

Por esse motivo, não foram registrados PRs e reviews formais. Apresentamos essa forma de colaboração como justificativa à avaliação do professor, reconhecendo que ela difere do fluxo de branches, PRs e revisão documentada exigido no enunciado.

Entregar:

- Link do repositório ou ZIP sem `node_modules` e `target`.
- Dois PRs, pelo menos um comentário de revisão do colega em cada um e merges.
- Print real do `mvn test` com `BUILD SUCCESS`.
- Prints de lista e formulário de Autores e de Livros, incluindo seleção do autor.
- Print real da API criando um livro vinculado a um autor.
- Print da aba Actions após a execução remota.

AWS não é exigida. As evidências estão em `prints/`; veja `REVISAO-ENTREGA.md` para o resultado da conferência e as pendências. A execução `fix #2` do GitHub Actions aprovou backend, frontend e aceitação em containers. Correções locais posteriores àquela execução precisam ser enviadas e verificadas novamente no CI.

## Extras incluídos na main

- `GET /api/livros/autor/{autorId}` retorna livros ordenados por título; autor sem livros ou inexistente retorna lista vazia (200).
- A tela de Livros oferece filtro por autor.
- Exclusão de autor com livros retorna 409 com mensagem amigável; a FK continua protegendo os dados.
- Testes adicionais verificam ordenação, filtro, CRUD completo de Autor, troca de autor no PUT, exclusão protegida e validações.
- `bash scripts/aceitacao.sh` verifica criação, vínculo, filtro e conflito contra uma API já iniciada. Requer curl e Python 3. Remove apenas os registros criados pelo próprio script. Opcional: `API_URL=http://localhost:3000/api bash scripts/aceitacao.sh` para verificar o proxy.
- O job extra `deploy` no GitHub Actions constrói os containers e executa a aceitação no runner descartável; não publica na AWS.

A execução local de containers não foi realizada. Inicie o Docker e os serviços manualmente quando desejar fazer a aceitação. Os extras estão na `main`; as branches originais não foram preservadas no histórico atual.

## Correções da revisão final

- Datas de nascimento usam JDBC 4.2 (`LocalDateJdbcType`) para manter o tipo SQL DATE e evitar conversão por instante em datas históricas. O teste verifica que `1839-06-21` permanece igual após salvar e consultar.
- O proxy de desenvolvimento usa `setupProxy.js`, evitando a falha de `allowedHosts` do react-scripts em ambientes sem endereço de rede detectável.
