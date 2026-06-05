package com.victors.apilogradouro.dto.response;

import java.time.LocalDateTime;

public record BairroResponseDTO(
        Long id,
        String nome,
        Long cidadeId,
        String cidadeNome,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
