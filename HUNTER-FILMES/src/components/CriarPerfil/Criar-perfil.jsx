import React, { useState } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import './Criar-perfil.css';

const CriarPerfil = ({ onVoltar, onPerfilCriado, isPrimeiroPerfil = false }) => {
  
  const { 
    adicionarNovoPerfil, 
    resetPerfisAndAddFirst,
  } = useOutletContext();

  const [nomePerfil, setNomePerfil] = useState('');
  const [imagemSelecionada, setImagemSelecionada] = useState(null);
  const [mensagem, setMensagem] = useState(null);
  const navigate = useNavigate();

  const showMessage = (text) => {
    setMensagem(text);
    setTimeout(() => setMensagem(null), 3000); 
  };

  const handleCriar = () => {
    if (!nomePerfil.trim()) {
      showMessage('Por favor, digite um nome para o perfil');
      return;
    }  

    const novoPerfil = { 
      nome: nomePerfil, 
      imagem: imagemSelecionada || 'https://placehold.co/150x150/0f172a/ffffff?text=New', 
    };
    
    console.log('Dados a serem enviados:', novoPerfil, 'Primeiro Perfil:', isPrimeiroPerfil);  

    if (isPrimeiroPerfil) {
      resetPerfisAndAddFirst(novoPerfil);
    } else {
      adicionarNovoPerfil(novoPerfil);
    }

    showMessage(`Perfil "${nomePerfil}" criado com sucesso!`);

    // ✅ CORRIGIDO: Aguarda um pouco antes de redirecionar
    setTimeout(() => {
      if (isPrimeiroPerfil && onPerfilCriado) {
        onPerfilCriado();
      } else if (onVoltar) {
        onVoltar();
      } else {
        // ✅ Fallback: se não houver callback, redireciona para /perfis
        navigate('/perfis');
      }
    }, 500); // Aguarda 500ms para garantir que o estado foi atualizado
  };

  return (
    <div className="modal-overlay">
    <div className="criar-perfil-page">
      {mensagem && (
        <div className="mensagem-sucesso">
          {mensagem}
        </div>
      )}

      <div className="selecao-avatar">
        <div className="avatares-grid">
          <div className="avatar-opcao">
            
            <div className="avatar-placeholder">
              {imagemSelecionada ? 
                <img src={imagemSelecionada} alt="Avatar selecionado" /> : 
                '+'
              }
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
            <p className='texto-adicionar-foto'>Adicionar foto</p>
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
            maxLength={20}
          />
        </div>

        <div>
          <button onClick={handleCriar} className="criar">
            Criar
          </button>
        </div>
      </div>
      </div>
    </div>
  );
};

export default CriarPerfil;