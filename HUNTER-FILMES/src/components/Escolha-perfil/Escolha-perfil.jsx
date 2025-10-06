import React, { useState } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import './Escolha-perfil.css';
import CriarPerfil from '../CriarPerfil/Criar-perfil.jsx';

const EscolhaPerfil = () => {
   const { perfis, perfilSelecionado, handlePerfilClick } = useOutletContext();
  const [isCriarPerfil, setIsCriarPerfil] = useState(false);
  const navigate = useNavigate();

  const handleCriaPerfil = () => {
    setIsCriarPerfil(true);
  };

  const handleVoltarPerfis = () => {
    setIsCriarPerfil(false);
  };

  const isPerfilSelecionado = (perfil) => {
    return perfilSelecionado && (perfilSelecionado.id === perfil.id || perfilSelecionado.nome === perfil.nome);
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
            key={perfil.id}
            className={`perfil-opcao ${isPerfilSelecionado(perfil) ? 'selecionado' : ''}`}
            onClick={() => handlePerfilClick(perfil)}
          >
            <img 
              src={perfil.imagem} 
              alt={perfil.nome}
              onError={(e) => {
                console.log(`Erro ao carregar imagem para ${perfil.nome}:`, perfil.imagem);
                e.target.src = 'https://placehold.co/150x150/0f172a/ffffff?text=Avatar';
              }}
            />
             <p>{isPerfilSelecionado(perfil) ? 'Selecionado' : perfil.nome}</p>
          </div>
        ))}
      </div>
      <div className="Criar-perfil-container">
        <button onClick={handleCriaPerfil} className="Criar-perfil-button"> 
          +
        </button>
        <p className="Criar-perfil">Adicionar perfil</p>
      </div>
    </div>
  );
};

export default EscolhaPerfil;