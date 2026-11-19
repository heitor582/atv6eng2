import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { autorService, livroService, mensagemErro } from '../services/api';
export default function LivroForm() {
 const {id} = useParams(), navigate = useNavigate();
 const [dados, setDados] = useState({titulo:'', isbn:'', anoPublicacao:'', genero:'', autorId:''});
 const [autores, setAutores] = useState([]), [erro, setErro] = useState(''), [salvando, setSalvando] = useState(false), [carregando, setCarregando] = useState(true);
 useEffect(() => {
  Promise.all([autorService.listar(), id ? livroService.buscar(id) : Promise.resolve(null)]).then(([a,l]) => {
   setAutores(a.data); if(l) setDados({titulo:l.data.titulo, isbn:l.data.isbn || '', anoPublicacao:l.data.anoPublicacao ?? '', genero:l.data.genero || '', autorId:String(l.data.autor.id)});
  }).catch(e => setErro(mensagemErro(e))).finally(() => setCarregando(false));
 }, [id]);
 const mudar = e => setDados({...dados, [e.target.name]:e.target.value});
 async function salvar(e) {
  e.preventDefault(); setSalvando(true);
  const payload={titulo:dados.titulo, isbn:dados.isbn, genero:dados.genero, anoPublicacao:dados.anoPublicacao === '' ? null : Number(dados.anoPublicacao), autor:{id:Number(dados.autorId)}};
  try { await (id ? livroService.atualizar(id,payload) : livroService.criar(payload)); navigate('/livros'); } catch(e) { setErro(mensagemErro(e)); } finally { setSalvando(false); }
 }
 return <><h1>{id ? 'Editar livro' : 'Novo livro'}</h1>{erro && <p className="error" role="alert">{erro}</p>}{!carregando && !autores.length && <p>Você precisa <Link to="/autores/novo">cadastrar um autor</Link> antes de salvar um livro.</p>}
 <form className="panel form" onSubmit={salvar}><fieldset disabled={carregando || salvando}>
 <label>Título<input name="titulo" required maxLength={200} value={dados.titulo} onChange={mudar}/></label>
 <label>Autor<select name="autorId" required value={dados.autorId} onChange={mudar}><option value="">Selecione um autor</option>{autores.map(a => <option value={a.id} key={a.id}>{a.nome}</option>)}</select></label>
 <label>ISBN<input name="isbn" maxLength={20} value={dados.isbn} onChange={mudar}/></label>
 <label>Ano de publicação<input type="number" step="1" name="anoPublicacao" value={dados.anoPublicacao} onChange={mudar}/></label>
 <label>Gênero<input name="genero" maxLength={50} value={dados.genero} onChange={mudar}/></label>
 <div className="actions"><button type="submit" disabled={!autores.length}>{salvando ? 'Salvando…' : 'Salvar livro'}</button><Link to="/livros">Cancelar</Link></div></fieldset></form></>;
}
