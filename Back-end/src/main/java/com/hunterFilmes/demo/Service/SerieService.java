package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Dto.SerieDto;
import com.hunterFilmes.demo.Model.Serie;
import com.hunterFilmes.demo.Repositori.SerieRepositori;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SerieService {
    @Autowired
    private SerieRepositori serieRepositori;

    @Transactional
    public Serie criarSerie(SerieDto serieDto) {
        if(serieDto.temporadas() == 0 || serieDto.temporadas() < 0 ){
            System.out.println("Temporada invalida");
        }
        var serie = new Serie();
        BeanUtils.copyProperties(serieDto, serie);
        return serieRepositori.save(serie);
    }

    public List<Serie> listarTodasSeries() {
        return serieRepositori.findAll();
    }


    public Optional<Serie> buscarSeriePorId(UUID id) {
        return serieRepositori.findById(id);
    }

    @Transactional
    public Optional<Serie> atualizarSerie(UUID id, SerieDto serieDto) {
        Optional<Serie> serieOp = serieRepositori.findById(id);
        if (serieOp.isEmpty()) {
            return Optional.empty();
        }
        var serie = serieOp.get();
        BeanUtils.copyProperties(serieDto, serie);
        return Optional.of(serieRepositori.save(serie));
    }

    @Transactional
    public boolean deletarSerie(UUID id) {
        Optional<Serie> serieOp = serieRepositori.findById(id);
        if (serieOp.isEmpty()) {
            return false;
        }
        serieRepositori.delete(serieOp.get());
        return true;
    }

    public boolean serieExiste(UUID id) {
        return serieRepositori.existsById(id);
    }

}
