package com.hunterFilmes.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id_filme;
    private String titulo;
    private String descicao;
    private String anoLancamento;
    private Float duracao;

    public UUID id_filme() {
        return id_filme;
    }

    public void setIdFilme(UUID id_filme) {
        this.id_filme = id_filme;
    }

    public String titulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String descicao() {
        return descicao;
    }

    public void setDescicao(String descicao) {
        this.descicao = descicao;
    }

    public String anoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public Float duracao() {
        return duracao;
    }

    public void setDuracao(Float duracao) {
        this.duracao = duracao;
    }
}
