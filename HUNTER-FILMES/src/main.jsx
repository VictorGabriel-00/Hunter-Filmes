import React from 'react';
import ReactDOM from 'react-dom/client';
import AppRoutes from './pages/Routes.jsx'; // 👈 Importa o novo roteador
import './index.css';
import TelaInicial from './components/TelaInicial/Tela-inicial.jsx';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    {/* O AppRoutes agora é o ponto de partida do app */}
    {/*<TelaInicial/>*/}
   { <AppRoutes /> }
  </React.StrictMode>,
);