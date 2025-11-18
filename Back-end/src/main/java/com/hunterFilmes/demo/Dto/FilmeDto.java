package com.hunterFilmes.demo.Dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record FilmeDto(@Nullable String titulo, @Nullable String descricao,@Nullable String anoLancamento,@NotNull Float duracao){
}
