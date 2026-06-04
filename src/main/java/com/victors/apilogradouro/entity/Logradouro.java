package com.victors.apilogradouro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Table(name = "logradouro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    // @Enumerated(STRING): persiste o nome do enum ("RUA", "AVENIDA") em vez do índice numérico (ORDINAL)
    // usar ORDINAL é frágil — reordenar o enum corromperia os dados existentes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLogradouro tipo;

    @Column(nullable = false, length = 9)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;
}
