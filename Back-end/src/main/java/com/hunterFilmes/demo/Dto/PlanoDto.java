package com.hunterFilmes.demo.Dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record PlanoDto(@Nullable String nome, @NotNull Float preco, @Nullable String descricao ) {
}
