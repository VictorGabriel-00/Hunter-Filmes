package com.hunterFilmes.demo.Dto;

import org.antlr.v4.runtime.misc.NotNull;

public record UsuarioDto(@NotNull String name, @NotNull String email , @NotNull String password, @NotNull String dataNascimento) {
}
