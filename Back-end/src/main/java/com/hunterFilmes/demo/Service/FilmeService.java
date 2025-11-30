package com.hunterFilmes.demo.Service;


import com.hunterFilmes.demo.Dto.FilmeDto;
import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Repositori.FilmeRepositori;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilmeService {

    @Autowired
    FilmeRepositori filmeRepositori;

    @Transactional
    public Filme criarFilme(FilmeDto  filmeDto) {
        if(filmeDto.duracao() == 0 || filmeDto.duracao() < 0){
            System.out.println("Duração do filme invalida");
        }

        var filme = new  Filme();
        BeanUtils.copyProperties(filmeDto, filme);
        return filmeRepositori.save(filme);
    }


    public List<Filme> listarTodosFilmes(){
        return filmeRepositori.findAll();
    }

    public Optional<Filme> listarFilmesPorId(UUID id){
        return filmeRepositori.findById(id);
    }


    @Transactional
    public Optional<Filme> atualizarFilme(UUID id, FilmeDto filmeDto) {
        Optional<Filme> filmeOp = filmeRepositori.findById(id);
        if(filmeOp.isPresent()){
            return Optional.empty();
        }

        var filme = filmeOp.get();
        BeanUtils.copyProperties(filmeDto, filme);
        return Optional.of(filmeRepositori.save(filme));
    }

    @Transactional
    public boolean deletarFilme(UUID id) {
        Optional<Filme> filmeOp = filmeRepositori.findById(id);
        if(filmeOp.isEmpty()){
            return false;
        }

        filmeRepositori.delete(filmeOp.get());
        return true;
    }

    public boolean filmeExiste(UUID id) {
        return filmeRepositori.existsById(id);
    }
}
