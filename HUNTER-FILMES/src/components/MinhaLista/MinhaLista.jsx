import React from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import './MinhaLista.css';
import Carousel from '../Carousel/Carousel.jsx'; // Importa o novo componente

const MinhaLista = () => {
  const { minhaLista } = useOutletContext();
  const navigate = useNavigate();

  const handleCardClick = (item) => {
    navigate(`/media/${item.Title}`);
  };

  return (
    <div className="minha-lista-container">
      {/* O placeholder é pego do App.css global */}
      <div className="header-placeholder"></div> 

      <main className="content"> {/* Reutiliza a classe 'content' de Tela-inicial.css */ }
        <section className="secao"> {/* Reutiliza a classe 'secao' de Tela-inicial.css */ }
          <h2>Minha Lista</h2>
          
          {minhaLista.length === 0 ? (
            <p className="lista-vazia">Sua lista está vazia. Adicione filmes e séries!</p>
          ) : (
            <Carousel items={minhaLista} onItemClick={handleCardClick} />
          )}
        </section>
      </main>
    </div>
  );
};

export default MinhaLista;