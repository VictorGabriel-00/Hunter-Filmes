import React, { useState, useRef, useLayoutEffect, useEffect } from 'react';
import { useOutletContext, useLocation, useNavigate } from 'react-router-dom';
import { FaStar, FaBars, FaTimes, FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import './Tela-inicial.css';
import FilmesData from './Filmes.json';
import seriesData from './Series.json';

const corrigirCaminhoBanner = (caminho) => {
  const nomeBanner = caminho.split('/').pop();
  return `/src/components/TelaInicial/Filmes_Series/Banner_Filmes/${nomeBanner}`;
};

// ✅ Componente Carousel melhorado
const Carousel = ({ items, onItemClick }) => {
  const trackRef = useRef(null);
  const [index, setIndex] = useState(1);
  const cardWidthRef = useRef(0);
  const transitioningRef = useRef(false);
  const GAP = 16;

  if (!items || items.length === 0) return null;

  const extended = [items[items.length - 1], ...items, items[0]];

  const updatePosition = (idx, withTransition = true) => {
    const track = trackRef.current;
    if (!track) return;
    const step = cardWidthRef.current + GAP;
    if (!withTransition) track.style.transition = 'none';
    else track.style.transition = '';
    track.style.transform = `translateX(-${idx * step}px)`;
  };

  const measure = () => {
    const track = trackRef.current;
    if (!track || !track.children || track.children.length < 2) return;
    const firstReal = track.children[1];
    const rect = firstReal.getBoundingClientRect();
    cardWidthRef.current = Math.round(rect.width);
  };

  useLayoutEffect(() => {
    measure();
    updatePosition(index, true);
  }, [items]);

  useEffect(() => {
    if (!trackRef.current) return;
    
    updatePosition(index, true);

    const handleTransitionEnd = () => {
      const maxIndex = extended.length - 1;
      const minIndex = 0;

      if (index === maxIndex) {
        const track = trackRef.current;
        if (!track) return;
        track.style.transition = 'none';
        const step = cardWidthRef.current + GAP;
        track.style.transform = `translateX(-${1 * step}px)`;
        track.offsetHeight;
        track.style.transition = '';
        setIndex(1);
        setTimeout(() => {
          transitioningRef.current = false;
        }, 0);
      } else if (index === minIndex) {
        const track = trackRef.current;
        if (!track) return;
        track.style.transition = 'none';
        const step = cardWidthRef.current + GAP;
        const targetIndex = extended.length - 2;
        track.style.transform = `translateX(-${targetIndex * step}px)`;
        track.offsetHeight;
        track.style.transition = '';
        setIndex(targetIndex);
        setTimeout(() => {
          transitioningRef.current = false;
        }, 0);
      } else {
        transitioningRef.current = false;
      }
    };

    const track = trackRef.current;
    track.addEventListener('transitionend', handleTransitionEnd);
    return () => track.removeEventListener('transitionend', handleTransitionEnd);
  }, [index, extended.length]);

  useEffect(() => {
    const onResize = () => {
      measure();
      updatePosition(index, false);
    };
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
  }, [index]);

  const next = () => {
    if (transitioningRef.current) return;
    transitioningRef.current = true;
    setIndex(i => i + 1);
  };

  const prev = () => {
    if (transitioningRef.current) return;
    transitioningRef.current = true;
    setIndex(i => i - 1);
  };

  // ✅ Função para lidar com cliques nos cards
  const handleCardClick = (idx) => {
    let realIndex = idx - 1;
    if (realIndex < 0) realIndex = items.length - 1;
    if (realIndex >= items.length) realIndex = 0;
    
    if (onItemClick) {
      onItemClick(items[realIndex]);
    }
  };

  return (
    <div className="carousel-wrapper">
      <button className="carousel-btn left" onClick={prev} aria-label="Anterior">
        <FaChevronLeft />
      </button>
      <div className="cards-container carousel-view">
        <div className="carousel-track" ref={trackRef}>
          {extended.map((item, idx) => (
            <div 
              key={idx} 
              className="card"
              onClick={() => handleCardClick(idx)}  
            >
              <img src={item.Banner} alt={item.Title} />
              <div className="card-info">
                <h3>{item.Title}</h3>
                <span>{item.Ano}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
      <button className="carousel-btn right" onClick={next} aria-label="Próximo">
        <FaChevronRight />
      </button>
    </div>
  );
};

const TelaInicial = () => {
  const { perfilSelecionado, handleLogout } = useOutletContext();
  const [menuAberto, setMenuAberto] = useState(false);
  
  const location = useLocation();
  const navigate = useNavigate();
  const pathname = location.pathname;

  const modo = pathname.endsWith('/filmes') ? 'filmes' :
               pathname.endsWith('/series') ? 'series' :
               'home';

  const filmes = FilmesData.map(filme => ({
    ...filme,
    Banner: corrigirCaminhoBanner(filme.Banner)
  }));

  const series = seriesData.map(serie => ({
    ...serie,
    Banner: corrigirCaminhoBanner(serie.Banner)
  }));

  const filmeJack = FilmesData.find(filme => filme.Title === "O Estranho Mundo de Jack");
  const serieDestaqueDefault = series[0];

  // ✅ Estado para o filme em destaque
  const [destaque, setDestaque] = useState(
    modo === 'series' ? {
      titulo: serieDestaqueDefault.Title,
      banner: corrigirCaminhoBanner(serieDestaqueDefault.Banner),
      sinopse: serieDestaqueDefault.Sinopse,
      Ano: serieDestaqueDefault.Ano,
      Genero: serieDestaqueDefault.Genero
    } : {
      titulo: filmeJack.Title,
      banner: corrigirCaminhoBanner(filmeJack.Banner),
      sinopse: filmeJack.Sinopse,
      Ano: filmeJack.Ano,
      Genero: filmeJack.Genero
    }
  );

  const toggleMenu = () => {
    setMenuAberto(!menuAberto);
  };

  const handleNavigate = (path) => {
    navigate(path);
    setMenuAberto(false);
  };

  // ✅ Função para atualizar o filme em destaque
  const handleItemClick = (item) => {
    setDestaque({
      titulo: item.Title,
      banner: corrigirCaminhoBanner(item.Banner),
      sinopse: item.Sinopse,
      Ano: item.Ano,
      Genero: item.Genero
    });
    
  
  };

  const criarRecomendados = (filmesArr, seriesArr) => {
    const recomendadosList = [];
    const maxLength = Math.max(filmesArr.length, seriesArr.length);

    for (let i = 0; i < maxLength; i++) {
      if (filmesArr[i]) recomendadosList.push(filmesArr[i]);
      if (seriesArr[i]) recomendadosList.push(seriesArr[i]);
    }
    return recomendadosList;
  };
  
  const recomendados = criarRecomendados(filmes, series);

  if (filmes.length === 0 || series.length === 0) {
    return <div>Carregando...</div>; 
  }

  return (
    <div className="tela-inicial-container">
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
          <li onClick={() => handleNavigate('/home')}>Início</li>
          <li onClick={() => handleNavigate('/filmes')}>Filmes</li>
          <li onClick={() => handleNavigate('/series')}>Séries</li>
          <li>Minha Lista</li>
          <li>Configurações</li>
          <li onClick={handleLogout}>Sair</li>
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
          />
        </div>
      </header>

      <main className="content">
        <section className="destaque-section">
          <div className="poster-principal">
            <img src={destaque.banner} alt={destaque.titulo} />
          </div>
          <div className="info-filme">
            <h1>{destaque.titulo}</h1>
            <div className="filme-info-details">
              <span>{destaque.Ano}</span>
              <span>{destaque.Genero}</span>
            </div>
            <p>{destaque.sinopse}</p>
          </div>
        </section>

        {modo === 'home' && (
          <section className="secao">
            <h2>Recomendados</h2>
            <Carousel items={recomendados} onItemClick={handleItemClick} />
          </section>
        )}

        {(modo === 'home' || modo === 'filmes') && (
          <section className="secao">
            <h2>Filmes</h2>
            <Carousel items={filmes} onItemClick={handleItemClick} />
          </section>
        )}

        {(modo === 'home' || modo === 'series') && (
          <section className="secao">
            <h2>Séries</h2>
            <Carousel items={series} onItemClick={handleItemClick} />
          </section>
        )}
      </main>
    </div>
  );
};

export default TelaInicial;