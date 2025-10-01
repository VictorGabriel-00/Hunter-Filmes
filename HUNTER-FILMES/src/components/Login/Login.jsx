import React, { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { FaGoogle, FaFacebook } from 'react-icons/fa';
import './Login.css';
import Cadastro from '../Cadastros/Cadastro';

const LoginScreen = () => {
  const { handleLoginSuccess } = useOutletContext(); 
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [isCadastro, setIsCadastro] = useState(false);

  const handleLogin = () => {
    setLoading(true);
      
    setTimeout(() => {
      console.log('Login:', { usuario, senha });
      setLoading(false);
      
      if (usuario === 'teste' && senha === '123') {
        handleLoginSuccess(); 
      } else {
        alert('Usuário ou senha inválidos!');
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
      {isCadastro ? (
        <Cadastro onVoltar={handleVoltarParaLogin} />
      ) : (
        <div className="login-container">
          <h2>Usuario</h2>
          <input
            type="text"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
          />
          <h2 className='senha'>Senha </h2>
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />
          <button onClick={handleLogin} disabled={loading} className="login">
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

export default LoginScreen;