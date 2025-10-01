import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Primeiro-perfil.css';
import CriarPerfil from '../CriarPerfil/Criar-perfil.jsx';

const PrimeiroPerfil = () => {
    const [isCriarPerfil, setIsCriarPerfil] = useState(false);
    const navigate = useNavigate();

    const handleCriaPerfil = () => {
        setIsCriarPerfil(true);
    };

    const handleVoltarPerfis = () => {
        setIsCriarPerfil(false);
    };

    const handlePerfilCriado = () => {
        navigate('/perfis');
    };

    if (isCriarPerfil) {
        return <CriarPerfil 
            onVoltar={handleVoltarPerfis} 
            onPerfilCriado={handlePerfilCriado}
            isPrimeiroPerfil={true}
        />;
    }

    return (
        <div className="criar-perfil-container">
            <h2>Crie um Perfil</h2>
            <div >
                <button onClick={handleCriaPerfil} className="criar-perfil-button"> 
                    +
                </button>
                <p className="criar-perfil">Adicionar perfil</p>
            </div>
        </div>
    );
}

export default PrimeiroPerfil;