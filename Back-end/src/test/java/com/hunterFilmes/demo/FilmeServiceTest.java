package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Dto.FilmeDto;
import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Repositori.FilmeRepositori;
import com.hunterFilmes.demo.Service.FilmeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmeServiceTest {

    @Mock
    private FilmeRepositori filmeRepositori;

    @InjectMocks
    private FilmeService filmeService;

    private Filme filme;
    private FilmeDto filmeDto;
    private UUID filmeId;

    @BeforeEach
    void setUp() {
        filmeId = UUID.randomUUID();

        filme = new Filme();
        filme.setId(filmeId);
        filme.setTitulo("O Poderoso Chefão");
        filme.setDescricao("Mafioso");
        filme.setAnoLancamento("1972");
        filme.setDuracao(175.0f);

        filmeDto = new FilmeDto(
                "O Poderoso Chefão",
                "Mafioso",
                "1972",
                175.0f
        );
    }

    @Test
    @DisplayName("Deve criar um filme com sucesso")
    @Order(1)
    void deveCriarFilmeComSucesso() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Criar Filme");
        System.out.println("========================================");

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = filmeService.criarFilme(filmeDto);

        assertNotNull(resultado);
        assertEquals("O Poderoso Chefão", resultado.getTitulo());
        assertEquals("Mafioso", resultado.getDescricao());
        assertEquals("1972", resultado.getAnoLancamento());
        assertEquals(175.0f, resultado.getDuracao());
        verify(filmeRepositori, times(1)).save(any(Filme.class));

        System.out.println("Filme criado com sucesso!");
        System.out.println("Detalhes do filme:");
        System.out.println("ID: " + resultado.getId());
        System.out.println("Título: " + resultado.getTitulo());
        System.out.println("Descrição: " + resultado.getDescricao());
        System.out.println("Ano: " + resultado.getAnoLancamento());
        System.out.println("Duração: " + resultado.getDuracao() + " min");
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    @Order(2)
    void deveListarTodosFilmes() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Listar Todos os Filmes");
        System.out.println("========================================");
        Filme filme2 = new Filme();
        filme2.setId(UUID.randomUUID());
        filme2.setTitulo("Pulp Fiction");
        filme2.setDescricao("nao lembro");
        filme2.setAnoLancamento("1994");
        filme2.setDuracao(154.0f);

        List<Filme> filmes = Arrays.asList(filme, filme2);
        when(filmeRepositori.findAll()).thenReturn(filmes);

        List<Filme> resultado = filmeService.listarTodosFilmes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("O Poderoso Chefão", resultado.get(0).getTitulo());
        assertEquals("Pulp Fiction", resultado.get(1).getTitulo());
        assertEquals(175.0f, resultado.get(0).getDuracao());
        assertEquals(154.0f, resultado.get(1).getDuracao());
        verify(filmeRepositori, times(1)).findAll();

        System.out.println("Filmes listados com sucesso!");
        System.out.println("Total de filmes: " + resultado.size());
        for (int i = 0; i < resultado.size(); i++) {
            System.out.println("\nFilme " + (i + 1) + ":");
            System.out.println("Título: " + resultado.get(i).getTitulo());
            System.out.println("Ano: " + resultado.get(i).getAnoLancamento());
            System.out.println("Duração: " + resultado.get(i).getDuracao() + " min");
        }
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Deve buscar filme por ID com sucesso")
    @Order(3)
    void deveBuscarFilmePorIdComSucesso() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Buscar Filme por ID");
        System.out.println("========================================");
        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));

        Optional<Filme> resultado = filmeService.listarFilmesPorId(filmeId);

        assertTrue(resultado.isPresent());
        assertEquals("O Poderoso Chefão", resultado.get().getTitulo());
        assertEquals(filmeId, resultado.get().getId());
        assertEquals(175.0f, resultado.get().getDuracao());
        verify(filmeRepositori, times(1)).findById(filmeId);

        System.out.println("Filme encontrado com sucesso!");
        System.out.println("Detalhes:");
        System.out.println("ID buscado: " + filmeId);
        System.out.println("Título: " + resultado.get().getTitulo());
        System.out.println("Duração: " + resultado.get().getDuracao() + " min");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando filme não existir")
    @Order(4)
    void deveRetornarOptionalVazioQuandoFilmeNaoExistir() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Buscar Filme Inexistente");
        System.out.println("========================================");

        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Filme> resultado = filmeService.listarFilmesPorId(idInexistente);

        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(filmeRepositori, times(1)).findById(idInexistente);

        System.out.println("Comportamento correto!");
        System.out.println("ID buscado: " + idInexistente);
        System.out.println("Resultado: Optional vazio (filme não encontrado)");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve atualizar filme com sucesso")
    @Order(5)
    void deveAtualizarFilmeComSucesso() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Atualizar Filme");
        System.out.println("========================================");

        FilmeDto filmeDtoAtualizado = new FilmeDto(
                "O Hobbit",
                "Pequeno",
                "2012",
                167.0f
        );

        Filme filmeAtualizado = new Filme();
        filmeAtualizado.setId(filmeId);
        filmeAtualizado.setTitulo("O Hobbit");
        filmeAtualizado.setDescricao("Pequeno");
        filmeAtualizado.setAnoLancamento("2012");
        filmeAtualizado.setDuracao(167.0f);

        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeAtualizado);

        System.out.println("Dados anteriores:");
        System.out.println("Título: " + filme.getTitulo());
        System.out.println("Ano: " + filme.getAnoLancamento());
        System.out.println("Duração: " + filme.getDuracao() + " min\n");

        Optional<Filme> resultado = filmeService.atualizarFilme(filmeId, filmeDtoAtualizado);

        assertTrue(resultado.isPresent());
        assertEquals("O Hobbit", resultado.get().getTitulo());
        assertEquals("Pequeno", resultado.get().getDescricao());
        assertEquals(167.0f, resultado.get().getDuracao());
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).save(any(Filme.class));

        System.out.println("Filme atualizado com sucesso!");
        System.out.println("Novos dados:");
        System.out.println("Título: " + resultado.get().getTitulo());
        System.out.println("Ano: " + resultado.get().getAnoLancamento());
        System.out.println("Duração: " + resultado.get().getDuracao() + " min");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Não deve atualizar filme quando ID não existir")
    @Order(6)
    void naoDeveAtualizarFilmeQuandoIdNaoExistir() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Atualizar Filme Inexistente");
        System.out.println("========================================");

        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Filme> resultado = filmeService.atualizarFilme(idInexistente, filmeDto);

        assertFalse(resultado.isPresent());
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).save(any(Filme.class));

        System.out.println("Comportamento correto!");
        System.out.println("ID buscado: " + idInexistente);
        System.out.println("Resultado: Atualização não realizada");
        System.out.println("Motivo: Filme não encontrado");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve deletar filme com sucesso")
    @Order(7)
    void deveDeletarFilmeComSucesso() {

        System.out.println("\n========================================");
        System.out.println("TESTE: Deletar Filme");
        System.out.println("========================================");

        System.out.println("Filme a ser deletado:");
        System.out.println("ID: " + filmeId);
        System.out.println("Título: " + filme.getTitulo() + "\n");

        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepositori).delete(filme);

        boolean resultado = filmeService.deletarFilme(filmeId);

        assertTrue(resultado);
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).delete(filme);
        System.out.println("Filme deletado com sucesso!");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Não deve deletar filme quando ID não existir")
    @Order(8)
    void naoDeveDeletarFilmeQuandoIdNaoExistir() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Deletar Filme Inexistente");
        System.out.println("========================================");

        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        boolean resultado = filmeService.deletarFilme(idInexistente);

        assertFalse(resultado);
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).delete(any(Filme.class));

        System.out.println("Comportamento correto!");
        System.out.println("ID buscado: " + idInexistente);
        System.out.println("Resultado: Deleção não realizada");
        System.out.println("Motivo: Filme não encontrado");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve verificar se filme existe")
    @Order(9)
    void deveVerificarSeFilmeExiste() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Verificar Existência do Filme");
        System.out.println("========================================");

        when(filmeRepositori.existsById(filmeId)).thenReturn(true);

        boolean resultado = filmeService.filmeExiste(filmeId);

        assertTrue(resultado);
        verify(filmeRepositori, times(1)).existsById(filmeId);
        System.out.println("Verificação realizada!");
        System.out.println("ID: " + filmeId);
        System.out.println("Existe: SIM");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve retornar false quando filme não existe")
    @Order(10)
    void deveRetornarFalseQuandoFilmeNaoExiste() {
        System.out.println("\n========================================");
        System.out.println("TESTE: Verificar Filme Inexistente");
        System.out.println("========================================");

        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.existsById(idInexistente)).thenReturn(false);

        boolean resultado = filmeService.filmeExiste(idInexistente);

        assertFalse(resultado);
        verify(filmeRepositori, times(1)).existsById(idInexistente);
        System.out.println("Verificação realizada!");
        System.out.println("ID: " + idInexistente);
        System.out.println("Existe: NÃO");
        System.out.println("========================================\n");
    }

    @Test
    @DisplayName("Deve criar filme com duração curta")
    @Order(11)
    void deveCriarFilmeComDuracaoCurta() {
        FilmeDto filmeCurto = new FilmeDto(
                "Curta Metragem",
                "Um curta metragem",
                "2020",
                15.5f
        );

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(UUID.randomUUID());
        filmeSalvo.setTitulo("Curta Metragem");
        filmeSalvo.setDescricao("Um curta metragem");
        filmeSalvo.setAnoLancamento("2020");
        filmeSalvo.setDuracao(15.5f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeSalvo);

        Filme resultado = filmeService.criarFilme(filmeCurto);

        assertNotNull(resultado);
        assertEquals(15.5f, resultado.getDuracao());
        assertEquals("Curta Metragem", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve criar filme com duração longa")
    @Order(12)
    void deveCriarFilmeComDuracaoLonga() {
        FilmeDto filmeLongo = new FilmeDto(
                "O Senhor dos Anéis: O Retorno do Rei",
                "Golon",
                "2003",
                263.0f
        );

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(UUID.randomUUID());
        filmeSalvo.setTitulo("O Senhor dos Anéis: O Retorno do Rei");
        filmeSalvo.setDescricao("Golon");
        filmeSalvo.setAnoLancamento("2003");
        filmeSalvo.setDuracao(263.0f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeSalvo);

        Filme resultado = filmeService.criarFilme(filmeLongo);

        assertNotNull(resultado);
        assertEquals(263.0f, resultado.getDuracao());
        assertEquals("O Senhor dos Anéis: O Retorno do Rei", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Nao deve Criar Filme Com Duração igual a 0")
    @Order(13)
    void naoDeveCriarFilmeComDuraca0(){
        System.out.println("\n========================================");
        System.out.println("TESTE: Não Criar Filme com Duração 0");
        System.out.println("========================================");
        FilmeDto filmeD0 = new FilmeDto(
                "Como treinar seu Dragão",
                "Dragão pra tudo que é lado",
                "2010",
                00.0f
        );

        Filme filmeD1 = new Filme();
        filmeD1.setId(UUID.randomUUID());
        filmeD1.setTitulo("Como treinar seu Dragão");
        filmeD1.setDescricao("Dragão pra tudo que é lado");
        filmeD1.setAnoLancamento("2010");
        filmeD1.setDuracao(00.0f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeD1);

        Filme resultado = filmeService.criarFilme(filmeD0);

        assertNotNull(resultado);
        assertEquals(00.0f, resultado.getDuracao());
        assertEquals("Como treinar seu Dragão", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));

        System.out.println("Atenção: O Filme Não foi Criado com Sucesso");
        System.out.println("Detalhes:");
        System.out.println("Título: " + resultado.getTitulo());
        System.out.println("Duração: " + resultado.getDuracao() + " min (INVÁLIDO!)");
        System.out.println("========================================\n");

    }

    @Test
    @DisplayName("Nao deve Criar Filme Com Duração Negativa")
    @Order(14)
    void naoDeveCriarFilmeComDuracaoNegativa(){
        System.out.println("\n========================================");
        System.out.println("TESTE: Não Criar Filme com Duração Negativa");
        System.out.println("========================================");
        FilmeDto filmeD0 = new FilmeDto(
                "Como treinar seu Dragão",
                "Dragão pra tudo que é lado",
                "2010",
                -1.0f
        );

        Filme filmeD1 = new Filme();
        filmeD1.setId(UUID.randomUUID());
        filmeD1.setTitulo("Como treinar seu Dragão");
        filmeD1.setDescricao("Dragão pra tudo que é lado");
        filmeD1.setAnoLancamento("2010");
        filmeD1.setDuracao(-1.0f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeD1);

        Filme resultado = filmeService.criarFilme(filmeD0);

        assertNotNull(resultado);
        assertEquals(-1.0f, resultado.getDuracao());
        assertEquals("Como treinar seu Dragão", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));

        System.out.println("Atenção: O Filme Não foi Criado com Sucesso");
        System.out.println("Detalhes:");
        System.out.println("Título: " + resultado.getTitulo());
        System.out.println("Duração: " + resultado.getDuracao() + " min (NEGATIVO!)");
        System.out.println("========================================\n");

    }



}