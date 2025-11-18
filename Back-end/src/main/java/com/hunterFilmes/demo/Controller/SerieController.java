package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Dto.SerieDto;
import com.hunterFilmes.demo.Dto.UsuarioDto;
import com.hunterFilmes.demo.Model.Serie;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.SerieRepositori;
import jakarta.validation.Valid;
import jdk.jfr.Percentage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/serie")
public class SerieController {

    @Autowired
    SerieRepositori serieRepositori;


    @PostMapping
    public ResponseEntity<Serie> addSerie(@RequestBody @Valid SerieDto serieDto){
        var serie = new Serie();
        BeanUtils.copyProperties(serieDto,serie);
        return  ResponseEntity.status(HttpStatus.CREATED).body(serieRepositori.save(serie));
    }


    @GetMapping
    public ResponseEntity<List<Serie>> findAllSerie(){
        return  ResponseEntity.status(HttpStatus.OK).body(serieRepositori.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> findSerie(@PathVariable(value = "id") UUID id){
        Optional<Serie> serieOP = serieRepositori.findById(id);
        if(serieOP.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(serieOP.get());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSerie(@PathVariable(value = "id")UUID id, @RequestBody @Valid SerieDto serieDto) {
        Optional<Serie> serieOp = serieRepositori.findById(id);
        if(serieOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        var serie = serieOp.get();
        BeanUtils.copyProperties(serieDto, serie);
        return ResponseEntity.status(HttpStatus.OK).body(serieRepositori.save(serie));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSerieById(@PathVariable(value = "id")UUID id) {
        Optional<Serie> serieOp = serieRepositori.findById(id);
        if(serieOp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("serie não encontrado");
        }
        serieRepositori.delete(serieOp.get());
        return ResponseEntity.status(HttpStatus.OK).body("serie deletado com sucesso");
    }





}
