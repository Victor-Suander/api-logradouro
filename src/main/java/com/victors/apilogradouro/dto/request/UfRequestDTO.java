package com.victors.apilogradouro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// TODO: criar DTOs das outras entidades seguindo este padrão
// as anotações abaixo só são ativadas se o parâmetro no controller receber @Valid

public record UfRequestDTO(

        // rejeita null, vazio e espaços em branco; exige exatamente 2 caracteres
        @NotBlank
        @Size(min = 2, max = 2)
        String sigla,

        // rejeita null e vazio; entre 2 e 100 caracteres
        @NotBlank
        @Size(min = 2, max = 100)
        String nome
) {}
