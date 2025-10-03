package com.hunterFilmes.demo.Repositori;

import com.hunterFilmes.demo.Model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SerieRepositori  extends JpaRepository<Serie, UUID> {
}
