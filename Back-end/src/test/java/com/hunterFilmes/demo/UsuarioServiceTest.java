package com.hunterFilmes.demo;

import com.hunterFilmes.demo.Dto.UsuarioDto;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import com.hunterFilmes.demo.Service.UsuarioService;
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
class UsuarioServiceTest {
    // Cria um repositorio "Falso" para realização dos testes
    @Mock
    private UsuarioRepositori usuarioRepositori;
    // injeta o mock no service
    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDto usuarioDto;
    private UUID usuarioId;

    // vai executar antes de tudo
    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();

        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Marcos Paulo");
        usuario.setEmail("MarcosJunior@gmail.com");
        usuario.setSenha("123456");
        usuario.setDataNascimento("27/09/2005");

        usuarioDto = new UsuarioDto(
                "Marcos Paulo",
                "MarcosJunior@gmail.com",
                "123456",
                "27/09/2005"
        );
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    @Order(1)
    void deveCriarUsuarioComSucesso() {
        // faz a preparação do que vai acontecer no teste
        when(usuarioRepositori.save(any(Usuario.class))).thenReturn(usuario);

        // chama o metodo que vai ser utilizado
        Usuario resultado = usuarioService.criarUsuario(usuarioDto);


        // são as verificações do teste pra ver se foi realizado com sucesso
        assertNotNull(resultado);
        assertEquals("Marcos Paulo", resultado.getNome());
        assertEquals("MarcosJunior@gmail.com", resultado.getEmail());
        verify(usuarioRepositori, times(1)).save(any(Usuario.class));

        // o resultado do teste
        System.out.println("Usuario criado com sucesso!!");
        System.out.println("Nome: " +  usuario.getNome());
        System.out.println("Email: " +  usuario.getEmail());
        System.out.println("Senha: " +  usuario.getSenha());
        System.out.println("Data de nascimento: " +  usuario.getDataNascimento());
    }
    // todos os testes segue o mesmo padrão acima

    @Test
    @DisplayName("Deve listar todos os usuários")
    @Order(2)
    void deveListarTodosUsuarios() {
        Usuario usuario2 = new Usuario();
        usuario2.setId(UUID.randomUUID());
        usuario2.setNome("Raica Lira");
        usuario2.setEmail("RaicaLira@gmail.com");

        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepositori.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodosUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Marcos Paulo", resultado.get(0).getNome());
        assertEquals("Raica Lira", resultado.get(1).getNome());
        verify(usuarioRepositori, times(1)).findAll();

        System.out.println("Usuario listado com sucesso!!");
        System.out.println("Usuario01: " +  usuarios.get(0).getNome());
        System.out.println("Usuario02: " +  usuarios.get(1).getNome());

    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    @Order(3)
    void deveBuscarUsuarioPorIdComSucesso() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorId(usuarioId);

        assertTrue(resultado.isPresent());
        assertEquals("Marcos Paulo", resultado.get().getNome());
        assertEquals(usuarioId, resultado.get().getId());
        verify(usuarioRepositori, times(1)).findById(usuarioId);

        System.out.println("Consulta pelo Id realizada com sucesso!!");
        System.out.println("Id: " + usuarioId);
        System.out.println("Nome: " + usuario.getNome());

    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existir")
    @Order(4)
    void deveRetornarOptionalVazioQuandoUsuarioNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(usuarioRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorId(idInexistente);

        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(usuarioRepositori, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    @Order(5)
    void deveAtualizarUsuarioComSucesso() {

        System.out.println("Usuario antigo");
        System.out.println("Id: " + usuarioId);
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("Data de nascimento: " + usuario.getDataNascimento() + "\n");

        UsuarioDto usuarioDtoAtualizado = new UsuarioDto(
                "Pedro Priori",
                "PedroPriori@gmail.com",
                "8888888",
                "05/12/2003"
        );

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("Pedro Priori");
        usuarioAtualizado.setEmail("PedroPriori@gmail.com");
        usuarioAtualizado.setSenha("8888888");
        usuarioAtualizado.setDataNascimento("05/12/2003");

        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepositori.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Optional<Usuario> resultado = usuarioService.atualizarUsuario(usuarioId, usuarioDtoAtualizado);

        assertTrue(resultado.isPresent());
        assertEquals("Pedro Priori", resultado.get().getNome());
        assertEquals("PedroPriori@gmail.com", resultado.get().getEmail());
        verify(usuarioRepositori, times(1)).findById(usuarioId);
        verify(usuarioRepositori, times(1)).save(any(Usuario.class));

        System.out.println("Usuario Atualizado com sucesso!!");
        System.out.println("Id: " + usuarioId);
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("Data de nascimento: " + usuario.getDataNascimento());

    }

    @Test
    @DisplayName("Não deve atualizar usuário quando ID não existir")
    @Order(6)
    void naoDeveAtualizarUsuarioQuandoIdNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        when(usuarioRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.atualizarUsuario(idInexistente, usuarioDto);

        assertFalse(resultado.isPresent());
        verify(usuarioRepositori, times(1)).findById(idInexistente);
        verify(usuarioRepositori, never()).save(any(Usuario.class));

        System.out.println("Id nao encontrado!!");

    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    @Order(7)
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepositori.findById(usuarioId)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepositori).delete(usuario);

        boolean resultado = usuarioService.deletarUsuario(usuarioId);

        assertTrue(resultado);
        verify(usuarioRepositori, times(1)).findById(usuarioId);
        verify(usuarioRepositori, times(1)).delete(usuario);

        System.out.println("Usuario deletado com sucesso!!");

    }

    @Test
    @DisplayName("Não deve deletar usuário quando ID não existir")
    @Order(8)
    void naoDeveDeletarUsuarioQuandoIdNaoExistir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(usuarioRepositori.findById(idInexistente)).thenReturn(Optional.empty());

        // Act
        boolean resultado = usuarioService.deletarUsuario(idInexistente);

        // Assert
        assertFalse(resultado);
        verify(usuarioRepositori, times(1)).findById(idInexistente);
        verify(usuarioRepositori, never()).delete(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve verificar se usuário existe")
    @Order(9)
    void deveVerificarSeUsuarioExiste() {
        when(usuarioRepositori.existsById(usuarioId)).thenReturn(true);

        boolean resultado = usuarioService.usuarioExiste(usuarioId);

        assertTrue(resultado);
        verify(usuarioRepositori, times(1)).existsById(usuarioId);
        System.out.println("Usuario existe!!");
    }

    @Test
    @DisplayName("Deve retornar false quando usuário não existe")
    @Order(10)
    void deveRetornarFalseQuandoUsuarioNaoExiste() {
        UUID idInexistente = UUID.randomUUID();
        when(usuarioRepositori.existsById(idInexistente)).thenReturn(false);

        boolean resultado = usuarioService.usuarioExiste(idInexistente);

        assertFalse(resultado);
        verify(usuarioRepositori, times(1)).existsById(idInexistente);

    }
}