import React, { useState, useEffect, useMemo } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { FaStar, FaBars, FaTimes } from 'react-icons/fa'; 
import './App.css';
import perfilVito from './assets/perfilVitu.jpg';
import perfilRaica from './assets/perfilRaica.jpg';
import perfilPriori from './assets/perfilPriori.jpg';
import perfilMarcos from './assets/perfilMarcos.jpg';

const LOGGED_IN_ID_KEY = 'loggedInUserId';
const USERS_LIST_KEY = 'appUserAccounts';
const getStorageKey = (userId) => `appPerfis_${userId}`;
const getMinhaListaKey = (userId) => `appMinhaLista_${userId}`; 

const PRE_CADASTRADO_USER_ID = 'user123';

function App() {
  const navigate = useNavigate();
  const location = useLocation();

  const [userId, setUserId] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [perfilSelecionado, setPerfilSelecionado] = useState(null);
  const [minhaLista, setMinhaLista] = useState([]);
  const [menuAberto, setMenuAberto] = useState(false);

  const toggleMenu = () => {
    setMenuAberto(!menuAberto);
  };

  const handleNavigate = (path) => {
    navigate(path);
    setMenuAberto(false);
  };

  const inicialPerfis = useMemo(() => [
    { id: 1, nome: 'Vitu', imagem: perfilVito},
    { id: 2, nome: 'Raica', imagem: perfilRaica},
    { id: 3, nome: 'Priori', imagem: perfilPriori },
    { id: 4, nome: 'Marcos', imagem: perfilMarcos },
  ], []);

  // (useEffect de inicialização do usuário pré-cadastrado - sem alterações)
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
      const key = getStorageKey(PRE_CADASTRADO_USER_ID);
      localStorage.setItem(key, JSON.stringify(inicialPerfis));
    } else if (precadastradoExists && !precadastradoExists.userId) {
      precadastradoExists.userId = PRE_CADASTRADO_USER_ID;
      const updatedUsers = users.map(u => u.email === 'teste@teste.com' ? precadastradoExists : u);
      localStorage.setItem(USERS_LIST_KEY, JSON.stringify(updatedUsers));
    }
  }, [inicialPerfis]);

  // (useState de listaPerfis - sem alterações)
  const [listaPerfis, setListaPerfis] = useState(() => {
    const storedId = localStorage.getItem(LOGGED_IN_ID_KEY);
    if (storedId) {
      try {
        const key = getStorageKey(storedId);
        const storedPerfis = localStorage.getItem(key);
        return storedPerfis ? JSON.parse(storedPerfis) : []; 
      } catch (e) {
        return [];
      }
    }
    return [];
  });

  // (useEffect de carregar perfis e lista - sem alterações)
  useEffect(() => {
    const storedId = localStorage.getItem(LOGGED_IN_ID_KEY);
    if (storedId) {
        setUserId(storedId);
        setIsLoggedIn(true);
        const key = getStorageKey(storedId);
        const storedPerfis = localStorage.getItem(key);
        let loadedPerfis = [];
        if (storedPerfis) {
            loadedPerfis = JSON.parse(storedPerfis);
        } else if (storedId === PRE_CADASTRADO_USER_ID) {
            loadedPerfis = inicialPerfis;
            localStorage.setItem(key, JSON.stringify(inicialPerfis));
        }
        if (loadedPerfis.length > 0) {
            setListaPerfis(loadedPerfis);
        }
        const listaKey = getMinhaListaKey(storedId);
        const storedLista = localStorage.getItem(listaKey);
        if (storedLista) {
            setMinhaLista(JSON.parse(storedLista));
        } else {
            setMinhaLista([]);
        }
    } else {
        setIsLoggedIn(false);
        setUserId(null);
        setMinhaLista([]);
    }
  }, [inicialPerfis]);

  // (useEffect de sincronizar perfis - sem alterações)
  useEffect(() => {
    if (userId && listaPerfis.length > 0) {
      const key = getStorageKey(userId);
      localStorage.setItem(key, JSON.stringify(listaPerfis));
    }
  }, [listaPerfis, userId]);

  // (useEffect de sincronizar minhaLista - sem alterações)
  useEffect(() => {
    if (userId) {
      const listaKey = getMinhaListaKey(userId);
      localStorage.setItem(listaKey, JSON.stringify(minhaLista));
    }
  }, [minhaLista, userId]);

  // (funções updatePerfis, adicionarNovoPerfil, resetPerfisAndAddFirst - sem alterações)
  const updatePerfis = (newPerfis, targetUserId = null) => {
    const userIdToUse = targetUserId || userId;
    setListaPerfis(newPerfis);
    if (userIdToUse) {
      const key = getStorageKey(userIdToUse);
      localStorage.setItem(key, JSON.stringify(newPerfis));
    }
  };
  const adicionarNovoPerfil = (novoPerfil) => { 
    if (!userId) return;
    if (novoPerfil && novoPerfil.nome) {
      const perfilParaSalvar = { id: Date.now(), ...novoPerfil };
      updatePerfis([...listaPerfis, perfilParaSalvar]);
    }
  }; 
  const resetPerfisAndAddFirst = (novoPerfil) => {
    if (novoPerfil && novoPerfil.nome) {
      const perfilParaSalvar = { id: Date.now(), ...novoPerfil };
      updatePerfis([perfilParaSalvar]);
    }
  };

  // (funções handleLoginSuccess, handleCadastroSuccess, handleLogout - sem alterações)
  const handleLoginSuccess = () => {
    let newUserId = localStorage.getItem(LOGGED_IN_ID_KEY);
    setUserId(newUserId);
    setIsLoggedIn(true);
    const key = getStorageKey(newUserId);
    const storedPerfis = localStorage.getItem(key);
    let loadedPerfis;
    if (storedPerfis) {
      loadedPerfis = JSON.parse(storedPerfis);
    } else if (newUserId === PRE_CADASTRADO_USER_ID) {
      loadedPerfis = inicialPerfis;
      localStorage.setItem(key, JSON.stringify(inicialPerfis));
    } else {
      loadedPerfis = [];
    }
    const listaKey = getMinhaListaKey(newUserId);
    const storedLista = localStorage.getItem(listaKey);
    if (storedLista) {
        setMinhaLista(JSON.parse(storedLista));
    } else {
        setMinhaLista([]);
    }
    if (!loadedPerfis || loadedPerfis.length === 0) {
      setListaPerfis([]);
      navigate('/primeiro-perfil');
    } else {
      setListaPerfis(loadedPerfis);
      navigate('/perfis'); 
    }
    localStorage.removeItem('temp_logged_email');
  };
  const handleCadastroSuccess = () => {
    const newUserId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
    localStorage.setItem(LOGGED_IN_ID_KEY, newUserId);
    const newUserKey = getStorageKey(newUserId);
    localStorage.setItem(newUserKey, JSON.stringify([]));
    const newListaKey = getMinhaListaKey(newUserId);
    localStorage.setItem(newListaKey, JSON.stringify([]));
    setMinhaLista([]);
    setUserId(newUserId);
    setIsLoggedIn(true);
    setListaPerfis([]);
    navigate('/primeiro-perfil'); 
  };
  const handleLogout = () => {
    localStorage.removeItem(LOGGED_IN_ID_KEY); 
    setUserId(null);
    setIsLoggedIn(false);
    setListaPerfis([]); 
    setPerfilSelecionado(null);
    setMinhaLista([]);
    navigate('/login');
  };

  // (useEffect de proteção de rotas - sem alterações)
  useEffect(() => {
    const rotasProtegidas = ['/perfis', '/primeiro-perfil', '/home', '/filmes', '/series', '/minha-lista'];
    const rotaAtualProtegida = rotasProtegidas.some(rota => location.pathname.startsWith(rota));
    
    if (!isLoggedIn && (rotaAtualProtegida || location.pathname.startsWith('/media/'))) {
      navigate('/login');
    }
  }, [isLoggedIn, location, navigate]);

  // (funções handlePerfilClick, adicionarAFavoritos, removerDeFavoritos, isFavorito - sem alterações)
  const handlePerfilClick = (perfilObj) => { 
    setPerfilSelecionado(perfilObj);
  };  
  const adicionarAFavoritos = (mediaItem) => {
    setMinhaLista((prevLista) => {
      if (prevLista.find(item => item.Title === mediaItem.Title)) {
        return prevLista;
      }
      return [...prevLista, mediaItem];
    });
  };
  const removerDeFavoritos = (mediaItem) => {
    setMinhaLista((prevLista) => 
      prevLista.filter(item => item.Title !== mediaItem.Title)
    );
  };
  const isFavorito = (mediaItem) => {
    if (!mediaItem) return false;
    return minhaLista.some(item => item.Title === mediaItem.Title);
  };

  // (contextValue - sem alterações)
  const contextValue = {
    isLoggedIn,
    perfilSelecionado,
    perfis: listaPerfis,
    handleLoginSuccess,
    handlePerfilClick,
    handleCadastroSuccess,
    adicionarNovoPerfil,
    handleLogout,
    resetPerfisAndAddFirst,
    minhaLista,
    adicionarAFavoritos,
    removerDeFavoritos,
    isFavorito,
  };

  // MODIFICADO: Adicionadas as rotas de perfil à lista de exclusão do Header
  const rotasSemHeader = [
    '/login', 
    '/cadastro', 
    '/', 
    '/perfis', 
    '/primeiro-perfil', 
    '/criar-perfil'
  ];

  return (
    <div className="app-container">
      
      {/* MODIFICADO: A lógica de renderização agora usa a nova lista 'rotasSemHeader' */ }
      {isLoggedIn && !rotasSemHeader.includes(location.pathname) && (
        <>
          <div 
            className={`overlay ${menuAberto ? 'show' : ''}`}
            onClick={toggleMenu}
          ></div>

          <aside className={`sidebar ${menuAberto ? 'open' : ''}`}>
            <div className="sidebar-header">
              <h3>Menu</h3>
              <button className="close-button" onClick={toggleMenu}>
                <FaTimes />
              </button>
            </div>
            
            <ul className="sidebar-menu">
              <li id = "1" onClick={() => handleNavigate('/home')}>Início</li>
              <li id = "2" onClick={() => handleNavigate('/filmes')}>Filmes</li>
              <li id = "3" onClick={() => handleNavigate('/series')}>Séries</li>
              <li id = "4" onClick={() => handleNavigate('/minha-lista')}>Minha Lista</li>
              <li id = "5">Configurações</li>
              <li id = "6" onClick={handleLogout}>Sair</li>
            </ul>
          </aside>

          <header className="header">
            <button className="menu-hamburger" onClick={toggleMenu}>
              <FaBars />
            </button>
            
            <div className="header-icons">
              <button className="icon-button">
                <FaStar />
              </button>
              <img 
                src={perfilSelecionado?.imagem || 'https://placehold.co/150x150/0f172a/ffffff?text=User'} 
                alt="Perfil" 
                className="perfil-icon"
                onClick={() => handleNavigate('/perfis')} 
              />
            </div>
          </header>
        </>
      )}

      <Outlet context={contextValue} />
    </div>
  );
}

export default App;