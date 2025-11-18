package com.hunterFilmes.demo.Controller;


import com.hunterFilmes.demo.Dto.FilmeDto;
import com.hunterFilmes.demo.Dto.SerieDto;
import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Model.Serie;
import com.hunterFilmes.demo.Repositori.FilmeRepositori;
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
@RequestMapping("/filme")
public class FilmeController {

    @Autowired
    FilmeRepositori filmeRepositori;


    @PostMapping
    public ResponseEntity<Filme> addFilme(@RequestBody @Valid FilmeDto filmeDto){
        var filme = new Filme();
        BeanUtils.copyProperties(filmeDto,filme);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeRepositori.save(filme));
    }


    @GetMapping
    public ResponseEntity<List<Filme>> findAllFilme(){
        return ResponseEntity.status(HttpStatus.OK).body(filmeRepositori.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findFilme(@PathVariable(value = "id") UUID id){
        Optional<Filme> filmeOP = filmeRepositori.findById(id);
        if(filmeOP.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(filmeOP.get());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Filme> updateFilme(@PathVariable(value = "id")UUID id, @RequestBody @Valid FilmeDto filmeDto) {
        Optional<Filme> filmeOp = filmeRepositori.findById(id);
        if(filmeOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        var filme = filmeOp.get();
        BeanUtils.copyProperties(filmeDto, filme);
        return ResponseEntity.status(HttpStatus.OK).body(filmeRepositori.save(filme));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFilmeById(@PathVariable(value = "id")UUID id) {
        Optional<Filme> filmeOp = filmeRepositori.findById(id);
        if(filmeOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flme não encontrado");
        }
        filmeRepositori.delete(filmeOp.get());
        return ResponseEntity.status(HttpStatus.OK).body("Filme deletado com sucesso");
    }



}
