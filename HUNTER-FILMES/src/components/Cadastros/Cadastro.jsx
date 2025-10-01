import React, { useState } from 'react';
import './Cadastro.css';
import { FaGoogle, FaFacebook } from 'react-icons/fa';
import {useOutletContext, useNavigate } from 'react-router-dom';

const Cadastro = () => {
  const [Usuario, setUsuario] = useState('');
  const [email, setEmail] = useState('');
  const [nascimento, setNascimento] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const { handleCadastroSuccess } = useOutletContext();

  const handleCadastro = () => {
    setLoading(true);

    setTimeout(() => {
      console.log('Cadastro:', { Usuario, email, nascimento, senha, confirmarSenha });
      setLoading(false);

      if (Usuario !== '' && email !== '' && nascimento !== '' && senha !== '' && confirmarSenha === senha ) {
        handleCadastroSuccess(); 
      } else {
        alert('Preencha todos os campos corretamente!');
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
    <div className="cadastro-container">
      <h2>Usuario</h2>
      <input
        type="text"
        value={Usuario}
        onChange={(e) => setUsuario(e.target.value)}
      />
      <h2>Email</h2>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      
      <h2>Senha</h2>
      <input
        type="password"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
      />
      <h2>Confirmar Senha</h2>
      <input
        type="password"
        value={confirmarSenha}
        onChange={(e) => setConfirmarSenha(e.target.value)}
      />
      <h2>Data de Nascimento</h2>
      <input
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
  );
};

export default Cadastro;
