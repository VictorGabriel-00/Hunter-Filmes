import React, { useState, useEffect, useMemo } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import './App.css';
import perfilVito from './assets/perfilVitu.jpg'
import perfilRaica from './assets/perfilRaica.jpg'
import perfilPriori from './assets/perfilPriori.jpg'
import perfilMarcos from './assets/perfilMarcos.jpg'

const LOGGED_IN_ID_KEY = 'loggedInUserId';
const USERS_LIST_KEY = 'appUserAccounts';
const getStorageKey = (userId) => `appPerfis_${userId}`;

const PRE_CADASTRADO_USER_ID = 'user123';

function App() {
  const navigate = useNavigate();
  const location = useLocation();

  const [userId, setUserId] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [perfilSelecionado, setPerfilSelecionado] = useState(null);

  // Define os perfis iniciais ANTES de usar em qualquer lugar
  const initialPerfis = useMemo(() => [
    { id: 1, nome: 'Vitu', imagem: perfilVito},
    { id: 2, nome: 'Raica', imagem: perfilRaica},
    { id: 3, nome: 'Priori', imagem: perfilPriori },
    { id: 4, nome: 'Marcos', imagem: perfilMarcos },
  ], []);

  // AGORA inicializa o usuário pré-cadastrado (depois de initialPerfis estar definido)
  useEffect(() => {
    const users = JSON.parse(localStorage.getItem(USERS_LIST_KEY) || '[]');
  
    const precadastradoExists = users.find(u => u.email === 'teste@teste.com');
    
    if (!precadastradoExists) {
      const precadastradoUser = {
        nome: 'teste',
        email: 'teste@teste.com',
        senha: '123',
        nascimento: '2006-04-21',
        userId: PRE_CADASTRADO_USER_ID
      };

      localStorage.setItem(USERS_LIST_KEY, JSON.stringify([...users, precadastradoUser]));
      console.log('Usuario pre-cadastrado criado: teste@teste.com / 123');
      
      // Salva os perfis pré-cadastrados para este usuário
      const key = getStorageKey(PRE_CADASTRADO_USER_ID);
      localStorage.setItem(key, JSON.stringify(initialPerfis));
      console.log('Perfis pre-cadastrados salvos para user123');
    } else if (precadastradoExists && !precadastradoExists.userId) {
      // Se o usuário existe mas não tem userId, adiciona
      precadastradoExists.userId = PRE_CADASTRADO_USER_ID;
      const updatedUsers = users.map(u => 
        u.email === 'teste@teste.com' ? precadastradoExists : u
      );
      localStorage.setItem(USERS_LIST_KEY, JSON.stringify(updatedUsers));
      console.log('userId adicionado ao usuario pre-cadastrado');
    }
  }, [initialPerfis]);

  const [listaPerfis, setListaPerfis] = useState(() => {
    const storedId = localStorage.getItem(LOGGED_IN_ID_KEY);
    if (storedId) {
      try {
        const key = getStorageKey(storedId);
        const storedPerfis = localStorage.getItem(key);

        if (storedId === PRE_CADASTRADO_USER_ID && !storedPerfis) {
          return initialPerfis;
        }

        return storedPerfis ? JSON.parse(storedPerfis) : []; 
      } catch (e) {
        console.error("Erro ao carregar perfis do localStorage:", e);
        return [];
      }
    }
    return [];
  });

  useEffect(() => {
    if (userId) {
      try {
        const key = getStorageKey(userId);
        localStorage.setItem(key, JSON.stringify(listaPerfis));
        console.log('Perfis salvos:', listaPerfis);
      } catch (e) {
        console.error("Erro ao salvar perfis no localStorage:", e);
      }
    }
  }, [listaPerfis, userId]);

  const updatePerfis = (newPerfis) => {
    console.log('Atualizando perfis para:', newPerfis);
    setListaPerfis(newPerfis);
  };

  const adicionarNovoPerfil = (novoPerfil) => { 
    if (!userId) {
      console.error("Tentativa de adicionar perfil sem usuário logado.");
      return;
    }

    if (novoPerfil && novoPerfil.nome) {
      const perfilParaSalvar = { 
        id: Date.now(), 
        ...novoPerfil
      };
      
      console.log('Adicionando novo perfil:', perfilParaSalvar);
      console.log('Lista atual:', listaPerfis);
      updatePerfis([...listaPerfis, perfilParaSalvar]);
    }
  }; 

  const resetPerfisAndAddFirst = (novoPerfil) => {
    if (novoPerfil && novoPerfil.nome) {
      const perfilParaSalvar = { 
        id: Date.now(), 
        ...novoPerfil
      };
      updatePerfis([perfilParaSalvar]);
    }
  };

  const handleLoginSuccess = () => {
    let newUserId = localStorage.getItem(LOGGED_IN_ID_KEY);
    
    console.log('handleLoginSuccess - userId do localStorage:', newUserId);
    console.log('PRE_CADASTRADO_USER_ID esperado:', PRE_CADASTRADO_USER_ID);
    
    setUserId(newUserId);
    setIsLoggedIn(true);

    const key = getStorageKey(newUserId);
    const storedPerfis = localStorage.getItem(key);
    
    console.log('Buscando perfis com key:', key);
    console.log('Perfis encontrados no localStorage:', storedPerfis);
    
    let loadedPerfis;
    if (storedPerfis) {
      loadedPerfis = JSON.parse(storedPerfis);
      console.log('Perfis carregados do localStorage:', loadedPerfis);
    } else if (newUserId === PRE_CADASTRADO_USER_ID) {
      console.log('Usuario pre-cadastrado detectado! Carregando perfis iniciais...');
      loadedPerfis = initialPerfis;
      localStorage.setItem(key, JSON.stringify(initialPerfis));
    } else {
      console.log('Nenhum perfil encontrado e nao e usuario pre-cadastrado');
    }

    if (!loadedPerfis || loadedPerfis.length === 0) {
      console.log('Nenhum perfil encontrado, redirecionando para criar primeiro perfil');
      updatePerfis([]);
      navigate('/primeiro-perfil');
    } else {
      console.log('Perfis carregados com sucesso:', loadedPerfis);
      updatePerfis(loadedPerfis);
      navigate('/perfis'); 
    }
    
    localStorage.removeItem('temp_logged_email');
  };

  const handleCadastroSuccess = () => {
    const newUserId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
    
    localStorage.setItem(LOGGED_IN_ID_KEY, newUserId);
    setUserId(newUserId);
    setIsLoggedIn(true);
    
    updatePerfis([]);
    
    navigate('/primeiro-perfil'); 
  };

  const handleLogout = () => {
    localStorage.removeItem(LOGGED_IN_ID_KEY); 
    
    setUserId(null);
    setIsLoggedIn(false);
    setListaPerfis(initialPerfis); 
    setPerfilSelecionado(null);
    
    navigate('/login');
  };

  useEffect(() => {
    if (!isLoggedIn) {
      if (location.pathname === '/perfis' || location.pathname === '/primeiro-perfil' || location.pathname === '/home') {
        navigate('/login');
      }
    }
    
    if (isLoggedIn && listaPerfis.length === 0 && location.pathname !== '/primeiro-perfil') {
      navigate('/primeiro-perfil');
    }
  }, [isLoggedIn, location, navigate, listaPerfis.length]);

  const handlePerfilClick = (perfilObj) => { 
    setPerfilSelecionado(perfilObj);
    console.log('Perfil selecionado:', perfilObj);
    //navigate('/home');
  };  

  const contextValue = {
    isLoggedIn,
    perfilSelecionado,
    perfis: listaPerfis,
    handleLoginSuccess,
    handlePerfilClick,
    handleCadastroSuccess,
    adicionarNovoPerfil,
    resetPerfisAndAddFirst,
  };

  return (
    <div className="app-container">
      <Outlet context={contextValue} />
    </div>
  );
}

export default App;