import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { autorService, mensagemErro } from '../services/api';
export default function AutorForm() {
 const {id} = useParams(), navigate = useNavigate();
 const [dados, setDados] = useState({nome:'', nacionalidade:'', dataNascimento:''});
 const [erro, setErro] = useState(''), [salvando, setSalvando] = useState(false), [carregando, setCarregando] = useState(!!id);
 useEffect(() => { if(id) autorService.buscar(id).then(r => setDados({nome:r.data.nome, nacionalidade:r.data.nacionalidade || '', dataNascimento:r.data.dataNascimento || ''})).catch(e => setErro(mensagemErro(e))).finally(() => setCarregando(false)); }, [id]);
 const mudar = e => setDados({...dados, [e.target.name]:e.target.value});
 async function salvar(e) { e.preventDefault(); setSalvando(true); try { const payload={...dados, dataNascimento:dados.dataNascimento || null}; await (id ? autorService.atualizar(id,payload) : autorService.criar(payload)); navigate('/autores'); } catch(e) { setErro(mensagemErro(e)); } finally { setSalvando(false); } }
 return <><h1>{id ? 'Editar autor' : 'Novo autor'}</h1>{erro && <p className="error" role="alert">{erro}</p>}<form className="panel form" onSubmit={salvar}><fieldset disabled={carregando || salvando}><label>Nome<input name="nome" required maxLength={100} value={dados.nome} onChange={mudar}/></label><label>Nacionalidade<input name="nacionalidade" maxLength={50} value={dados.nacionalidade} onChange={mudar}/></label><label>Data de nascimento<input type="date" name="dataNascimento" value={dados.dataNascimento} onChange={mudar}/></label><div className="actions"><button type="submit">{salvando ? 'Salvando…' : 'Salvar autor'}</button><Link to="/autores">Cancelar</Link></div></fieldset></form></>;
}
