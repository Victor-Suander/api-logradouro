package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.LogradouroRequestDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogradouroService {

    LogradouroResponseDTO salvar(LogradouroRequestDTO dto);

    Page<LogradouroResponseDTO> listar(Pageable pageable);

    LogradouroResponseDTO buscarPorId(Long id);

    LogradouroResponseDTO buscarPorCep(String cep);

    LogradouroResponseDTO atualizar(Long id, LogradouroRequestDTO dto);

    void deletar(Long id);
}
