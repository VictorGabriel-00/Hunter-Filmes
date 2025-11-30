package com.hunterFilmes.demo.Dto;

import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Model.Usuario;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PagamentoDto(@NotNull UUID usuario, @NotNull UUID plano ,@NotNull float valor) {
}
