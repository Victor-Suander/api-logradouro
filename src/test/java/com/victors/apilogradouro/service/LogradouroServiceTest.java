package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.LogradouroRequestDTO;
import com.victors.apilogradouro.entity.TipoLogradouro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.LogradouroRepository;
import com.victors.apilogradouro.service.impl.LogradouroServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogradouroServiceTest {

    @Mock
    private LogradouroRepository logradouroRepository;

    @Mock
    private BairroRepository bairroRepository;

    @InjectMocks
    private LogradouroServiceImpl logradouroService;

    @Test
    void salvar_comCepDuplicado_lancaRegraNegocioException() {
        when(logradouroRepository.existsByCep("01310-100")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> logradouroService.salvar(
                        new LogradouroRequestDTO("Avenida Paulista", TipoLogradouro.AVENIDA, "01310-100", 1L)));
    }

    @Test
    void salvar_comBairroInexistente_lancaRecursoNaoEncontradoException() {
        when(logradouroRepository.existsByCep("01310-100")).thenReturn(false);
        when(bairroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> logradouroService.salvar(
                        new LogradouroRequestDTO("Avenida Paulista", TipoLogradouro.AVENIDA, "01310-100", 99L)));
    }

    @Test
    void buscarPorCep_comCepInexistente_lancaRecursoNaoEncontradoException() {
        when(logradouroRepository.findByCep("99999-999")).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> logradouroService.buscarPorCep("99999-999"));
    }
}
