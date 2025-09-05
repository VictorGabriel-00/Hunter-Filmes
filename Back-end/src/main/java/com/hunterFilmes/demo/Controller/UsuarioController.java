package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Dto.UsuarioDto;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
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
@RequestMapping("/api/usuario")
@CrossOrigin
public class UsuarioController {

    @Autowired
    UsuarioRepositori usuarioRepositori;

    @PostMapping
    public ResponseEntity<Usuario> addUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        var usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepositori.save(usuario));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepositori.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioById(@PathVariable(value = "id")UUID id) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);
        if(usuarioOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioOp.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable(value = "id")UUID id, @RequestBody @Valid UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);
        if(usuarioOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        var usuario = usuarioOp.get();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepositori.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuarioById(@PathVariable(value = "id")UUID id) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);
        if(usuarioOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        usuarioRepositori.delete(usuarioOp.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    }


}
