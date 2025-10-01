import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import './App.css';
import perfilVitu from './assets/perfilVitu.jpg';
import perfilRaica from './assets/perfilRaica.jpg';
import perfilPriori from './assets/perfilPriori.jpg';
import perfilMarcos from './assets/perfilMarcos.jpg';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [perfilSelecionado, setPerfilSelecionado] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  const perfis = ['Vitu', 'Raica', 'Priori', 'Marcos'];

  const imagensPerfis = {
    Vitu: perfilVitu,
    Raica: perfilRaica,
    Priori: perfilPriori,
    Marcos: perfilMarcos,
  };

  // Proteção de rota
  useEffect(() => {
    if (!isLoggedIn && location.pathname === '/perfis') {
      navigate('/login');
    }
  }, [isLoggedIn, location, navigate]);

  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
    navigate('/perfis'); 
  };

  const handlePerfilClick = (perfil) => {
    setPerfilSelecionado(perfil);
    console.log('Perfil selecionado:', perfil);
    // navigate('/home'); 
  };

  const handleCadastroSuccess = () => {
  setIsLoggedIn(true);
  navigate('/primeiro-perfil'); 
};


  const contextValue = {
    isLoggedIn,
    perfilSelecionado,
    perfis,
    imagensPerfis,
    handleLoginSuccess,
    handlePerfilClick,
    handleCadastroSuccess
  };

  return (
    <div className="app-container">
      <Outlet context={contextValue} />
    </div>
  );
}

export default App;