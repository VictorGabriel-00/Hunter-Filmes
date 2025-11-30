package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Dto.SerieDto;
import com.hunterFilmes.demo.Model.Serie;
import com.hunterFilmes.demo.Repositori.SerieRepositori;
import com.hunterFilmes.demo.Service.SerieService;
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
class SerieServiceTest {

    @Mock
    private SerieRepositori serieRepositori;

    @InjectMocks
    private SerieService serieService;

    private Serie serie;
    private SerieDto serieDto;
    private UUID serieId;

    @BeforeEach
    void setUp() {
        serieId = UUID.randomUUID();

        serie = new Serie();
        serie.setId(serieId);
        serie.setTitulo("Dexter");
        serie.setDescricao("Assassino");
        serie.setAnoLancamento("2006");
        serie.setTemporadas(8);

        serieDto = new SerieDto(
                "Dexter",
                "Assassino",
                "2006",
                8
        );
    }

    @Test
    @DisplayName("Deve criar uma série com sucesso")
    void deveCriarSerieComSucesso() {

        when(serieRepositori.save(any(Serie.class))).thenReturn(serie);

        Serie resultado = serieService.criarSerie(serieDto);

        assertNotNull(resultado);
        assertEquals("Dexter", resultado.getTitulo());
        assertEquals("Assassino", resultado.getDescricao());
        assertEquals("2006", resultado.getAnoLancamento());
        assertEquals(8, resultado.getTemporadas());
        verify(serieRepositori, times(1)).save(any(Serie.class));

        System.out.println("Serie Criada com sucesso!!");
        System.out.println("Nome: " + serie.getTitulo());
    }

    @Test
    @DisplayName("Deve listar todas as séries")
    void deveListarTodasSeries() {
        Serie serie2 = new Serie();
        serie2.setId(UUID.randomUUID());
        serie2.setTitulo("Stranger Things");
        serie2.setDescricao("Criança");
        serie2.setAnoLancamento("2016");
        serie2.setTemporadas(4);

        List<Serie> series = Arrays.asList(serie, serie2);
        when(serieRepositori.findAll()).thenReturn(series);

        List<Serie> resultado = serieService.listarTodasSeries();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Dexter", resultado.get(0).getTitulo());
        assertEquals("Stranger Things", resultado.get(1).getTitulo());
        assertEquals(8, resultado.get(0).getTemporadas());
        assertEquals(4, resultado.get(1).getTemporadas());
        verify(serieRepositori, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar série por ID com sucesso")
    void deveBuscarSeriePorIdComSucesso() {
        when(serieRepositori.findById(serieId)).thenReturn(Optional.of(serie));

        Optional<Serie> resultado = serieService.buscarSeriePorId(serieId);

        assertTrue(resultado.isPresent());
        assertEquals("Dexter", resultado.get().getTitulo());
        assertEquals(serieId, resultado.get().getId());
        assertEquals(8, resultado.get().getTemporadas());
        verify(serieRepositori, times(1)).findById(serieId);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando série não existir")
    void deveRetornarOptionalVazioQuandoSerieNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(serieRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Serie> resultado = serieService.buscarSeriePorId(idInexistente);

        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(serieRepositori, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve atualizar série com sucesso")
    void deveAtualizarSerieComSucesso() {
        SerieDto serieDtoAtualizada = new SerieDto(
                "Breaking Bad",
                "Professor",
                "2008",
                6
        );

        Serie serieAtualizada = new Serie();
        serieAtualizada.setId(serieId);
        serieAtualizada.setTitulo("Breaking Bad");
        serieAtualizada.setDescricao("Professor");
        serieAtualizada.setAnoLancamento("2008");
        serieAtualizada.setTemporadas(6);

        when(serieRepositori.findById(serieId)).thenReturn(Optional.of(serie));
        when(serieRepositori.save(any(Serie.class))).thenReturn(serieAtualizada);

        Optional<Serie> resultado = serieService.atualizarSerie(serieId, serieDtoAtualizada);

        assertTrue(resultado.isPresent());
        assertEquals("Breaking Bad", resultado.get().getTitulo());
        assertEquals("Professor", resultado.get().getDescricao());
        assertEquals(6, resultado.get().getTemporadas());
        verify(serieRepositori, times(1)).findById(serieId);
        verify(serieRepositori, times(1)).save(any(Serie.class));
    }

    @Test
    @DisplayName("Não deve atualizar série quando ID não existir")
    void naoDeveAtualizarSerieQuandoIdNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(serieRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Serie> resultado = serieService.atualizarSerie(idInexistente, serieDto);

        assertFalse(resultado.isPresent());
        verify(serieRepositori, times(1)).findById(idInexistente);
        verify(serieRepositori, never()).save(any(Serie.class));
    }

    @Test
    @DisplayName("Deve deletar série com sucesso")
    void deveDeletarSerieComSucesso() {
        when(serieRepositori.findById(serieId)).thenReturn(Optional.of(serie));
        doNothing().when(serieRepositori).delete(serie);

        boolean resultado = serieService.deletarSerie(serieId);

        assertTrue(resultado);
        verify(serieRepositori, times(1)).findById(serieId);
        verify(serieRepositori, times(1)).delete(serie);
    }

    @Test
    @DisplayName("Não deve deletar série quando ID não existir")
    void naoDeveDeletarSerieQuandoIdNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(serieRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        boolean resultado = serieService.deletarSerie(idInexistente);

        assertFalse(resultado);
        verify(serieRepositori, times(1)).findById(idInexistente);
        verify(serieRepositori, never()).delete(any(Serie.class));
    }

    @Test
    @DisplayName("Deve verificar se série existe")
    void deveVerificarSeSerieExiste() {
        when(serieRepositori.existsById(serieId)).thenReturn(true);

        boolean resultado = serieService.serieExiste(serieId);

        assertTrue(resultado);
        verify(serieRepositori, times(1)).existsById(serieId);
    }

    @Test
    @DisplayName("Deve retornar false quando série não existe")
    void deveRetornarFalseQuandoSerieNaoExiste() {
        UUID idInexistente = UUID.randomUUID();
        when(serieRepositori.existsById(idInexistente)).thenReturn(false);

        boolean resultado = serieService.serieExiste(idInexistente);

        assertFalse(resultado);
        verify(serieRepositori, times(1)).existsById(idInexistente);
    }

    @Test
    @DisplayName("Nao deve criar serie se a temporada for igual a 0")
    void naoCriarSerieCom0Temporadas() {

        SerieDto serieComMuitasTemporadas = new SerieDto(
                "Grey's Anatomy",
                "Drama médico de longa duração",
                "2005",
                0
        );

        Serie serieSalva = new Serie();
        serieSalva.setId(UUID.randomUUID());
        serieSalva.setTitulo("Grey's Anatomy");
        serieSalva.setDescricao("Drama médico de longa duração");
        serieSalva.setAnoLancamento("2005");
        serieSalva.setTemporadas(0);

        when(serieRepositori.save(any(Serie.class))).thenReturn(serieSalva);

        Serie resultado = serieService.criarSerie(serieComMuitasTemporadas);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTemporadas());
        assertEquals("Grey's Anatomy", resultado.getTitulo());
        verify(serieRepositori, times(1)).save(any(Serie.class));
    }

    @Test
    @DisplayName("Nao deve criar serie se a temporada for igual a 0")
    void naoCriarComTemporadaNegativa() {

        SerieDto serieComMuitasTemporadas = new SerieDto(
                "B99",
                "Policial",
                "2010",
                -1
        );

        Serie serieSalva = new Serie();
        serieSalva.setId(UUID.randomUUID());
        serieSalva.setTitulo("B99");
        serieSalva.setDescricao("Policial");
        serieSalva.setAnoLancamento("2010");
        serieSalva.setTemporadas(-1);

        when(serieRepositori.save(any(Serie.class))).thenReturn(serieSalva);

        Serie resultado = serieService.criarSerie(serieComMuitasTemporadas);

        assertNotNull(resultado);
        assertEquals(-1, resultado.getTemporadas());
        assertEquals("B99", resultado.getTitulo());
        verify(serieRepositori, times(1)).save(any(Serie.class));
    }


}