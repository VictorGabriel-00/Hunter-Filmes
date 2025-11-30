package com.hunterFilmes.demo.Repositori;

import com.hunterFilmes.demo.Model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepositori extends JpaRepository<Pagamento, UUID> {
    List<Pagamento> findBypagamentoAtivo(boolean pagamentoAtivo);
    Optional<Pagamento> findByUsuarioIdAndPagamentoAtivoTrue(UUID idUsuario);
}
