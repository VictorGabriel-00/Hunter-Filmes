package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Model.Usuario;
import jakarta.validation.ConstraintValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
class UsuarioTeste {

	@Test
	void UsuarioTesteTipoValido() {
		Usuario user = new Usuario("Victor", "Teste@Teste.com", "88089120vitu", "12/05/2005");



	}

	@Test
	void UsuarioTesteTipoInvalido(){

	}


}
