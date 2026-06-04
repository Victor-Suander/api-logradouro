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

/**
 * Representa uma Unidade Federativa (estado) do Brasil.
 *
 * @Entity  — marca esta classe como uma entidade JPA, ou seja, será mapeada para uma tabela no banco de dados.
 * @Table   — define o nome da tabela no banco. Sem esta anotação, o JPA usaria o nome da classe como padrão.
 */
@Entity
@Table(name = "uf")

/**
 * Anotações do Lombok — eliminam o boilerplate de getters, setters e construtores:
 *
 * @Getter          — gera getters públicos para todos os campos.
 * @Setter          — gera setters públicos para todos os campos.
 * @NoArgsConstructor — gera construtor sem argumentos (exigido pelo JPA).
 * @AllArgsConstructor — gera construtor com todos os campos (útil para testes e instanciação manual).
 * @Builder         — habilita o padrão Builder: Uf.builder().sigla("SP").nome("São Paulo").build().
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Uf {

    /**
     * @Id             — marca este campo como chave primária da tabela.
     * @GeneratedValue — define a estratégia de geração do valor da PK.
     *                   IDENTITY delega ao banco o auto incremento (compatível com H2, MySQL e PostgreSQL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column — personaliza o mapeamento da coluna:
     *   nullable = false → NOT NULL no banco.
     *   unique   = true  → cria constraint UNIQUE na coluna.
     *   length   = 2     → limita a 2 caracteres (VARCHAR(2)).
     */
    @Column(nullable = false, unique = true, length = 2)
    private String sigla;

    /**
     * @Column — mesmas regras acima, mas sem unique.
     *   length = 100 → VARCHAR(100) para o nome completo do estado.
     */
    @Column(nullable = false, length = 100)
    private String nome;
}