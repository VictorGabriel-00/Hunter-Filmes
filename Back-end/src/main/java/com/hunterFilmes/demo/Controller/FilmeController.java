package com.hunterFilmes.demo.Controller;


import com.hunterFilmes.demo.Dto.FilmeDto;
import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/filme")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;


    @PostMapping
    public ResponseEntity<Filme> addFilme(@RequestBody @Valid FilmeDto filmeDto) {
        Filme filme = filmeService.criarFilme(filmeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(filme);
    }


    @GetMapping
    public ResponseEntity<List<Filme>> getAllFilmes() {
        List<Filme> filme = filmeService.listarTodosFilmes();
        return ResponseEntity.status(HttpStatus.OK).body(filme);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object>  getFilme(@PathVariable(value = "id") UUID id) {
        Optional<Filme> filmeOp = filmeService.listarFilmesPorId(id);
        if(filmeOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(filmeOp.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFilme(@PathVariable(value = "id") UUID id,
                                              @RequestBody @Valid FilmeDto filmeDto) {
        Optional<Filme> filmeOp = filmeService.atualizarFilme(id, filmeDto);
        if(filmeOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(filmeOp.get());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFilme(@PathVariable(value = "id") UUID id) {
        boolean deleta = filmeService.deletarFilme(id);
        if(!deleta){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Filme deletado com sucesso");

    }


}
