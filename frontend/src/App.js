import { BrowserRouter, NavLink, Navigate, Route, Routes } from 'react-router-dom';
import AutorList from './components/AutorList';
import AutorForm from './components/AutorForm';
import LivroList from './components/LivroList';
import LivroForm from './components/LivroForm';
import './App.css';
export default function App() {
 return <BrowserRouter><nav><strong>Biblioteca<span>WEB</span></strong><NavLink to="/autores">Autores</NavLink><NavLink to="/livros">Livros</NavLink></nav><main><Routes><Route path="/" element={<Navigate to="/autores" replace/>}/><Route path="/autores" element={<AutorList/>}/><Route path="/autores/novo" element={<AutorForm/>}/><Route path="/autores/editar/:id" element={<AutorForm/>}/><Route path="/livros" element={<LivroList/>}/><Route path="/livros/novo" element={<LivroForm/>}/><Route path="/livros/editar/:id" element={<LivroForm/>}/><Route path="*" element={<p>Página não encontrada.</p>}/></Routes></main><footer>Biblioteca Web · Engenharia de Software</footer></BrowserRouter>;
}
