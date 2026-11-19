import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { autorService, livroService, mensagemErro } from '../services/api';
export default function LivroList() {
 const [livros, setLivros] = useState([]), [erro, setErro] = useState(''), [carregando, setCarregando] = useState(true);
 const [autores, setAutores] = useState([]), [autorId, setAutorId] = useState('');
 const carregar = useCallback(async () => {
  setCarregando(true);
  try { const r = await (autorId ? livroService.porAutor(autorId) : livroService.listar()); setLivros(r.data); setErro(''); }
  catch(e) { setErro(mensagemErro(e)); }
  finally { setCarregando(false); }
 }, [autorId]);
 useEffect(() => { carregar(); }, [carregar]);
 useEffect(() => { autorService.listar().then(r => setAutores(r.data)).catch(e => setErro(mensagemErro(e))); }, []);
 async function excluir(livro) {
  if (!window.confirm(`Excluir ${livro.titulo}?`)) return;
  try { await livroService.deletar(livro.id); setErro(''); carregar(); } catch(e) { setErro(mensagemErro(e)); }
 }
 return <><header><div><p className="eyebrow">ACERVO DIGITAL</p><h1>Livros</h1><p>Organize títulos, autores e novas descobertas.</p></div><Link className="button" to="/livros/novo">Novo livro</Link></header>
 <label className="filter">Filtrar por autor<select value={autorId} onChange={e => setAutorId(e.target.value)}><option value="">Todos os autores</option>{autores.map(a => <option key={a.id} value={a.id}>{a.nome}</option>)}</select></label>
 {erro && <p role="alert" className="error">{erro}</p>}
 {carregando ? <p>Carregando livros…</p> : <div className="panel"><table><thead><tr><th>Título</th><th>Autor</th><th>Ano</th><th>Gênero</th><th>ISBN</th><th>Ações</th></tr></thead><tbody>{livros.map(l => <tr key={l.id}><td>{l.titulo}</td><td>{l.autor.nome}</td><td>{l.anoPublicacao ?? '—'}</td><td>{l.genero || '—'}</td><td>{l.isbn || '—'}</td><td className="actions"><Link to={`/livros/editar/${l.id}`}>Editar</Link><button className="danger" onClick={() => excluir(l)}>Excluir</button></td></tr>)}</tbody></table>{!livros.length && <p className="empty">Nenhum livro cadastrado. Cadastre um autor e adicione seu primeiro livro.</p>}</div>}</>;
}
