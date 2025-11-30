package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Model.Pagamento;
import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Repositori.PagamentoRepositori;
import com.hunterFilmes.demo.Repositori.PlanoRepositori;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    public Pagamento processarPagamento(UUID idUsuario, UUID idPlano,float valor) {

        var usuario = usuarioRepositori.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var plano = planoRepositori.findById(idPlano)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        if(valor < plano.getPreco()) {
            throw new RuntimeException("Valor insuficiente para o plano selecionado");
        }


        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setUsuario(usuario);
        novoPagamento.setPlano(plano);
        novoPagamento.setDataPagamento(java.time.LocalDateTime.now());
        novoPagamento.setPagamentoAtivo(true);

        return pagamentoRepositori.save(novoPagamento);
    }


    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public boolean verificacaoAssinatura() {
        List<Pagamento> assinaturasAtivas = pagamentoRepositori.findBypagamentoAtivo(true);

        LocalDateTime hoje = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        for (Pagamento assinatura : assinaturasAtivas) {

            LocalDateTime dataInicio = assinatura.getDataPagamento();

            if (dataInicio == null) {
                continue;
            }

            long diasPassados = ChronoUnit.DAYS.between(dataInicio, hoje);

            if (diasPassados >= 30) {
                assinatura.setPagamentoAtivo(false);
                System.out.println("Assinatura ID " + assinatura.getId() + " expirou.");
            }
        }

        pagamentoRepositori.saveAll(assinaturasAtivas);
        return true;
    }


    public boolean verificarPagamentoAtivo(UUID idPagamento) {
        Optional<Pagamento> pagamentoOp = pagamentoRepositori.findById(idPagamento);

        if (pagamentoOp.isPresent()) {
            Pagamento pagamento = pagamentoOp.get();
            return pagamento.isPagamentoAtivo();
        }

        return false;
    }
}


