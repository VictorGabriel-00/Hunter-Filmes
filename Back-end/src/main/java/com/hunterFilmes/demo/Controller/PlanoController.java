package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plano")
public class PlanoController {

    @Autowired
    private PlanoService planoService;


    @GetMapping
    public ResponseEntity<List<Plano>> listarPlanos(){
        List<Plano> planos = planoService.listarPlanos();
        return ResponseEntity.status(HttpStatus.OK).body(planos);
    }


}
