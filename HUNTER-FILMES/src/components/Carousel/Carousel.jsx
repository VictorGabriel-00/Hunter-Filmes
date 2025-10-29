import React, { useState, useRef, useLayoutEffect, useEffect } from 'react';
import { FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import './Carousel.css';

const Carousel = ({ items, onItemClick }) => {
  const [index, setIndex] = useState(0); // Começa no índice 0
  const [atStart, setAtStart] = useState(true);
  const [atEnd, setAtEnd] = useState(false);
  
  const trackRef = useRef(null);
  const viewRef = useRef(null); // Ref para a área visível
  const cardWidthRef = useRef(0);
  const GAP = 16; // 1rem

  if (!items || items.length === 0) return null;

  // Função para mover o carrossel
  const updatePosition = (idx, withTransition = true) => {
    const track = trackRef.current;
    if (!track) return;
    const step = cardWidthRef.current + GAP;
    if (!withTransition) track.style.transition = 'none';
    else track.style.transition = '';
    track.style.transform = `translateX(-${idx * step}px)`;
  };

  // Função para verificar se estamos no início ou no fim
  const checkLimits = (currentIndex) => {
    if (!trackRef.current || !viewRef.current || !cardWidthRef.current) return;
    
    const cardWidth = cardWidthRef.current + GAP;
    // Largura total de todos os itens
    const trackWidth = cardWidth * items.length - GAP; 
    // Largura da área visível
    const viewWidth = viewRef.current.clientWidth;
    
    // O scroll máximo possível
    const maxScroll = Math.max(0, trackWidth - viewWidth);
    // O scroll atual
    const currentScroll = currentIndex * cardWidth;
    
    setAtStart(currentIndex === 0);
    // Estamos no fim se a largura total for menor que a tela
    // OU se o scroll atual for maior ou igual ao scroll máximo
    setAtEnd(trackWidth <= viewWidth || currentScroll >= maxScroll);
  };

  // Função para medir a largura do card
  const measure = () => {
    const track = trackRef.current;
    // Modificado para checar o primeiro item (índice 0)
    if (!track || !track.children || track.children.length === 0) return;
    const firstReal = track.children[0];
    const rect = firstReal.getBoundingClientRect();
    cardWidthRef.current = Math.round(rect.width);
  };

  // Na primeira renderização (e se os itens mudarem)
  useLayoutEffect(() => {
    measure();
    updatePosition(0, false); // Garante que começa no 0
    checkLimits(0); // Verifica os limites iniciais
  }, [items]); // Depende dos 'items'

  // Quando o índice (scroll) mudar
  useEffect(() => {
    if (!trackRef.current) return;
    updatePosition(index, true); // Move com transição
    checkLimits(index); // Verifica os limites
  }, [index]);

  // Quando a janela for redimensionada
  useEffect(() => {
    const onResize = () => {
      measure();
      updatePosition(index, false); // Reposiciona sem transição
      checkLimits(index); // Re-verifica os limites
    };
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
  }, [index, items]); // Depende do índice e dos itens

  // Função 'Próximo'
  const next = () => {
    // Só avança se não estiver no fim
    if (atEnd) return;
    setIndex(i => i + 1);
  };

  // Função 'Anterior'
  const prev = () => {
    // Só volta se não estiver no início
    if (atStart) return;
    setIndex(i => i - 1);
  };

  // Função de clique no card (agora é mais simples)
  const handleCardClick = (idx) => {
    if (onItemClick) {
      onItemClick(items[idx]);
    }
  };

  return (
    <div className="carousel-wrapper">
      <button 
        className="carousel-btn left" 
        onClick={prev} 
        aria-label="Anterior"
        // Esconde o botão se estiver no início
        style={{ visibility: atStart ? 'hidden' : 'visible' }} 
      >
        <FaChevronLeft />
      </button>
      
      {/* Adiciona a 'ref' na área de visualização */}
      <div className="cards-container carousel-view" ref={viewRef}>
        <div className="carousel-track" ref={trackRef}>
          {/* Mapeia 'items' diretamente, sem 'extended' */}
          {items.map((item, idx) => (
            <div 
              key={item.Title || idx} // Chave simplificada
              className="card"
              onClick={() => handleCardClick(idx)} // Clique simplificado
            >
              <img id = "Banner"src={item.Banner} alt={item.Title} />
              <div className="card-info">
                <h3>{item.Title}</h3>
                <span>{item.Ano}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
      
      <button 
        className="carousel-btn right" 
        onClick={next} 
        aria-label="Próximo"
        // Esconde o botão se estiver no fim
        style={{ visibility: atEnd ? 'hidden' : 'visible' }}
      >
        <FaChevronRight />
      </button>
    </div>
  );
};

export default Carousel;