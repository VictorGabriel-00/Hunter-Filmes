import React, { useState } from 'react';
import './Criar-perfil.css';

const CriarPerfil = ({ onVoltar, onPerfilCriado, isPrimeiroPerfil = false }) => {
  const [nomePerfil, setNomePerfil] = useState('');
  const [imagemSelecionada, setImagemSelecionada] = useState(null);

  const handleCriar = () => {
    if (!nomePerfil.trim()) {
      alert('Por favor, digite um nome para o perfil');
      return;
    }

    const novoPerfil = {
      nome: nomePerfil,
      imagem: imagemSelecionada,
    };

    console.log('Novo perfil:', novoPerfil);

    if (isPrimeiroPerfil && onPerfilCriado) {
      onPerfilCriado();
    } else if (onVoltar) {
      onVoltar();
    }
  };

  return (
    <div className="criar-perfil-page">

      <div className="selecao-avatar">
          
          <div className="avatares-grid">
            <div className="avatar-opcao">
              
              <div className="avatar-placeholder"> +
              <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => {
                    const file = e.target.files[0];
                    if (file) {
                      const reader = new FileReader();
                      reader.onloadend = () => {
                        setImagemSelecionada(reader.result);
                      };
                      reader.readAsDataURL(file);
                    }
                  }}
                />
              </div>
            </div>
          </div>
      </div>
      
      <div className="form-criar-perfil">
        <div className="campo-nome">
          <label>Nome</label>
          <input
            type="text"
            value={nomePerfil}
            onChange={(e) => setNomePerfil(e.target.value)}
            placeholder="Digite o nome"
            maxLength={20}
          />
        </div>

        <div className="botoes-acao">
          <button onClick={handleCriar} className="Criar">
            Criar
          </button>
        </div>
      </div>
    </div>
  );
};

export default CriarPerfil;