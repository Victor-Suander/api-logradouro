package com.victors.apilogradouro.dto.response;

import com.victors.apilogradouro.entity.TipoLogradouro;

// TODO: adicionar campos createdAt e updatedAt quando auditoria for implementada

public record LogradouroResponseDTO(
        Long id,
        String nome,
        TipoLogradouro tipo,
        String cep,
        Long bairroId,
        String bairroNome,
        String cidadeNome,
        String ufSigla
) {}
