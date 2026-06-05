package com.victors.apilogradouro.dto.response;

import java.time.LocalDateTime;

public record UfResponseDTO(
        Long id,
        String sigla,
        String nome,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
