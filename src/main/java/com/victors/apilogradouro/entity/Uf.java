package com.victors.apilogradouro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: criar entidade Cidade com relacionamento com UF
// TODO: adicionar campos de auditoria createdAt e updatedAt

@Entity
@Table(name = "uf")
@Getter
@Setter
@NoArgsConstructor // obrigatório pelo JPA
@AllArgsConstructor
@Builder
public class Uf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment delegado ao banco
    private Long id;

    @Column(nullable = false, unique = true, length = 2) // constraint UNIQUE, VARCHAR(2)
    private String sigla;

    @Column(nullable = false, length = 100)
    private String nome;
}
