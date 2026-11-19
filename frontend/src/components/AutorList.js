import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { autorService, mensagemErro } from '../services/api';
export default function AutorList() {
 const [autores, setAutores] = useState([]), [erro, setErro] = useState(''), [carregando, setCarregando] = useState(true);
 const carregar = () => autorService.listar().then(r => setAutores(r.data)).catch(e => setErro(mensagemErro(e))).finally(() => setCarregando(false));
 useEffect(() => { carregar(); }, []);
 async function excluir(autor) {
  if (!window.confirm(`Excluir ${autor.nome}? Exclua os livros deste autor primeiro.`)) return;
  try { await autorService.deletar(autor.id); setErro(''); carregar(); } catch(e) { setErro(mensagemErro(e)); }
 }
 return <><header><div><p className="eyebrow">ACERVO DIGITAL</p><h1>Autores</h1><p>As pessoas por trás das histórias.</p></div><Link className="button" to="/autores/novo">Novo autor</Link></header>
 {erro && <p role="alert" className="error">{erro}</p>}
 {carregando ? <p>Carregando autores…</p> : <div className="panel"><table><thead><tr><th>Nome</th><th>Nacionalidade</th><th>Nascimento</th><th>Ações</th></tr></thead><tbody>{autores.map(a => <tr key={a.id}><td>{a.nome}</td><td>{a.nacionalidade || '—'}</td><td>{a.dataNascimento || '—'}</td><td className="actions"><Link to={`/autores/editar/${a.id}`}>Editar</Link><button className="danger" onClick={() => excluir(a)}>Excluir</button></td></tr>)}</tbody></table>{!autores.length && <p className="empty">Nenhum autor cadastrado. Adicione o primeiro autor.</p>}</div>}</>;
}
