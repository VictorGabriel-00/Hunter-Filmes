package com.hunterFilmes.demo.Dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record SerieDto(@Nullable String titulo,@Nullable String descricao,@Nullable String anoLancamento,@NotNull int temporadas) {
}
