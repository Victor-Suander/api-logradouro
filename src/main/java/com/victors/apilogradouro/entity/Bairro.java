package com.victors.apilogradouro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: criar entidade Logradouro com relacionamento com Bairro

@Entity
@Table(name = "bairro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bairro extends Auditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    // @ManyToOne: muitos bairros pertencem a uma única Cidade
    // @JoinColumn: define a coluna de FK (cidade_id) na tabela bairro
    @ManyToOne
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;
}
