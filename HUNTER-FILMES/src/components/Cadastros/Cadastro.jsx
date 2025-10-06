import React, { useState } from 'react';
import './Cadastro.css';
import { FaGoogle, FaFacebook } from 'react-icons/fa';
import { useOutletContext, useNavigate } from 'react-router-dom';

const USERS_LIST_KEY = 'appUserAccounts';

const Cadastro = () => {
  const [Usuario, setUsuario] = useState('');
  const [email, setEmail] = useState('');
  const [nascimento, setNascimento] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState(null); 
  const { handleCadastroSuccess } = useOutletContext();

  const showMessage = (text, isSuccess = false) => {
    setMensagem({ text, isSuccess });
    setTimeout(() => setMensagem(null), 4000);
  };

  const handleCadastro = () => {
    if (!Usuario || !email || !nascimento || !senha || !confirmarSenha) {
      showMessage('Por favor, preencha todos os campos.', false);
      return;
    }

    if (senha !== confirmarSenha) {
      showMessage('As senhas não coincidem.', false);
      return;
    }
    
    setLoading(true);

    setTimeout(() => {
      setLoading(false);
      try {
        const storedUsers = JSON.parse(localStorage.getItem(USERS_LIST_KEY) || '[]');
                
        // Verificar se o e-mail já existe
        if (storedUsers.find(user => user.email === email)) {
          showMessage("Este e-mail já está cadastrado!", false);
          return;
        }

        // Salvar o novo usuário no localStorage
        const newUser = { 
          nome: Usuario, 
          email: email, 
          senha: senha,
          nascimento: nascimento
        };
                
        localStorage.setItem(USERS_LIST_KEY, JSON.stringify([...storedUsers, newUser]));
                
        // Chamar a função de sucesso
        handleCadastroSuccess(); 

      } catch (e) {
        console.error("Erro ao salvar cadastro:", e);
        showMessage("Erro interno ao realizar o cadastro.", false);
      }
    }, 1000);
  };

  const handleGoogleLogin = () => {
    console.log('Login com Google');
  };

  const handleFacebookLogin = () => {
    console.log('Login com Facebook');
  };

  return (
    <>
      {mensagem && (
        <div className={mensagem.isSuccess ? 'mensagem-sucesso' : 'mensagem-erro'}>
          {mensagem.text}
        </div>
      )}
      
      <div className="cadastro-container">
        <h2>Usuario</h2>
        <input
          id="usuario"
          type="text"
          value={Usuario}
          onChange={(e) => setUsuario(e.target.value)}
        />
        <h2>Email</h2>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        
        <h2>Senha</h2>
        <input
          id="senha"
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
        />
        <h2>Confirmar Senha</h2>
        <input
          id="confirmarSenha"
          type="password"
          value={confirmarSenha}
          onChange={(e) => setConfirmarSenha(e.target.value)}
        />
        <h2>Data de Nascimento</h2>
        <input
          id="nascimento"
          type="date"
          value={nascimento}
          onChange={(e) => setNascimento(e.target.value)}
          className='data-nascimento'
        />
        <button onClick={handleCadastro} disabled={loading} className="cadastrar">
          {loading ? 'Cadastrando...' : 'Cadastrar-se'}
        </button>
        <hr />
        <button onClick={handleGoogleLogin} className="google-login">
          <FaGoogle className='icon' />
          Google
        </button>
        <button onClick={handleFacebookLogin} className="facebook-login">
          <FaFacebook className='icon' />
          Facebook
        </button>
      </div>
    </>
  );
};

export default Cadastro;