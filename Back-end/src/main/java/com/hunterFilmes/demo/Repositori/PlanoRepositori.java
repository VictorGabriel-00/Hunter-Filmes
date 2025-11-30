package com.hunterFilmes.demo.Repositori;

import com.hunterFilmes.demo.Model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanoRepositori extends JpaRepository<Plano, UUID> {
}
