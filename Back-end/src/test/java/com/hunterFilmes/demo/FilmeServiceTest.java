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
        filme.setDescricao("A saga da família Corleone no mundo do crime organizado");
        filme.setAnoLancamento("1972");
        filme.setDuracao(175.0f);

        filmeDto = new FilmeDto(
                "O Poderoso Chefão",
                "A saga da família Corleone no mundo do crime organizado",
                "1972",
                175.0f
        );
    }

    @Test
    @DisplayName("Deve criar um filme com sucesso")
    void deveCriarFilmeComSucesso() {
        // Arrange (Preparação)
        when(filmeRepositori.save(any(Filme.class))).thenReturn(filme);

        // Act (Ação)
        Filme resultado = filmeService.criarFilme(filmeDto);

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals("O Poderoso Chefão", resultado.getTitulo());
        assertEquals("A saga da família Corleone no mundo do crime organizado", resultado.getDescricao());
        assertEquals("1972", resultado.getAnoLancamento());
        assertEquals(175.0f, resultado.getDuracao());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    void deveListarTodosFilmes() {
        // Arrange
        Filme filme2 = new Filme();
        filme2.setId(UUID.randomUUID());
        filme2.setTitulo("Pulp Fiction");
        filme2.setDescricao("Histórias entrelaçadas do submundo do crime");
        filme2.setAnoLancamento("1994");
        filme2.setDuracao(154.0f);

        List<Filme> filmes = Arrays.asList(filme, filme2);
        when(filmeRepositori.findAll()).thenReturn(filmes);

        // Act
        List<Filme> resultado = filmeService.listarTodosFilmes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("O Poderoso Chefão", resultado.get(0).getTitulo());
        assertEquals("Pulp Fiction", resultado.get(1).getTitulo());
        assertEquals(175.0f, resultado.get(0).getDuracao());
        assertEquals(154.0f, resultado.get(1).getDuracao());
        verify(filmeRepositori, times(1)).findAll();
    }

//    @Test
//    @DisplayName("Deve buscar filme por ID com sucesso")
//    void deveBuscarFilmePorIdComSucesso() {
//        // Arrange
//        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
//
//        // Act
//        Optional<Filme> resultado = filmeService.buscarFilmePorId(filmeId);
//
//        // Assert
//        assertTrue(resultado.isPresent());
//        assertEquals("O Poderoso Chefão", resultado.get().getTitulo());
//        assertEquals(filmeId, resultado.get().getId());
//        assertEquals(175.0f, resultado.get().getDuracao());
//        verify(filmeRepositori, times(1)).findById(filmeId);
//    }

//    @Test
//    @DisplayName("Deve retornar Optional vazio quando filme não existir")
//    void deveRetornarOptionalVazioQuandoFilmeNaoExistir() {
//        // Arrange
//        UUID idInexistente = UUID.randomUUID();
//        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());
//
//        // Act
//        Optional<Filme> resultado = filmeService.buscarFilmePorId(idInexistente);
//
//        // Assert
//        assertFalse(resultado.isPresent());
//        assertTrue(resultado.isEmpty());
//        verify(filmeRepositori, times(1)).findById(idInexistente);
//    }

    @Test
    @DisplayName("Deve atualizar filme com sucesso")
    void deveAtualizarFilmeComSucesso() {
        // Arrange
        FilmeDto filmeDtoAtualizado = new FilmeDto(
                "O Poderoso Chefão - Versão do Diretor",
                "Descrição atualizada do filme",
                "1972",
                200.0f
        );

        Filme filmeAtualizado = new Filme();
        filmeAtualizado.setId(filmeId);
        filmeAtualizado.setTitulo("O Poderoso Chefão - Versão do Diretor");
        filmeAtualizado.setDescricao("Descrição atualizada do filme");
        filmeAtualizado.setAnoLancamento("1972");
        filmeAtualizado.setDuracao(200.0f);

        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeAtualizado);

        // Act
        Optional<Filme> resultado = filmeService.atualizarFilme(filmeId, filmeDtoAtualizado);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("O Poderoso Chefão - Versão do Diretor", resultado.get().getTitulo());
        assertEquals("Descrição atualizada do filme", resultado.get().getDescricao());
        assertEquals(200.0f, resultado.get().getDuracao());
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Não deve atualizar filme quando ID não existir")
    void naoDeveAtualizarFilmeQuandoIdNaoExistir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        // Act
        Optional<Filme> resultado = filmeService.atualizarFilme(idInexistente, filmeDto);

        // Assert
        assertFalse(resultado.isPresent());
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve deletar filme com sucesso")
    void deveDeletarFilmeComSucesso() {
        // Arrange
        when(filmeRepositori.findById(filmeId)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepositori).delete(filme);

        // Act
        boolean resultado = filmeService.deletarFilme(filmeId);

        // Assert
        assertTrue(resultado);
        verify(filmeRepositori, times(1)).findById(filmeId);
        verify(filmeRepositori, times(1)).delete(filme);
    }

    @Test
    @DisplayName("Não deve deletar filme quando ID não existir")
    void naoDeveDeletarFilmeQuandoIdNaoExistir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        // Act
        boolean resultado = filmeService.deletarFilme(idInexistente);

        // Assert
        assertFalse(resultado);
        verify(filmeRepositori, times(1)).findById(idInexistente);
        verify(filmeRepositori, never()).delete(any(Filme.class));
    }

    @Test
    @DisplayName("Deve verificar se filme existe")
    void deveVerificarSeFilmeExiste() {
        // Arrange
        when(filmeRepositori.existsById(filmeId)).thenReturn(true);

        // Act
        boolean resultado = filmeService.filmeExiste(filmeId);

        // Assert
        assertTrue(resultado);
        verify(filmeRepositori, times(1)).existsById(filmeId);
    }

    @Test
    @DisplayName("Deve retornar false quando filme não existe")
    void deveRetornarFalseQuandoFilmeNaoExiste() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(filmeRepositori.existsById(idInexistente)).thenReturn(false);

        // Act
        boolean resultado = filmeService.filmeExiste(idInexistente);

        // Assert
        assertFalse(resultado);
        verify(filmeRepositori, times(1)).existsById(idInexistente);
    }

    @Test
    @DisplayName("Deve criar filme com duração curta")
    void deveCriarFilmeComDuracaoCurta() {
        // Arrange
        FilmeDto filmeCurto = new FilmeDto(
                "Curta Metragem",
                "Um curta metragem premiado",
                "2020",
                15.5f
        );

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(UUID.randomUUID());
        filmeSalvo.setTitulo("Curta Metragem");
        filmeSalvo.setDescricao("Um curta metragem premiado");
        filmeSalvo.setAnoLancamento("2020");
        filmeSalvo.setDuracao(15.5f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeSalvo);

        // Act
        Filme resultado = filmeService.criarFilme(filmeCurto);

        // Assert
        assertNotNull(resultado);
        assertEquals(15.5f, resultado.getDuracao());
        assertEquals("Curta Metragem", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }

    @Test
    @DisplayName("Deve criar filme com duração longa")
    void deveCriarFilmeComDuracaoLonga() {
        // Arrange
        FilmeDto filmeLongo = new FilmeDto(
                "O Senhor dos Anéis: O Retorno do Rei - Versão Estendida",
                "Épico de fantasia final da trilogia",
                "2003",
                263.0f
        );

        Filme filmeSalvo = new Filme();
        filmeSalvo.setId(UUID.randomUUID());
        filmeSalvo.setTitulo("O Senhor dos Anéis: O Retorno do Rei - Versão Estendida");
        filmeSalvo.setDescricao("Épico de fantasia final da trilogia");
        filmeSalvo.setAnoLancamento("2003");
        filmeSalvo.setDuracao(263.0f);

        when(filmeRepositori.save(any(Filme.class))).thenReturn(filmeSalvo);

        // Act
        Filme resultado = filmeService.criarFilme(filmeLongo);

        // Assert
        assertNotNull(resultado);
        assertEquals(263.0f, resultado.getDuracao());
        assertEquals("O Senhor dos Anéis: O Retorno do Rei - Versão Estendida", resultado.getTitulo());
        verify(filmeRepositori, times(1)).save(any(Filme.class));
    }
}