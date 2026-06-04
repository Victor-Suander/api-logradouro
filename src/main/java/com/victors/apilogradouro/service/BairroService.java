package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BairroService {

    BairroResponseDTO salvar(BairroRequestDTO dto);

    Page<BairroResponseDTO> listar(Pageable pageable);

    BairroResponseDTO buscarPorId(Long id);

    BairroResponseDTO atualizar(Long id, BairroRequestDTO dto);

    void deletar(Long id);

    // retorno será Page<LogradouroResponseDTO> quando Logradouro for criado
    Page<Object> listarLogradouros(Long bairroId, Pageable pageable);
}
