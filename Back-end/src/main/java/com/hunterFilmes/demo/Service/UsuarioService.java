package com.hunterFilmes.demo.Service;

import com.hunterFilmes.demo.Dto.PlanoAtivoDto;
import com.hunterFilmes.demo.Dto.UsuarioDto;
import com.hunterFilmes.demo.Dto.UsuarioResponseDto;
import com.hunterFilmes.demo.Model.Pagamento;
import com.hunterFilmes.demo.Model.Usuario;
import com.hunterFilmes.demo.Repositori.PagamentoRepositori;
import com.hunterFilmes.demo.Repositori.UsuarioRepositori;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepositori usuarioRepositori;

    @Autowired
    private PagamentoRepositori pagamentoRepositori;

    @Transactional
    public Usuario criarUsuario(UsuarioDto usuarioDto) {
        var usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return usuarioRepositori.save(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepositori.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(UUID id) {
        return usuarioRepositori.findById(id);
    }

    public Optional<UsuarioResponseDto> buscarUsuarioComPlano(UUID id) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);

        if (usuarioOp.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOp.get();

        Optional<Pagamento> pagamentoAtivo = pagamentoRepositori
                .findByUsuarioIdAndPagamentoAtivoTrue(id);

        PlanoAtivoDto planoAtivoDto = null;

        if (pagamentoAtivo.isPresent()) {
            Pagamento pagamento = pagamentoAtivo.get();
            planoAtivoDto = new PlanoAtivoDto(
                    pagamento.getId(),
                    pagamento.getPlano().getId_plano(),
                    pagamento.getPlano().getNome(),
                    pagamento.getPlano().getPreco(),
                    pagamento.getPlano().getDescricao(),
                    pagamento.isPagamentoAtivo(),
                    pagamento.getDataPagamento()
            );
        }

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataNascimento(),
                planoAtivoDto
        );

        return Optional.of(responseDto);
    }

    @Transactional
    public Optional<Usuario> atualizarUsuario(UUID id, UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);
        if (usuarioOp.isEmpty()) {
            return Optional.empty();
        }
        var usuario = usuarioOp.get();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return Optional.of(usuarioRepositori.save(usuario));
    }

    @Transactional
    public boolean deletarUsuario(UUID id) {
        Optional<Usuario> usuarioOp = usuarioRepositori.findById(id);
        if (usuarioOp.isEmpty()) {
            return false;
        }
        usuarioRepositori.delete(usuarioOp.get());
        return true;
    }

    public boolean usuarioExiste(UUID id) {
        return usuarioRepositori.existsById(id);
    }
}