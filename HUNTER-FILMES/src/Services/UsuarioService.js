import api from './api';

export const usuarioService = {
  // Login
  login: async (email, senha) => {
    try {
      const response = await api.post('/usuario/login', { email, senha });
      return { success: true, data: response.data };
    } catch (error) {
      return {
        success: false,
        message: error.response?.data?.mensagem || 'Erro ao fazer login'
      };
    }
  },

  // Cadastro
  cadastrar: async (usuario) => {
    try {
      const response = await api.post('/usuario', {
        nome: usuario.nome,
        email: usuario.email,
        senha: usuario.senha,
        dataNascimento: usuario.nascimento,
      });
      return { success: true, data: response.data };
    } catch (error) {
      return {
        success: false,
        message: error.response?.data?.mensagem || 'Erro ao cadastrar usuário'
      };
    }
  },

  // Buscar todos os usuários
  listar: async () => {
    try {
      const response = await api.get('/usuario');
      return { success: true, data: response.data };
    } catch (error) {
      return { success: false, message: 'Erro ao listar usuários' };
    }
  },

  // Buscar usuário por ID
  buscarPorId: async (id) => {
    try {
      const response = await api.get(`/usuario/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      return { success: false, message: 'Usuário não encontrado' };
    }
  },

  // Atualizar usuário
  atualizar: async (id, usuario) => {
    try {
      const response = await api.put(`/usuario/${id}`, usuario);
      return { success: true, data: response.data };
    } catch (error) {
      return { success: false, message: 'Erro ao atualizar usuário' };
    }
  },

  // Deletar usuário
  deletar: async (id) => {
    try {
      await api.delete(`/usuario/${id}`);
      return { success: true };
    } catch (error) {
      return { success: false, message: 'Erro ao deletar usuário' };
    }
  },
};


/*import axios from "axios";

export const axiosInstance = axios.create({
    baseURL: "http://localhost:8080"
})


export class UsuarioService {
    
}*/

