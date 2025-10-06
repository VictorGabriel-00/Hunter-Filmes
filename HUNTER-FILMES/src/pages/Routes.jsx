import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginScreen from '../components/Login/Login.jsx';
import EscolhaPerfil from '../components/Escolha-perfil/Escolha-perfil.jsx';
import CriarPerfil from '../components/CriarPerfil/Criar-perfil.jsx';
import App from '../App.jsx'; 
import PrimeiroPerfil from '../components/Primeiro-perfil/Primeiro-perfil.jsx';

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

          <Route path="cadastro" element={<LoginScreen />} />
        </Route>

        <Route path="*" element={<h1>404 - Página Não Encontrada</h1>} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
