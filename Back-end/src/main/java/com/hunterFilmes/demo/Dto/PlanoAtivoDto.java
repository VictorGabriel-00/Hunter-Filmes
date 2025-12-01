package com.hunterFilmes.demo.Dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlanoAtivoDto(
        UUID idPagamento,
        UUID idPlano,
        String nomePlano,
        Float precoPlano,
        String descricaoPlano,
        boolean pagamentoAtivo,
        LocalDateTime dataPagamento
) {
}