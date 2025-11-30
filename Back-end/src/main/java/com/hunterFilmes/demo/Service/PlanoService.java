package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Repositori.PlanoRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepositori planoRepositori;

    @Bean
    public CommandLineRunner setPlano(PlanoRepositori planoRepositori){
        return args -> {
            if(planoRepositori.findAll().isEmpty()){
                Plano basico = new Plano();
                basico.setNome("Plano Básico");
                basico.setPreco(19.90f);
                basico.setDescricao("Sem anúncios");
                basico.setRemoverAnuncio(true);
                basico.setPermitiBaixar(false);

                planoRepositori.save(basico);


                Plano premium = new Plano();
                premium.setNome("Plano Premium");
                premium.setPreco(29.90f);
                premium.setDescricao("Sem anúncios e Downloads ilimitados");
                basico.setRemoverAnuncio(true);
                basico.setPermitiBaixar(true);

                planoRepositori.save(premium);

                System.out.println("Planos Cadastrados com sucesso");

            }
        };
    }


    public List<Plano> listarPlanos(){
        return planoRepositori.findAll();
    }


}
