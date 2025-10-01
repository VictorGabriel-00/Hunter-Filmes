import React, { useState } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import './Escolha-perfil.css';
import CriarPerfil from '../CriarPerfil/Criar-perfil.jsx';

const EscolhaPerfil = () => {
  const { perfis, imagensPerfis, perfilSelecionado, handlePerfilClick } = useOutletContext();
  const [isCriarPerfil, setIsCriarPerfil] = useState(false);
  const navigate = useNavigate();

  const handleCriaPerfil = () => {
    setIsCriarPerfil(true);
    
  };

  const handleVoltarPerfis = () => {
    setIsCriarPerfil(false);
  };

  if (isCriarPerfil) {
    return <CriarPerfil onVoltar={handleVoltarPerfis} />;
  }

  return (
    <div className="escolha-perfil">
      <h2>Escolha seu perfil</h2>
      <div className="perfil-opcoes">
        {perfis.map((perfil) => (
          <div
            key={perfil}
            className={`perfil-opcao ${perfilSelecionado === perfil ? 'selecionado' : ''}`}
            onClick={() => handlePerfilClick(perfil)}
          >
            <img 
              src={imagensPerfis[perfil]} 
              alt={perfil}
              onError={(e) => {
                console.log(`Erro ao carregar imagem para ${perfil}:`, imagensPerfis[perfil]);
                e.target.src = '/assets/perfil-default.jpg';
              }}
            />
            <p>{perfilSelecionado === perfil ? 'Selecionado' : perfil}</p>
          </div>
        ))}
      </div>
      <div className="criar-perfil-container">
        <button onClick={handleCriaPerfil} className="criar-perfil-button"> 
          +
        </button>
        <p className="criar-perfil">Adicionar perfil</p>
      </div>
    </div>
  );
};

export default EscolhaPerfil;