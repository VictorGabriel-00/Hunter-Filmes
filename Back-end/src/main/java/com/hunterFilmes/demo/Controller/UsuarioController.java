package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Dto.UsuarioDto;
import com.hunterFilmes.demo.Dto.UsuarioResponseDto;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import com.hunterFilmes.demo.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> addUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        Usuario usuario = usuarioService.criarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioById(@PathVariable(value = "id") UUID id) {
        Optional<UsuarioResponseDto> usuarioOp = usuarioService.buscarUsuarioComPlano(id);
        if (usuarioOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOp.get());
    }

    @GetMapping("/simples/{id}")
    public ResponseEntity<Object> getUsuarioSimplesById(@PathVariable(value = "id") UUID id) {
        Optional<Usuario> usuarioOp = usuarioService.buscarUsuarioPorId(id);
        if (usuarioOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOp.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOp = usuarioService.atualizarUsuario(id, usuarioDto);
        if (usuarioOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOp.get());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuarioById(@PathVariable(value = "id") UUID id) {
        boolean deletado = usuarioService.deletarUsuario(id);
        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    }


}
