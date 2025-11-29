package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import com.hunterFilmes.demo.Service.UsuarioService;
import jakarta.validation.ConstraintValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioTeste {


    @Mock
    private UsuarioRepositori usuarioRepositori;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");
        usuario.setDataNascimento("12/05/2005");
    }





}
