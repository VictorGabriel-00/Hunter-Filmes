package com.hunterFilmes.demo.Controller;

import com.hunterFilmes.demo.Dto.PagamentoDto;
import com.hunterFilmes.demo.Model.Pagamento;
import com.hunterFilmes.demo.Service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Object> realizarPagamento(@RequestBody @Valid PagamentoDto pagamentoDto) {
        try {
            Pagamento pagamento = pagamentoService.processarPagamento(
                    pagamentoDto.usuario(),
                    pagamentoDto.plano()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> verificarStatusPagamento(@PathVariable UUID id) {
        boolean ativo = pagamentoService.verificarPagamentoAtivo(id);
        return ResponseEntity.ok(ativo);
    }
}