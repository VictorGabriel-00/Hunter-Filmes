package com.hunterFilmes.demo.Repositori;

import com.hunterFilmes.demo.Model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilmeRepositori extends JpaRepository<Filme, UUID> {
}
