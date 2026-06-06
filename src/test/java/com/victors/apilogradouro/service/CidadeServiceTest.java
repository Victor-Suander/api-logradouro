package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.CidadeRequestDTO;
import com.victors.apilogradouro.entity.Cidade;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.UfRepository;
import com.victors.apilogradouro.service.impl.CidadeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CidadeServiceTest {

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private UfRepository ufRepository;

    @Mock
    private BairroRepository bairroRepository;

    @InjectMocks
    private CidadeServiceImpl cidadeService;

    @Test
    void salvar_comNomeDuplicado_lancaRegraNegocioException() {
        when(cidadeRepository.existsByNome("São Paulo")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> cidadeService.salvar(new CidadeRequestDTO("São Paulo", 1L)));
    }

    @Test
    void salvar_comUfInexistente_lancaRecursoNaoEncontradoException() {
        when(cidadeRepository.existsByNome("São Paulo")).thenReturn(false);
        when(ufRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> cidadeService.salvar(new CidadeRequestDTO("São Paulo", 99L)));
    }

    @Test
    void deletar_comBairrosVinculados_lancaRegraNegocioException() {
        Uf uf = Uf.builder().id(1L).sigla("SP").nome("São Paulo").build();
        Cidade cidade = Cidade.builder().id(1L).nome("São Paulo").uf(uf).build();
        when(cidadeRepository.findById(1L)).thenReturn(Optional.of(cidade));
        when(bairroRepository.existsByCidadeId(1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> cidadeService.deletar(1L));
    }
}
