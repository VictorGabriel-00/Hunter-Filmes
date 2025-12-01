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
import org.junit.jupiter.api.Order;
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
    @Order(1)
    void deveProcessarPagamentoComSucesso() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Processar Pagamento");
        System.out.println("========================================");

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
        System.out.println("Detalhes do pagamento:");
        System.out.println("ID Pagamento: " + resultado.getId());
        System.out.println("Usuário: " + resultado.getUsuario().getNome());
        System.out.println("Plano: " + resultado.getPlano().getNome());
        System.out.println("Valor: R$ " + String.format("%.2f", resultado.getValor()));
        System.out.println("Status: " + (resultado.isPagamentoAtivo() ? "ATIVO" : "INATIVO"));
        System.out.println("Data: " + resultado.getDataPagamento());
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Não deve processar pagamento se usuário não existir")
    @Order(2)
    void naoDeveProcessarPagamentoUsuarioNaoEncontrado() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Processar Pagamento - Usuário Inexistente");
        System.out.println("========================================");


        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, 29.90f);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));
        System.out.println("Usuário não encontrado ao tentar processar pagamento.");

        System.out.println("Comportamento correto!");
        System.out.println("Detalhes:");
        System.out.println("ID Usuário: " + usuarioId);
        System.out.println("Erro: " + exception.getMessage());
        System.out.println("Ação: Pagamento não processado");
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Não deve processar pagamento se plano não existir")
    @Order(3)
    void naoDeveProcessarPagamentoPlanoNaoEncontrado() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Processar Pagamento - Plano Inexistente");
        System.out.println("========================================");

        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(planoRepositori.findById(planoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, 29.90f);
        });

        assertEquals("Plano não encontrado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));

        System.out.println("Comportamento correto!");
        System.out.println("Detalhes:");
        System.out.println("ID Plano: " + planoId);
        System.out.println("Erro: " + exception.getMessage());
        System.out.println("Ação: Pagamento não processado");
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Não deve processar pagamento com valor insuficiente")
    @Order(4)
    void naoDeveProcessarPagamentoValorInsuficiente() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Processar Pagamento - Valor Insuficiente");
        System.out.println("========================================");

        float valorInsuficiente = 10.00f;

        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(planoRepositori.findById(planoId)).thenReturn(Optional.of(plano));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(usuarioId, planoId, valorInsuficiente);
        });

        assertEquals("Valor insuficiente para o plano selecionado", exception.getMessage());
        verify(pagamentoRepositori, never()).save(any(Pagamento.class));

        System.out.println("Comportamento correto!");
        System.out.println("Detalhes:");
        System.out.println("Plano: " + plano.getNome());
        System.out.println("Valor Necessário: R$ " + String.format("%.2f", plano.getPreco()));
        System.out.println("Valor Enviado: R$ " + String.format("%.2f", valorInsuficiente));
        System.out.println("Falta: R$ " + String.format("%.2f", (plano.getPreco() - valorInsuficiente)));
        System.out.println("Erro: " + exception.getMessage());
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Deve verificar se pagamento está ativo com sucesso")
    @Order(5)
    void deveVerificarPagamentoAtivo() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Verificar Status do Pagamento");
        System.out.println("========================================");

        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.of(pagamento));

        boolean ativo = pagamentoService.verificarPagamentoAtivo(pagamentoId);

        assertTrue(ativo);
        verify(pagamentoRepositori, times(1)).findById(pagamentoId);

        System.out.println("Verificação realizada!");
        System.out.println("Detalhes:");
        System.out.println("ID Pagamento: " + pagamentoId);
        System.out.println("Status: ATIVO");
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Deve retornar false se pagamento não existir ou estiver inativo")
    @Order(6)
    void deveRetornarFalsePagamentoInexistenteOuInativo() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Verificar Pagamento Inexistente");
        System.out.println("========================================");

        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.empty());
        assertFalse(pagamentoService.verificarPagamentoAtivo(pagamentoId));



        pagamento.setPagamentoAtivo(false);
        when(pagamentoRepositori.findById(pagamentoId)).thenReturn(Optional.of(pagamento));
        assertFalse(pagamentoService.verificarPagamentoAtivo(pagamentoId));

        System.out.println("Pagamento inexistente:");
        System.out.println("ID: " + pagamentoId);
        System.out.println("Resultado: NÃO ENCONTRADO\n");
        System.out.println("========================================\n");


    }

    @Test
    @DisplayName("Deve verificar assinatura e desativar pagamentos expirados")
    @Order(7)
    void deveVerificarAssinaturaEDesativarExpirados() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Verificação de Assinaturas Expiradas");
        System.out.println("========================================");

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

        System.out.println("Estado inicial:");
        System.out.println("Total de assinaturas ativas: " + assinaturasAtivas.size());
        System.out.println("\nAssinatura 1:");
        System.out.println("ID: " + pagamentoRecente.getId());
        System.out.println("Data: " + pagamentoRecente.getDataPagamento());
        System.out.println("Dias: 10 dias atrás");
        System.out.println("Status: ATIVO");
        System.out.println("\nAssinatura 2:");
        System.out.println("ID: " + pagamentoExpirado.getId());
        System.out.println("Data: " + pagamentoExpirado.getDataPagamento());
        System.out.println("Dias: 31 dias atrás");
        System.out.println("Status: EXPIRADO!\n");

        pagamentoService.verificacaoAssinatura();

        assertTrue(pagamentoRecente.isPagamentoAtivo(), "Pagamento recente deve continuar ativo");
        assertFalse(pagamentoExpirado.isPagamentoAtivo(), "Pagamento antigo deve ser desativado");

        verify(pagamentoRepositori, times(1)).saveAll(assinaturasAtivas);
        System.out.println("Verificação executada com sucesso!");
        System.out.println("Estado final:");
        System.out.println("Assinatura 1: " + (pagamentoRecente.isPagamentoAtivo() ? "ATIVA" : "INATIVA"));
        System.out.println("Assinatura 2: " + (pagamentoExpirado.isPagamentoAtivo() ? "ATIVA" : "INATIVA"));
        System.out.println("\nAssinatura 2 foi desativada por ter mais de 30 dias!");
        System.out.println("========================================\n");
    }
}