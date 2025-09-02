package com.hunterFilmes.demo.Dto;

import jakarta.annotation.Nullable;

public record UsuarioDto(@Nullable String nome, @Nullable String email , @Nullable String senha, @Nullable String dataNascimento) {
}
