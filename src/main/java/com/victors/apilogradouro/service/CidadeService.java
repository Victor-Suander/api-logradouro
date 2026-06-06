package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.CidadeRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CidadeService {

    CidadeResponseDTO salvar(CidadeRequestDTO dto);

    Page<CidadeResponseDTO> listar(Pageable pageable);

    CidadeResponseDTO buscarPorId(Long id);

    Page<BairroResponseDTO> listarBairros(Long cidadeId, Pageable pageable);

    CidadeResponseDTO atualizar(Long id, CidadeRequestDTO dto);

    void deletar(Long id);
}
