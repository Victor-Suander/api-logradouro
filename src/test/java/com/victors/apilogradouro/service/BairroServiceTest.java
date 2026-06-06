package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.entity.Bairro;
import com.victors.apilogradouro.entity.Cidade;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.LogradouroRepository;
import com.victors.apilogradouro.service.impl.BairroServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BairroServiceTest {

    @Mock
    private BairroRepository bairroRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private LogradouroRepository logradouroRepository;

    @InjectMocks
    private BairroServiceImpl bairroService;

    @Test
    void salvar_comNomeDuplicado_lancaRegraNegocioException() {
        when(bairroRepository.existsByNome("Centro")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> bairroService.salvar(new BairroRequestDTO("Centro", 1L)));
    }

    @Test
    void salvar_comCidadeInexistente_lancaRecursoNaoEncontradoException() {
        when(bairroRepository.existsByNome("Centro")).thenReturn(false);
        when(cidadeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> bairroService.salvar(new BairroRequestDTO("Centro", 99L)));
    }

    @Test
    void deletar_comLogradourosVinculados_lancaRegraNegocioException() {
        Uf uf = Uf.builder().id(1L).sigla("SP").nome("São Paulo").build();
        Cidade cidade = Cidade.builder().id(1L).nome("São Paulo").uf(uf).build();
        Bairro bairro = Bairro.builder().id(1L).nome("Centro").cidade(cidade).build();
        when(bairroRepository.findById(1L)).thenReturn(Optional.of(bairro));
        when(logradouroRepository.existsByBairroId(1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> bairroService.deletar(1L));
    }
}
