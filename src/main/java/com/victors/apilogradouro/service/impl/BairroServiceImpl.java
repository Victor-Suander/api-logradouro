package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// TODO: criar testes unitários para este service

@Slf4j
@Service
@RequiredArgsConstructor
public class BairroServiceImpl implements BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final LogradouroRepository logradouroRepository;

    @Override
    public BairroResponseDTO salvar(BairroRequestDTO dto) {
        log.info("Salvando Bairro: nome={}", dto.nome());
        if (bairroRepository.existsByNome(dto.nome())) {
            log.warn("Nome de Bairro duplicado: {}", dto.nome());
            throw new RegraNegocioException(MensagensErro.BAIRRO_NOME_DUPLICADO);
        }

        Cidade cidade = buscarCidadeOuLancarErro(dto.cidadeId());

        Bairro bairro = Bairro.builder()
                .nome(dto.nome())
                .cidade(cidade)
                .build();

        BairroResponseDTO result = toResponseDTO(bairroRepository.save(bairro));
        log.info("Bairro salvo com sucesso: id={}", result.id());
        return result;
    }

    @Override
    public Page<BairroResponseDTO> listar(Pageable pageable) {
        log.debug("Listando Bairros: pageable={}", pageable);
        return bairroRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    public BairroResponseDTO buscarPorId(Long id) {
        log.debug("Buscando Bairro por id: {}", id);
        return toResponseDTO(buscarOuLancarErro(id));
    }

    @Override
    public BairroResponseDTO atualizar(Long id, BairroRequestDTO dto) {
        log.info("Atualizando Bairro: id={}", id);
        Bairro bairro = buscarOuLancarErro(id);

        // exclui o próprio id da consulta — sem isso, editar sem mudar o nome lançaria falso erro de duplicidade
        if (bairroRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            log.warn("Nome de Bairro duplicado na atualização: {}", dto.nome());
            throw new RegraNegocioException(MensagensErro.BAIRRO_NOME_DUPLICADO);
        }

        bairro.setNome(dto.nome());
        bairro.setCidade(buscarCidadeOuLancarErro(dto.cidadeId()));

        BairroResponseDTO result = toResponseDTO(bairroRepository.save(bairro));
        log.info("Bairro atualizado com sucesso: id={}", id);
        return result;
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando Bairro: id={}", id);
        buscarOuLancarErro(id);
        // impede exclusão de bairro com logradouros vinculados — evitaria violação de FK no banco
        if (logradouroRepository.existsByBairroId(id)) {
            log.warn("Tentativa de excluir Bairro com logradouros vinculados: id={}", id);
            throw new RegraNegocioException(MensagensErro.BAIRRO_COM_LOGRADOUROS);
        }
        bairroRepository.deleteById(id);
        log.info("Bairro deletado com sucesso: id={}", id);
    }

    @Override
    public Page<LogradouroResponseDTO> listarLogradouros(Long bairroId, Pageable pageable) {
        buscarOuLancarErro(bairroId);
        return logradouroRepository.findByBairroId(bairroId, pageable)
                .map(l -> new LogradouroResponseDTO(
                        l.getId(), l.getNome(), l.getTipo(), l.getCep(),
                        l.getBairro().getId(), l.getBairro().getNome(),
                        l.getBairro().getCidade().getNome(),
                        l.getBairro().getCidade().getUf().getSigla(),
                        l.getCreatedAt(), l.getUpdatedAt()));
    }

    private Bairro buscarOuLancarErro(Long id) {
        return bairroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Bairro não encontrado: id={}", id);
                    return new RecursoNaoEncontradoException(MensagensErro.BAIRRO_NAO_ENCONTRADO);
                });
    }

    private Cidade buscarCidadeOuLancarErro(Long cidadeId) {
        return cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> {
                    log.warn("Cidade não encontrada ao buscar para Bairro: id={}", cidadeId);
                    return new RecursoNaoEncontradoException(MensagensErro.CIDADE_NAO_ENCONTRADA);
                });
    }

    private BairroResponseDTO toResponseDTO(Bairro bairro) {
        return new BairroResponseDTO(
                bairro.getId(),
                bairro.getNome(),
                bairro.getCidade().getId(),
                bairro.getCidade().getNome(),
                bairro.getCreatedAt(),
                bairro.getUpdatedAt()
        );
    }
}
