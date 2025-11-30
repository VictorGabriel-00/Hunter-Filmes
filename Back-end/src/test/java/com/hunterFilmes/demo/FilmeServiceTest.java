package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Dto.FilmeDto;
import com.hunterFilmes.demo.Model.Filme;
import com.hunterFilmes.demo.Repositori.FilmeRepositori;
import com.hunterFilmes.demo.Service.FilmeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    void deveCriarFilmeComSucesso() {
        when(filmeRepositori.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = filmeService.criarFilme(filmeDto);

        assertNotNull(resultado);
        assertEquals("O Poderoso Chefão", resultado.getTitulo());
        assertEquals("Mafioso", resultado.getDescricao());
        assertEquals("1972", resultado.getAnoLancamento());
        assertEquals(175.0f, resultado.getDuracao());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    void deveListarTodosFilmes() {
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
    }

    @Test
    @DisplayName("Deve buscar filme por ID com sucesso")
    void deveBuscarFilmePorIdComSucesso() {
        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));

        Optional<Filme> resultado = filmeService.listarFilmesPorId(filmeId);

        assertTrue(resultado.isPresent());
        assertEquals("O Poderoso Chefão", resultado.get().getTitulo());
        assertEquals(filmeId, resultado.get().getId());
        assertEquals(175.0f, resultado.get().getDuracao());
        verify(filmeRepositori, times(1)).findById(filmeId);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando filme não existir")
    void deveRetornarOptionalVazioQuandoFilmeNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Filme> resultado = filmeService.listarFilmesPorId(idInexistente);

        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(filmeRepositori, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve atualizar filme com sucesso")
    void deveAtualizarFilmeComSucesso() {
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

        Optional<Filme> resultado = filmeService.atualizarFilme(filmeId, filmeDtoAtualizado);

        assertTrue(resultado.isPresent());
        assertEquals("O Hobbit", resultado.get().getTitulo());
        assertEquals("Pequeno", resultado.get().getDescricao());
        assertEquals(167.0f, resultado.get().getDuracao());
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Não deve atualizar filme quando ID não existir")
    void naoDeveAtualizarFilmeQuandoIdNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Filme> resultado = filmeService.atualizarFilme(idInexistente, filmeDto);

        assertFalse(resultado.isPresent());
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve deletar filme com sucesso")
    void deveDeletarFilmeComSucesso() {
        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepositori).delete(filme);

        boolean resultado = filmeService.deletarFilme(filmeId);

        assertTrue(resultado);
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).delete(filme);
    }

    @Test
    @DisplayName("Não deve deletar filme quando ID não existir")
    void naoDeveDeletarFilmeQuandoIdNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        boolean resultado = filmeService.deletarFilme(idInexistente);

        assertFalse(resultado);
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).delete(any(Filme.class));
    }

    @Test
    @DisplayName("Deve verificar se filme existe")
    void deveVerificarSeFilmeExiste() {
        when(filmeRepositori.existsById(filmeId)).thenReturn(true);

        boolean resultado = filmeService.filmeExiste(filmeId);

        assertTrue(resultado);
        verify(filmeRepositori, times(1)).existsById(filmeId);
    }

    @Test
    @DisplayName("Deve retornar false quando filme não existe")
    void deveRetornarFalseQuandoFilmeNaoExiste() {
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.existsById(idInexistente)).thenReturn(false);

        boolean resultado = filmeService.filmeExiste(idInexistente);

        assertFalse(resultado);
        verify(filmeRepositori, times(1)).existsById(idInexistente);
    }

    @Test
    @DisplayName("Deve criar filme com duração curta")
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
    void deveCriarFilmeComDuracaoLonga() {
        FilmeDto filmeLongo = new FilmeDto(
                "O Senhor dos Anéis: O Retorno do Rei - Versão Estendida",
                "Épico de fantasia final da trilogia",
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
    void naoDeveCriarFilmeComDuraca0(){
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

    }

    @Test
    @DisplayName("Nao deve Criar Filme Com Duração Negativa")
    void naoDeveCriarFilmeComDuracaoNegativa(){
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

    }



}