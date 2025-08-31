import React from 'react';
import './Escolha-perfil.css';

const EscolhaPerfil = ({ perfis, imagensPerfis, perfilSelecionado, onPerfilClick }) => {
  return (
    <div className="escolha-perfil">
      <h2>Escolha seu perfil</h2>
      <div className="perfil-opcoes">
        {perfis.map((perfil) => (
          <div
            key={perfil}
            className={`perfil-opcao ${perfilSelecionado === perfil ? 'selecionado' : ''}`}
            onClick={() => onPerfilClick(perfil)}
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
    </div>
  );
};

export default EscolhaPerfil;