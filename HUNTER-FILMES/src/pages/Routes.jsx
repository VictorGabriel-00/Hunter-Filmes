import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginScreen from '../components/Login/Login.jsx';
import EscolhaPerfil from '../components/Escolha-perfil/Escolha-perfil.jsx';
import CriarPerfil from '../components/CriarPerfil/Criar-perfil.jsx';
import App from '../App.jsx'; 
import PrimeiroPerfil from '../components/Primeiro-perfil/Primeiro-perfil.jsx';
import TelaInicial from '../components/TelaInicial/Tela-inicial.jsx';
import DetalheMedia from '../components/DetalheMedia/DetalheMedia.jsx';
// import MinhaLista from '../components/MinhaLista/MinhaLista.jsx'; // <-- REMOVA ESTA LINHA

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        {}
        <Route path="/" element={<App />}> 
          
          <Route index element={<LoginScreen />} />
          <Route path="login" element={<LoginScreen />} />
          <Route path="perfis" element={<EscolhaPerfil />} />
          
          <Route path="criar-perfil" element={<CriarPerfil />} />
          <Route path="primeiro-perfil" element={<PrimeiroPerfil/>} />
          <Route path="home" element={<TelaInicial />} />
          <Route path="filmes" element={<TelaInicial />} />
          <Route path="series" element={<TelaInicial />} />
          <Route path="cadastro" element={<LoginScreen />} />

          <Route path="media/:title" element={<DetalheMedia />} />
          
          {/* --- ESTA É A LINHA MODIFICADA --- */}
          <Route path="minha-lista" element={<TelaInicial />} /> 
          {/* --- FIM DA MODIFICAÇÃO --- */}

        </Route>

        <Route path="*" element={<h1>404 - Página Não Encontrada</h1>} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;