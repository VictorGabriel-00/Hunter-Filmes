package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepositori usuarioRepositori;


    //Criar Usuario
    public Usuario criarUsuario(Usuario usuario){
        return usuarioRepositori.save(usuario);
    }

    //Listar todos os usuarios
    public List<Usuario> listarUsuario(){
        return usuarioRepositori.findAll();
    }

    //Listar por Id
    public Optional<Usuario> buscarUsuarioById(UUID id){
        return usuarioRepositori.findById(id);
    }

    // atualizar Usuario
    public Usuario atualizarUsuario(UUID id,  Usuario usuarioAtualizado){
        Usuario usuario = usuarioRepositori.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrato"));

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setSenha(usuarioAtualizado.getSenha());
        usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());

        return usuarioRepositori.save(usuario);

    }


    // Deleear usuario
    public void deletarUsuario(UUID id){
        usuarioRepositori.deleteById(id);
    }


}
