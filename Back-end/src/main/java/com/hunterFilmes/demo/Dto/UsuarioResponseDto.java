package com.hunterFilmes.demo.Dto;

import java.util.UUID;

public record UsuarioResponseDto(
        UUID id,
        String nome,
        String email,
        String dataNascimento,
        PlanoAtivoDto planoAtivo
) {
}