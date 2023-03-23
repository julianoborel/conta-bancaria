package com.juliano.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @NotNull
    private Double valor;

    private String tipoOperacao;

}
