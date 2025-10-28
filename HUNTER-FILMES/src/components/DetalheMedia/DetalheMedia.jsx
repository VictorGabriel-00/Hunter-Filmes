import React from 'react';
import { useParams, useOutletContext, useNavigate } from 'react-router-dom';
import { FaPlay, FaPlus, FaCheck } from 'react-icons/fa';
import FilmesData from '../TelaInicial/Filmes.json';
import seriesData from '../TelaInicial/Series.json';
import './DetalheMedia.css';

// (Funções auxiliares - sem alterações)
const corrigirCaminhoBanner = (caminho) => {
  const nomeBanner = caminho.split('/').pop();
  return `/src/components/TelaInicial/Filmes_Series/Banner_Filmes/${nomeBanner}`;
};
const todosOsFilmes = FilmesData.map(f => ({ ...f, Banner: corrigirCaminhoBanner(f.Banner) }));
const todasAsSeries = seriesData.map(s => ({ ...s, Banner: corrigirCaminhoBanner(s.Banner) }));
const todoOMedia = [...todosOsFilmes, ...todasAsSeries];


const DetalheMedia = () => {
  const { title } = useParams(); 
  const { minhaLista, adicionarAFavoritos, removerDeFavoritos, isFavorito } = useOutletContext();
  const navigate = useNavigate();

  const mediaItem = todoOMedia.find(m => m.Title === title);

  if (!mediaItem) {
    return (
        <div className="detalhe-media-container">
            {/* O placeholder foi removido */}
            <h2>Mídia não encontrada!</h2>
        </div>
    );
  }

  const primeiroGenero = mediaItem.Genero.split(',')[0].trim();
  const semelhantes = todoOMedia.filter(m => 
    m.Genero.includes(primeiroGenero) && 
    m.Title !== mediaItem.Title 
  );

  const jaEhFavorito = isFavorito(mediaItem);

  const handleToggleFavorito = () => {
    if (jaEhFavorito) {
      removerDeFavoritos(mediaItem);
    } else {
      adicionarAFavoritos(mediaItem);
    }
  };

  const handleCardClick = (item) => {
    navigate(`/media/${item.Title}`);
  };

  return (
    <div className="detalhe-media-container">
      {/* Imagem de fundo com overlay */}
      <div className="detalhe-banner-bg" style={{ backgroundImage: `url(${mediaItem.Banner})` }}>
        <div className="detalhe-banner-overlay"></div>
      </div>
      
      {/* O placeholder foi REMOVIDO daqui */}
      
      <div className="detalhe-conteudo">
        <div className="detalhe-poster">
          <img src={mediaItem.Banner} alt={mediaItem.Title} />
        </div>
        <div className="detalhe-info">
          <h1>{mediaItem.Title}</h1>
          <div className="detalhe-meta">
            <span>{mediaItem.Ano}</span>
            <span>{mediaItem.Genero}</span>
          </div>
          <p className="detalhe-sinopse">{mediaItem.Sinopse}</p>
          <div className="detalhe-botoes">
            <button className="btn-assistir">
              <FaPlay /> Assistir
            </button>
            <button className="btn-minha-lista" onClick={handleToggleFavorito}>
              {jaEhFavorito ? <FaCheck /> : <FaPlus />}
              Minha Lista
            </button>
          </div>
        </div>
      </div>

      {semelhantes.length > 0 && (
        <section className="secao-semelhantes">
          <h2>Semelhantes</h2>
          <div className="cards-container-simples">
            {semelhantes.map(item => (
              <div 
                key={item.Title} 
                className="card-simples" 
                onClick={() => handleCardClick(item)}
              >
                <img src={item.Banner} alt={item.Title} />
              </div>
            ))}
          </div>
        </section>
      )}
    </div>
  );
};

export default DetalheMedia;