package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Dto.SerieDto;
import com.hunterFilmes.demo.Model.Serie;
import com.hunterFilmes.demo.Service.SerieService;
import jakarta.validation.Valid;
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
    private SerieService serieService;

    @PostMapping
    public ResponseEntity<Serie> addSerie(@RequestBody @Valid SerieDto serieDto) {
        Serie serie = serieService.criarSerie(serieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(serie);
    }

    @GetMapping
    public ResponseEntity<List<Serie>> findAllSerie() {
        List<Serie> series = serieService.listarTodasSeries();
        return ResponseEntity.status(HttpStatus.OK).body(series);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findSerie(@PathVariable(value = "id") UUID id) {
        Optional<Serie> serieOP = serieService.buscarSeriePorId(id);
        if (serieOP.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(serieOP.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSerie(@PathVariable(value = "id") UUID id,
                                              @RequestBody @Valid SerieDto serieDto) {
        Optional<Serie> serieOp = serieService.atualizarSerie(id, serieDto);
        if (serieOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(serieOp.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSerieById(@PathVariable(value = "id") UUID id) {
        boolean deletado = serieService.deletarSerie(id);
        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Serie deletado com sucesso");
    }
}