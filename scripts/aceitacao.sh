#!/usr/bin/env bash
# Executar somente contra uma instância de teste já iniciada pelo usuário.
set -euo pipefail
API="${API_URL:-http://localhost:8080/api}"
AUTOR_ID=""
LIVRO_ID=""
limpar() {
  if [ -n "$LIVRO_ID" ]; then curl -fsS -X DELETE "$API/livros/$LIVRO_ID" >/dev/null || true; fi
  if [ -n "$AUTOR_ID" ]; then curl -fsS -X DELETE "$API/autores/$AUTOR_ID" >/dev/null || true; fi
}
trap limpar EXIT
extrair_id() { python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])'; }
AUTOR=$(curl -fsS -X POST "$API/autores" -H 'Content-Type: application/json' -d '{"nome":"Autor do teste de aceitação"}')
AUTOR_ID=$(printf '%s' "$AUTOR" | extrair_id)
LIVRO=$(curl -fsS -X POST "$API/livros" -H 'Content-Type: application/json' -d "{\"titulo\":\"Livro de aceitação\",\"autor\":{\"id\":$AUTOR_ID}}")
LIVRO_ID=$(printf '%s' "$LIVRO" | extrair_id)
printf '%s' "$LIVRO" | python3 -c 'import json,sys; livro=json.load(sys.stdin); assert livro["autor"]["id"] == int(sys.argv[1]); print("Vínculo confirmado:", livro)' "$AUTOR_ID"
STATUS=$(curl -sS -o /dev/null -w '%{http_code}' -X DELETE "$API/autores/$AUTOR_ID")
[ "$STATUS" = "409" ] || { echo "Esperado 409 ao excluir autor com livro; recebido $STATUS"; exit 1; }
curl -fsS "$API/livros/autor/$AUTOR_ID" | python3 -c 'import json,sys; livros=json.load(sys.stdin); assert len(livros) == 1; assert livros[0]["id"] == int(sys.argv[1]); print("Filtro confirmado.")' "$LIVRO_ID"
echo 'ACEITAÇÃO OK'
