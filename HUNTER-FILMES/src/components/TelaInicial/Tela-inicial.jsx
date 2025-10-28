import React, { useState, useEffect } from 'react';
import { useOutletContext, useLocation, useNavigate } from 'react-router-dom';
import './Tela-inicial.css';
import FilmesData from './Filmes.json';
import seriesData from './Series.json';
import Carousel from '../Carousel/Carousel.jsx';

const corrigirCaminhoBanner = (caminho) => {
  const nomeBanner = caminho.split('/').pop();
  return `/src/components/TelaInicial/Filmes_Series/Banner_Filmes/${nomeBanner}`;
};

const TelaInicial = () => {
  // --- ADICIONE 'minhaLista' AO CONTEXTO ---
  const { minhaLista } = useOutletContext(); 
  
  const location = useLocation();
  const navigate = useNavigate();
  const pathname = location.pathname;

  // --- ADICIONE O 'modo' LISTA ---
  const modo = pathname.endsWith('/filmes') ? 'filmes' :
               pathname.endsWith('/series') ? 'series' :
               pathname.endsWith('/minha-lista') ? 'lista' : // <-- ADICIONE ISTO
               'home';

  const filmes = FilmesData.map(filme => ({
    ...filme,
    Banner: corrigirCaminhoBanner(filme.Banner)
  }));

  const series = seriesData.map(serie => ({
    ...serie,
    Banner: corrigirCaminhoBanner(serie.Banner)
  }));

  const filmeJack = filmes.find(filme => filme.Title === "O Estranho Mundo de Jack");
  const serieDestaqueDefault = series[0];
  
  // --- ADICIONE LÓGICA PARA DESTAQUE DA LISTA ---
  // Se a lista não estiver vazia, usa o primeiro item, senão, volta para o Jack
  const itemDestaqueLista = (minhaLista && minhaLista.length > 0) ? minhaLista[0] : filmeJack;

  // --- ATUALIZE O ESTADO INICIAL DO DESTAQUE ---
  const [destaque, setDestaque] = useState(
    modo === 'series' ? serieDestaqueDefault :
    modo === 'lista' ? itemDestaqueLista : // <-- ADICIONE ISTO
    filmeJack // 'home' e 'filmes' usam o Jack
  );

  // --- ADICIONE UM useEffect PARA ATUALIZAR O DESTAQUE QUANDO O MODO OU A LISTA MUDAR ---
  useEffect(() => {
    if (modo === 'series') {
      setDestaque(serieDestaqueDefault);
    } else if (modo === 'lista') {
      // Atualiza o destaque se a lista mudar (ex: adicionar o primeiro item)
      const novoDestaqueLista = (minhaLista && minhaLista.length > 0) ? minhaLista[0] : filmeJack;
      setDestaque(novoDestaqueLista);
    } else {
      setDestaque(filmeJack);
    }
    // Adiciona dependências, incluindo minhaLista
  }, [modo, minhaLista, filmeJack, serieDestaqueDefault]);
  
  
  const handleItemClick = (item) => {
    if (item && item.Title) {
      navigate(`/media/${item.Title}`);
    }
  };

  const criarRecomendados = (filmesArr, seriesArr) => {
    // ... (função sem alterações) ...
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
      <div className="header-placeholder"></div>

      <main className="content">
        <section 
          className="destaque-section" 
          onClick={() => handleItemClick(destaque)} 
          style={{cursor: 'pointer'}}
        >
          <div className="poster-principal">
            <img src={destaque.Banner} alt={destaque.Title} />
          </div>
          <div className="info-filme">
            <h1>{destaque.Title}</h1>
            <div className="filme-info-details">
              <span>{destaque.Ano}</span>
              <span>{destaque.Genero}</span>
            </div>
            <p>{destaque.Sinopse}</p>
          </div>
        </section>

        {/* --- LÓGICA DE RENDERIZAÇÃO ATUALIZADA --- */}
        {/* Mostra 'Recomendados' APENAS no 'home' */}
        {modo === 'home' && (
          <section className="secao">
            <h2>Recomendados</h2>
            <Carousel items={recomendados} onItemClick={handleItemClick} />
          </section>
        )}

        {/* Mostra 'Filmes' no 'home' OU 'filmes' */}
        {(modo === 'home' || modo === 'filmes') && (
          <section className="secao">
            <h2>Filmes</h2>
            <Carousel items={filmes} onItemClick={handleItemClick} />
          </section>
        )}

        {/* Mostra 'Séries' no 'home' OU 'series' */}
        {(modo === 'home' || modo === 'series') && (
          <section className="secao">
            <h2>Séries</h2>
            <Carousel items={series} onItemClick={handleItemClick} />
          </section>
        )}

        {/* Mostra 'Minha Lista' APENAS no 'lista' */}
        {(modo === 'lista') && (
          <section className="secao">
            <h2>Minha Lista</h2>
            {minhaLista.length > 0 ? (
              <Carousel items={minhaLista} onItemClick={handleItemClick} />
            ) : (
              // Adiciona uma mensagem de lista vazia
              <p className="lista-vazia">Sua lista está vazia. Adicione filmes e séries!</p>
            )}
          </section>
        )}
        {/* --- FIM DA LÓGICA DE RENDERIZAÇÃO --- */}
        
      </main>
    </div>
  );
};

export default TelaInicial;