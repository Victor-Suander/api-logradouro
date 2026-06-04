package com.victors.apilogradouro.dto.response;

// TODO: adicionar campos createdAt e updatedAt quando auditoria for implementada

public record BairroResponseDTO(
        Long id,
        String nome,
        Long cidadeId,
        String cidadeNome
) {}
