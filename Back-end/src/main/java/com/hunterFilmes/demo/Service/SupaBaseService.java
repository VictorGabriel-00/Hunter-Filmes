//package com.hunterFilmes.demo.Service;
//
//import com.hunterFilmes.demo.Dto.UsuarioDto;
//import com.hunterFilmes.demo.Model.Usuario;
//import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class SupaBaseService {
//    @Autowired
//    private UsuarioRepositori usuarioRepositori;
//
//    @Transactional
//    public Usuario criarUsuario(UsuarioDto usuarioDto) {
//
//        var usuario = new Usuario();
//        BeanUtils.copyProperties(usuarioDto, usuario);
//        return usuarioRepositori.save(usuario);
//    }
//
//    public List<Usuario> buscarTodosUsuarios() {
//        return usuarioRepositori.findAll();
//    }
//
//    public Usuario buscarUsuarioPorId(UUID id) {
//        return usuarioRepositori.findById(id)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
//    }
//
////    public Usuario buscarUsuarioPorEmail(String email) {
////        return usuarioRepositori.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
////    }
//
//    @Transactional
//    public Usuario atualizarUsuario(UUID id, UsuarioDto usuarioDto) {
//        Usuario usuario = buscarUsuarioPorId(id);
//
//        // Se o email foi alterado, verificar se é único
////        if (!usuario.getEmail().equals(usuarioDto.getEmail())) {
////            validarEmailUnico(usuarioDto.getEmail());
////        }
//
//        BeanUtils.copyProperties(usuarioDto, usuario);
//        return usuarioRepositori.save(usuario);
//    }
//
//    @Transactional
//    public void deletarUsuario(UUID id) {
//        Usuario usuario = buscarUsuarioPorId(id);
//        usuarioRepositori.delete(usuario);
//    }
//
//    public long contarUsuarios() {
//        return usuarioRepositori.count();
//    }
//
//    public boolean existeUsuario(UUID id) {
//        return usuarioRepositori.existsById(id);
//    }
//
////    private void validarEmailUnico(String email) {
////        if (usuarioRepositori.existsByEmail(email)) {
////            throw new RuntimeException("Email já está em uso: " + email);
////        }
////    }
//}
//
