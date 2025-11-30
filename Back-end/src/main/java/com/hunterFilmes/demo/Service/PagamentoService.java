package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Model.Pagamento;
import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.PagamentoRepositori; // Certifique-se de criar este repositório
import com.hunterFilmes.demo.Repositori.PlanoRepositori;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepositori pagamentoRepositori;

    @Autowired
    private UsuarioRepositori usuarioRepositori;

    @Autowired
    private PlanoRepositori planoRepositori;

//    public Pagamento processarPagamento(Usuario usuario, Plano plano) {
//        Pagamento novoPagamento = new Pagamento();
//        novoPagamento.setUsuario(usuario);
//        novoPagamento.setPlano(plano);
//        novoPagamento.setDataPagamento(LocalDateTime.now());
//
//        novoPagamento.setPagamentoAtivo(true);
//
//        return pagamentoRepositori.save(novoPagamento);
//    }

    public boolean verificarPagamentoAtivo(UUID idPagamento) {
        Optional<Pagamento> pagamentoOp = pagamentoRepositori.findById(idPagamento);

        if (pagamentoOp.isPresent()) {
            Pagamento pagamento = pagamentoOp.get();
            return pagamento.isPagamentoAtivo();
        }

        return false;
    }

    public Pagamento processarPagamento(UUID idUsuario, UUID idPlano) {
        var usuario = usuarioRepositori.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var plano = planoRepositori.findById(idPlano)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setUsuario(usuario);
        novoPagamento.setPlano(plano);
        novoPagamento.setDataPagamento(java.time.LocalDateTime.now());
        novoPagamento.setPagamentoAtivo(true);

        return pagamentoRepositori.save(novoPagamento);
    }
}