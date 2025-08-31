import React, { useState } from 'react';
import LoginScreen from './components/Login/Login.jsx';
import EscolhaPerfil from './components/Escolha-perfil/Escolha-perfil.jsx';
import perfilVitu from './assets/perfilVitu.jpg';
import perfilRaica from './assets/perfilRaica.jpg';
import perfilPriori from './assets/perfilPriori.jpg';
import perfilMarcos from './assets/perfilMarcos.jpg';
import './App.css';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [perfilSelecionado, setPerfilSelecionado] = useState(null);

  const perfis = ['Vitu', 'Raica', 'Priori', 'Marcos'];
  
  const imagensPerfis = {
    Vitu: perfilVitu,
    Raica: perfilRaica,
    Priori: perfilPriori,
    Marcos: perfilMarcos,
  };


  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
  };

  const handlePerfilClick = (perfil) => {
    setPerfilSelecionado(perfil);
    console.log('Perfil selecionado:', perfil);
  };

  return (
    <>
      {isLoggedIn ? (
        <EscolhaPerfil
          perfis={perfis}
          imagensPerfis={imagensPerfis}
          perfilSelecionado={perfilSelecionado}
          onPerfilClick={handlePerfilClick}
        />
      ) : (
        <LoginScreen onLoginSuccess={handleLoginSuccess} />
      )}
    </>
  );
}

export default App;