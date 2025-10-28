import React, { useState } from 'react';
// Importar useLocation e useNavigate
import { useOutletContext, useLocation, useNavigate } from 'react-router-dom';
import { FaStar, FaBars, FaTimes, FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import './Tela-inicial.css';
import FilmesData from './Filmes.json';
import seriesData from './Series.json';

// Função para corrigir o caminho das imagens
const corrigirCaminhoBanner = (caminho) => {
  const nomeBanner = caminho.split('/').pop(); // Pega apenas o nome do arquivo
  return `/src/components/TelaInicial/Filmes_Series/Banner_Filmes/${nomeBanner}`;
};


const TelaInicial = () => {
  const { perfilSelecionado, handleLogout } = useOutletContext();
  const [menuAberto, setMenuAberto] = useState(false);

  // --- MUDANÇAS AQUI ---
  // Hooks de Roteamento
  const location = useLocation();
  const navigate = useNavigate();
  const pathname = location.pathname;

  // Determina o modo de exibição com base na rota
  const modo = pathname.endsWith('/filmes') ? 'filmes' :
               pathname.endsWith('/series') ? 'series' :
               'home';
  // --- FIM MUDANÇAS ---


  const toggleMenu = () => {
    setMenuAberto(!menuAberto);
  };

  // --- MUDANÇA AQUI ---
  // Função para navegar e fechar o menu
  const handleNavigate = (path) => {
    navigate(path);
    setMenuAberto(false);
  };
  // --- FIM MUDANÇA ---


  // Aplicar correção de caminho para todos os filmes e séries
  const filmes = FilmesData.map(filme => ({
    ...filme,
    Banner: corrigirCaminhoBanner(filme.Banner)
  }));

  const series = seriesData.map(serie => ({
    ...serie,
    Banner: corrigirCaminhoBanner(serie.Banner)
  }));

  // --- MUDANÇAS AQUI ---
  // Destaque dinâmico
  const filmeJack = FilmesData.find(filme => filme.Title === "O Estranho Mundo de Jack");
  const serieDestaqueDefault = series[0]; // Pega a primeira série da lista

  // Define o item de destaque com base no 'modo'
  const destaque = modo === 'series' ? {
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
  };
  // --- FIM MUDANÇAS ---
  
  /*const recomendados = filmes.slice(0, 5).map(filme => ({
    ...filme,
    Banner: corrigirCaminhoBanner(filme.Banner)
  }));*/

  const criarRecomendados = (filmesArr, seriesArr) => {
    const recomendadosList = [];
    // Encontra o comprimento da maior lista
    const maxLength = Math.max(filmesArr.length, seriesArr.length);

    for (let i = 0; i < maxLength; i++) {
      // Adiciona um filme se ele existir nesse índice
      if (filmesArr[i]) {
        recomendadosList.push(filmesArr[i]);
      }
      // Adiciona uma série se ela existir nesse índice
      if (seriesArr[i]) {
        recomendadosList.push(seriesArr[i]);
      }
    }
    return recomendadosList;
  };
  
  // ADICIONE a nova definição de 'recomendados'
  const recomendados = criarRecomendados(filmes, series);
  
  // Componente Carousel reutilizável (código do carrossel permanece o mesmo)
  const Carousel = ({ items }) => {
    // ... (toda a lógica do Carousel que você já tem) ...
    // ... (não vou colar tudo aqui para economizar espaço) ...
    const trackRef = React.useRef(null);
    const [index, setIndex] = React.useState(1); // começamos no primeiro item real (depois do clone)
    const cardWidthRef = React.useRef(0);
    const transitioningRef = React.useRef(false);

    const GAP = 16; // gap em px (coincide com 1rem no CSS)

    // Se não houver items, não renderiza
    if (!items || items.length === 0) return null;

    // Monta array com clones nas extremidades: [last, ...items, first]
    const extended = [items[items.length - 1], ...items, items[0]];

    // Atualiza posição do track com base no index
    const updatePosition = (idx, withTransition = true) => {
      const track = trackRef.current;
      if (!track) return;
      const step = cardWidthRef.current + GAP;
      if (!withTransition) track.style.transition = 'none';
      else track.style.transition = '';
      track.style.transform = `translateX(-${idx * step}px)`;
    };

    // Medir largura do card (usamos o primeiro card real, que está em children[1])
    const measure = () => {
      const track = trackRef.current;
      if (!track || !track.children || track.children.length < 2) return;
      const firstReal = track.children[1];
      const rect = firstReal.getBoundingClientRect();
      cardWidthRef.current = Math.round(rect.width);
    };

    // Ajusta posição quando index ou tamanho dos items mudam
    React.useLayoutEffect(() => {
      measure();
      // set position for current index without removing transition on initial mount
      updatePosition(index, true);
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [items]);

    // Reposiciona sempre que o index mudar
    React.useEffect(() => {
      if (!trackRef.current) return;
      
      // Aplica a nova posição com animação
      updatePosition(index, true);

      const handleTransitionEnd = () => {
        const maxIndex = extended.length - 1; // último índice (clone do primeiro)
        const minIndex = 0; // clone do último

        // Se estivermos no clone do primeiro (final), saltar para o primeiro real
        if (index === maxIndex) {
          updatingToInner(1);
        } 
        // Se estivermos no clone do último (início), saltar para o último real
        else if (index === minIndex) {
          updatingToInner(extended.length - 2);
        }
        // Se não estivermos em um clone, a transição terminou. Libere a trava.
        else {
          transitioningRef.current = false;
        }
      };

      const updatingToInner = (targetIndex) => {
        const track = trackRef.current;
        if (!track) return;
        track.style.transition = 'none';
        const step = cardWidthRef.current + GAP;
        track.style.transform = `translateX(-${targetIndex * step}px)`;
        // forçar reflow
        // eslint-disable-next-line no-unused-expressions
        track.offsetHeight;
        // reativa transição
        track.style.transition = '';
        setIndex(targetIndex);

        setTimeout(() => {
          transitioningRef.current = false;
        }, 0);
      };

      const track = trackRef.current;
      track.addEventListener('transitionend', handleTransitionEnd);
      return () => track.removeEventListener('transitionend', handleTransitionEnd);
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [index, extended.length]);

    React.useEffect(() => {
      const onResize = () => {
        measure();
        updatePosition(index, false);
      };
      window.addEventListener('resize', onResize);
      return () => window.removeEventListener('resize', onResize);
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [index]);

    const goTo = (nextIndex) => {
      if (!trackRef.current) return;
      setIndex(nextIndex);
    };

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

    return (
      <div className="carousel-wrapper">
        <button className="carousel-btn left" onClick={prev} aria-label="Anterior">
          <FaChevronLeft />
        </button>
        <div className="cards-container carousel-view">
          <div className="carousel-track" ref={trackRef}>
            {extended.map((item, idx) => (
              <div key={idx} className="card">
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
  // ... (fim da lógica do carrossel) ...

  if (filmes.length === 0 || series.length === 0) {
    return <div>Carregando...</div>; 
  }

  return (
    <div className="tela-inicial-container">
      {/* Overlay para fechar o menu ao clicar fora */}
      <div 
        className={`overlay ${menuAberto ? 'show' : ''}`}
        onClick={toggleMenu}
      ></div>

      {/* Sidebar */}
      <aside className={`sidebar ${menuAberto ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h3>Menu</h3>
          <button className="close-button" onClick={toggleMenu}>
            <FaTimes />
          </button>
        </div>
        
        {/* --- MUDANÇA AQUI --- */}
        {/* Adicionados OnClicks para navegação */}
        <ul className="sidebar-menu">
          <li onClick={() => handleNavigate('/home')}>Início</li>
          <li onClick={() => handleNavigate('/filmes')}>Filmes</li>
          <li onClick={() => handleNavigate('/series')}>Séries</li>
          <li>Minha Lista</li>
          <li>Configurações</li>
          <li onClick={handleLogout}>Sair</li>
        </ul>
        {/* --- FIM MUDANÇA --- */}
      </aside>

      {/* Header */}
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

      {/* Conteúdo Principal */}
      <main className="content">
        {/* Seção de Destaque */}
        {/* --- MUDANÇA AQUI --- */}
        {/* Atualizado para usar o objeto 'destaque' dinâmico */}
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
        {/* --- FIM MUDANÇA --- */}


        {/* --- MUDANÇAS AQUI --- */}
        {/* Renderização condicional dos carrosséis */}

        {/* Recomendados (Só na home) */}
        {modo === 'home' && (
          <section className="secao">
            <h2>Recomendados</h2>
            <Carousel items={recomendados} />
          </section>
        )}

        {/* Filmes (Na home OU na página de filmes) */}
        {(modo === 'home' || modo === 'filmes') && (
          <section className="secao">
            <h2>Filmes</h2>
            <Carousel items={filmes} />
          </section>
        )}

        {/* Séries (Na home OU na página de séries) */}
        {(modo === 'home' || modo === 'series') && (
          <section className="secao">
            <h2>Séries</h2>
            <Carousel items={series} />
          </section>
        )}
        {/* --- FIM MUDANÇAS --- */}
      </main>
    </div>
  );
};

export default TelaInicial;