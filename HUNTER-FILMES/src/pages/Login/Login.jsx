import {FaGoogle, FaFacebook} from 'react-icons/fa';
import React, { useState } from 'react';
import './Login.css';

const LoginScreen = () => {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = () => {
    setLoading(true);
    
    // Simular chamada de API
    setTimeout(() => {
      console.log('Login:', { usuario, senha });
      setLoading(false);
      // Aqui você faria a integração com sua API
    }, 1000);
  };

  const handleGoogleLogin = () => {
    console.log('Login com Google');
    // Integração com Google OAuth
  };

  const handleFacebookLogin = () => {
    console.log('Login com Facebook');
    // Integração com Facebook Login
  };

  const handleCadastro = () => {
    console.log('Redirecionando para cadastro');
    // Navegação para tela de cadastro
  };

    return (
        
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
        <button  onClick={handleGoogleLogin} className="google-login">
            <FaGoogle className='icon' />
            Google
        </button>
        <button  onClick={handleFacebookLogin} className="facebook-login">
            <FaFacebook className='icon'  />
            Facebook
        </button>
        </div>        
        
    );
};

export default LoginScreen;