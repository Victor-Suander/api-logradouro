package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.LogradouroRequestDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import com.victors.apilogradouro.entity.Bairro;
import com.victors.apilogradouro.entity.Logradouro;
import com.victors.apilogradouro.exception.MensagensErro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.LogradouroRepository;
import com.victors.apilogradouro.service.LogradouroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// TODO: criar testes unitários para este service

@Service
@RequiredArgsConstructor
public class LogradouroServiceImpl implements LogradouroService {

    private final LogradouroRepository logradouroRepository;
    private final BairroRepository bairroRepository;

    @Override
    public LogradouroResponseDTO salvar(LogradouroRequestDTO dto) {
        if (logradouroRepository.existsByCep(dto.cep())) {
            throw new RegraNegocioException(MensagensErro.LOGRADOURO_CEP_DUPLICADO);
        }

        Bairro bairro = buscarBairroOuLancarErro(dto.bairroId());

        Logradouro logradouro = Logradouro.builder()
                .nome(dto.nome())
                .tipo(dto.tipo())
                .cep(dto.cep())
                .bairro(bairro)
                .build();

        return toResponseDTO(logradouroRepository.save(logradouro));
    }

    @Override
    public Page<LogradouroResponseDTO> listar(Pageable pageable) {
        return logradouroRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    public LogradouroResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarOuLancarErro(id));
    }

    @Override
    public LogradouroResponseDTO buscarPorCep(String cep) {
        return toResponseDTO(logradouroRepository.findByCep(cep)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.LOGRADOURO_NAO_ENCONTRADO)));
    }

    @Override
    public LogradouroResponseDTO atualizar(Long id, LogradouroRequestDTO dto) {
        Logradouro logradouro = buscarOuLancarErro(id);

        // exclui o próprio id da consulta — sem isso, editar sem mudar o CEP lançaria falso erro de duplicidade
        if (logradouroRepository.existsByCepAndIdNot(dto.cep(), id)) {
            throw new RegraNegocioException(MensagensErro.LOGRADOURO_CEP_DUPLICADO);
        }

        logradouro.setNome(dto.nome());
        logradouro.setTipo(dto.tipo());
        logradouro.setCep(dto.cep());
        logradouro.setBairro(buscarBairroOuLancarErro(dto.bairroId()));

        return toResponseDTO(logradouroRepository.save(logradouro));
    }

    @Override
    public void deletar(Long id) {
        buscarOuLancarErro(id);
        logradouroRepository.deleteById(id);
    }

    private Logradouro buscarOuLancarErro(Long id) {
        return logradouroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.LOGRADOURO_NAO_ENCONTRADO));
    }

    private Bairro buscarBairroOuLancarErro(Long bairroId) {
        return bairroRepository.findById(bairroId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.BAIRRO_NAO_ENCONTRADO));
    }

    private LogradouroResponseDTO toResponseDTO(Logradouro logradouro) {
        Bairro bairro = logradouro.getBairro();
        return new LogradouroResponseDTO(
                logradouro.getId(),
                logradouro.getNome(),
                logradouro.getTipo(),
                logradouro.getCep(),
                bairro.getId(),
                bairro.getNome(),
                bairro.getCidade().getNome(),
                bairro.getCidade().getUf().getSigla(),
                logradouro.getCreatedAt(),
                logradouro.getUpdatedAt()
        );
    }
}
