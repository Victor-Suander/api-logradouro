package com.victors.apilogradouro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// TODO: criar DTOs das outras entidades seguindo este padrão

/**
 * DTO de entrada para criação e atualização de UF.
 * Uso de record garante imutabilidade e elimina boilerplate de getters/construtor.
 *
 * As anotações de validação são processadas pelo Spring quando o parâmetro
 * no controller recebe @Valid — sem ela, as anotações abaixo são ignoradas.
 */
public record UfRequestDTO(

        // @NotBlank — rejeita null, string vazia "" e string com apenas espaços "   "
        // @Size(min=2, max=2) — exige exatamente 2 caracteres, padrão das siglas de UF (ex: SP, RJ)
        @NotBlank
        @Size(min = 2, max = 2)
        String sigla,

        // @NotBlank — mesma regra: não aceita nulo nem vazio
        // @Size(min=2, max=100) — nome deve ter entre 2 e 100 caracteres
        @NotBlank
        @Size(min = 2, max = 100)
        String nome
) {}
