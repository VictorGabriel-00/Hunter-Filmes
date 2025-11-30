package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Model.Pagamento;
import com.hunterFilmes.demo.Model.Plano;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.PagamentoRepositori;
import com.hunterFilmes.demo.Repositori.PlanoRepositori;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import com.hunterFilmes.demo.Service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepositori pagamentoRepositori;

    @Mock
    private UsuarioRepositori usuarioRepositori;

    @Mock
    private PlanoRepositori planoRepositori;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Usuario usuario;
    private Plano plano;
    private Pagamento pagamento;
    private UUID usuarioId;
    private UUID planoId;
    private UUID pagamentoId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        planoId = UUID.randomUUID();
        pagamentoId = UUID.randomUUID();

        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Raica Lira");
        usuario.setEmail("RaicaLira@gemail.com");

        plano = new Plano();
        plano.setId_plano(planoId);
        plano.setNome("Plano Premium");
        plano.setPreco(29.90f);

        pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setUsuario(usuario);
        pagamento.setPlano(plano);
        pagamento.setValor(29.90f);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setPagamentoAtivo(true);
    }

    @Test
    @DisplayName("Deve processar pagamento com sucesso")
    void deveProcessarPagamentoComSucesso() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(planoRepositori.findById(planoId)).thenReturn(Optional.of(plano));
        when(pagamentoRepositori.save(any(Pagamento.class))).thenReturn(pagamento);


        Pagamento resultado = pagamentoService.processarPagamento(usuarioId, planoId, 29.90f);

        assertNotNull(resultado);
        assertTrue(resultado.isPagamentoAtivo());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(plano, resultado.getPlano());
        assertEquals(29.90f, resultado.getValor());

        verify(usuarioRepositori, times(1)).findById(usuarioId);
        verify(planoRepositori, times(1)).findById(planoId);
        verify(pagamentoRepositori, times(1)).save(any(Pagamento.class));

        System.out.println("Pagamento processado com sucesso!");
    }

    @Test
    @DisplayName("Não deve processar pagamento se usuário não existir")
    void naoDeveProcessarPagamentoUsuarioNaoEncontrado() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, 29.90f);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));
        System.out.println("Usuário não encontrado ao tentar processar pagamento.");
    }

    @Test
    @DisplayName("Não deve processar pagamento se plano não existir")
    void naoDeveProcessarPagamentoPlanoNaoEncontrado() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(planoRepositori.findById(planoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, 29.90f);
        });

        assertEquals("Plano não encontrado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Não deve processar pagamento com valor insuficiente")
    void naoDeveProcessarPagamentoValorInsuficiente() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(planoRepositori.findById(planoId)).thenReturn(Optional.of(plano));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, 10.00f);
        });

        assertEquals("Valor insuficiente para o plano selecionado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve verificar se pagamento está ativo com sucesso")
    void deveVerificarPagamentoAtivo() {
        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.of(pagamento));

        boolean ativo = pagamentoService.verificarPagamentoAtivo(pagamentoId);

        assertTrue(ativo);
        verify(pagamentoRepositori, times(1)).findById(pagamentoId);
    }

    @Test
    @DisplayName("Deve retornar false se pagamento não existir ou estiver inativo")
    void deveRetornarFalsePagamentoInexistenteOuInativo() {
        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.empty());
        assertFalse(pagamentoService.verificarPagamentoAtivo(pagamentoId));

        pagamento.setPagamentoAtivo(false);
        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.of(pagamento));
        assertFalse(pagamentoService.verificarPagamentoAtivo(pagamentoId));
    }

    @Test
    @DisplayName("Deve verificar assinatura e desativar pagamentos expirados")
    void deveVerificarAssinaturaEDesativarExpirados() {
        Pagamento pagamentoRecente = new Pagamento();
        pagamentoRecente.setId(UUID.randomUUID());
        pagamentoRecente.setDataPagamento(LocalDateTime.now().minusDays(10));
        pagamentoRecente.setPagamentoAtivo(true);

        Pagamento pagamentoExpirado = new Pagamento();
        pagamentoExpirado.setId(UUID.randomUUID());
        pagamentoExpirado.setDataPagamento(LocalDateTime.now().minusDays(31));
        pagamentoExpirado.setPagamentoAtivo(true);

        List<Pagamento> assinaturasAtivas = Arrays.asList(pagamentoRecente, pagamentoExpirado);

        when(pagamentoRepositori.findBypagamentoAtivo(true)).thenReturn(assinaturasAtivas);

        pagamentoService.verificacaoAssinatura();

        assertTrue(pagamentoRecente.isPagamentoAtivo(), "Pagamento recente deve continuar ativo");
        assertFalse(pagamentoExpirado.isPagamentoAtivo(), "Pagamento antigo deve ser desativado");

        verify(pagamentoRepositori, times(1)).saveAll(assinaturasAtivas);
        System.out.println("Verificação de assinatura executada com sucesso!");
    }
}