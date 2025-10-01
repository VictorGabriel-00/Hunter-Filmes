import React from 'react';
import ReactDOM from 'react-dom/client';
import AppRoutes from './pages/Routes.jsx'; // 👈 Importa o novo roteador
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    {/* O AppRoutes agora é o ponto de partida do app */}
    <AppRoutes /> 
  </React.StrictMode>,
);