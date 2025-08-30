package com.hunterFilmes.demo.Repositori;

import com.hunterFilmes.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsuarioRepositori extends JpaRepository<Usuario, UUID> {
}
