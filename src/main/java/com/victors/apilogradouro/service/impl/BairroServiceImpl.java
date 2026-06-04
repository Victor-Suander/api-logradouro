package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.entity.Bairro;
import com.victors.apilogradouro.entity.Cidade;
import com.victors.apilogradouro.exception.MensagensErro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.LogradouroRepository;
import com.victors.apilogradouro.service.BairroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// TODO: criar testes unitários para este service

@Service
@RequiredArgsConstructor
public class BairroServiceImpl implements BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final LogradouroRepository logradouroRepository;

    @Override
    public BairroResponseDTO salvar(BairroRequestDTO dto) {
        if (bairroRepository.existsByNome(dto.nome())) {
            throw new RegraNegocioException(MensagensErro.BAIRRO_NOME_DUPLICADO);
        }

        Cidade cidade = buscarCidadeOuLancarErro(dto.cidadeId());

        Bairro bairro = Bairro.builder()
                .nome(dto.nome())
                .cidade(cidade)
                .build();

        return toResponseDTO(bairroRepository.save(bairro));
    }

    @Override
    public Page<BairroResponseDTO> listar(Pageable pageable) {
        return bairroRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    public BairroResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarOuLancarErro(id));
    }

    @Override
    public BairroResponseDTO atualizar(Long id, BairroRequestDTO dto) {
        Bairro bairro = buscarOuLancarErro(id);

        // exclui o próprio id da consulta — sem isso, editar sem mudar o nome lançaria falso erro de duplicidade
        if (bairroRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new RegraNegocioException(MensagensErro.BAIRRO_NOME_DUPLICADO);
        }

        bairro.setNome(dto.nome());
        bairro.setCidade(buscarCidadeOuLancarErro(dto.cidadeId()));

        return toResponseDTO(bairroRepository.save(bairro));
    }

    @Override
    public void deletar(Long id) {
        buscarOuLancarErro(id);
        // impede exclusão de bairro com logradouros vinculados — evitaria violação de FK no banco
        if (logradouroRepository.existsByBairroId(id)) {
            throw new RegraNegocioException(MensagensErro.BAIRRO_COM_LOGRADOUROS);
        }
        bairroRepository.deleteById(id);
    }

    // TODO: implementar listarLogradouros quando Logradouro for criado
    @Override
    public Page<Object> listarLogradouros(Long bairroId, Pageable pageable) {
        buscarOuLancarErro(bairroId);
        return Page.empty(pageable);
    }

    private Bairro buscarOuLancarErro(Long id) {
        return bairroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.BAIRRO_NAO_ENCONTRADO));
    }

    private Cidade buscarCidadeOuLancarErro(Long cidadeId) {
        return cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.CIDADE_NAO_ENCONTRADA));
    }

    private BairroResponseDTO toResponseDTO(Bairro bairro) {
        return new BairroResponseDTO(
                bairro.getId(),
                bairro.getNome(),
                bairro.getCidade().getId(),
                bairro.getCidade().getNome()
        );
    }
}
