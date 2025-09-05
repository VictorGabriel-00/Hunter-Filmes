package com.hunterFilmes.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    public UUID id_plano() {
        return id_plano;
    }

    public void setIdPlano(UUID id_plano) {
        this.id_plano = id_plano;
    }

    public String nome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float preco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public String descricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
