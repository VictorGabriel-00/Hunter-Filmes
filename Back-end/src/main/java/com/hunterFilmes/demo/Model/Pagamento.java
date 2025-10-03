package com.hunterFilmes.demo.Model;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @ManyToOne
    private Plano plano;
    @ManyToOne
    private Usuario usuario;

    public UUID id() {
        return id;
    }

    public Plano plano() {
        return plano;
    }

}
