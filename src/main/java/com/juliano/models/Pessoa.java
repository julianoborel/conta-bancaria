package com.juliano.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "pessoas")
@Getter
@Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String nome;

    @NotNull
    private String cpf;

    @NotNull
    private String endereco;

    @OneToMany(mappedBy = "pessoa")
    private List<Conta> contas;

}