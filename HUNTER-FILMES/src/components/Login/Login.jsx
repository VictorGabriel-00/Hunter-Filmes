import React, { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { FaGoogle, FaFacebook } from 'react-icons/fa';
import './Login.css';
import Cadastro from '../Cadastros/Cadastro';

const USERS_LIST_KEY = 'appUserAccounts';
const LOGGED_IN_ID_KEY = 'loggedInUserId';

const Login = () => {
  const { handleLoginSuccess, handleCadastroSuccess } = useOutletContext();
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [isCadastro, setIsCadastro] = useState(false);
  const [mensagem, setMensagem] = useState(null);

  const showMessage = (text, isSuccess = false) => {
    setMensagem({ text, isSuccess });
    setTimeout(() => setMensagem(null), 4000);
  };



  const handleLogin = () => {
    setLoading(true);
      
    setTimeout(() => {
      setLoading(false);
      const storedUsers = JSON.parse(localStorage.getItem(USERS_LIST_KEY) || '[]');
            
      const userFound = storedUsers.find(
        user => user.email === usuario && user.senha === senha
      );

      if (userFound) {
        showMessage('Login bem-sucedido! Redirecionando...', true);
        
        // Salva o userId do usuário encontrado
        if (userFound.userId) {
          localStorage.setItem(LOGGED_IN_ID_KEY, userFound.userId);
        } else {
          // Se o usuário não tem userId, cria um
          const newUserId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
          userFound.userId = newUserId;
          
          // Atualiza o usuário na lista
          const updatedUsers = storedUsers.map(u => 
            u.email === userFound.email ? userFound : u
          );
          localStorage.setItem(USERS_LIST_KEY, JSON.stringify(updatedUsers));
          localStorage.setItem(LOGGED_IN_ID_KEY, newUserId);
        }
        
        // Salva o email temporariamente para o App.jsx usar
        localStorage.setItem('temp_logged_email', userFound.email);
        
        setTimeout(() => {
          handleLoginSuccess();
        }, 500);
      } else {
        showMessage('Usuário ou senha inválidos!', false); 
      }
    }, 1000);
  };

  const handleGoogleLogin = () => {
    console.log('Login com Google');
    handleLoginSuccess();
  };

  const handleFacebookLogin = () => {
    console.log('Login com Facebook');
    handleLoginSuccess();
  };

  const handleCadastro = () => {
    setIsCadastro(true);
  };

  const handleVoltarParaLogin = () => {
    setIsCadastro(false);
  };

  return (
  
    <>
  
      {mensagem && (
        <div className={mensagem.isSuccess ? 'mensagem-sucesso' : 'mensagem-erro'}>
          {mensagem.text}
        </div>
      )}

      {isCadastro ? (
        <Cadastro onVoltar={handleVoltarParaLogin} />
      ) : (
        <div className="login-container">
          <h2>Usuario</h2>
          <input
            id="usuario"
            type="text"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
          />
          <h2 className='senha'>Senha</h2>
          <input
            id="senha"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />
          <button id="login" onClick={handleLogin} disabled={loading} className="login">
            {loading ? 'Entrando...' : 'Login'}
          </button>
          <button className="cadastro" onClick={handleCadastro}>
            Cadastrar-se
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
      )}
    </>
  );
};

export default Login;