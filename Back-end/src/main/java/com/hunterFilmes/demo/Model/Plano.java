package com.hunterFilmes.demo.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.hunterFilmes.demo.Model.Pagamento;

import java.util.UUID;

@Entity
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id_plano;
    private String nome;
    private Float preco;
    private String descricao;
    private boolean removerAnuncio;
    private boolean permitiBaixar;

    public UUID getId_plano() {
        return id_plano;
    }

    public void setId_plano(UUID id_plano) {
        this.id_plano = id_plano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isRemoverAnuncio() {
        return removerAnuncio;
    }

    public void setRemoverAnuncio(boolean removerAnuncio) {
        this.removerAnuncio = removerAnuncio;
    }

    public boolean isPermitiBaixar() {
        return permitiBaixar;
    }

    public void setPermitiBaixar(boolean permitiBaixar) {
        this.permitiBaixar = permitiBaixar;
    }
}
