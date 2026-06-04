package com.victors.apilogradouro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CidadeRequestDTO(

        @NotBlank
        @Size(min = 2, max = 120)
        String nome,

        @NotNull(message = "UF é obrigatória")
        Long ufId // referencia o id da UF pai
) {}
