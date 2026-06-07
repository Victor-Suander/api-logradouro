package com.victors.apilogradouro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.service.LogradouroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// usa LogradouroController pois tem campo enum (TipoLogradouro) e todas as validações necessárias
@WebMvcTest(LogradouroController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LogradouroService logradouroService;

    @Test
    void regraNegocioException_retorna400_comCampoMensagem() throws Exception {
        when(logradouroService.salvar(any()))
                .thenThrow(new RegraNegocioException("CEP já cadastrado."));

        String body = objectMapper.writeValueAsString(
                Map.of("nome", "Avenida Paulista", "tipo", "AVENIDA", "cep", "01310-100", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("CEP já cadastrado."));
    }

    @Test
    void recursoNaoEncontradoException_retorna404_comCampoMensagem() throws Exception {
        when(logradouroService.buscarPorId(1L))
                .thenThrow(new RecursoNaoEncontradoException("Logradouro não encontrado."));

        mockMvc.perform(get("/logradouros/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Logradouro não encontrado."));
    }

    @Test
    void validacaoCampoInvalido_retorna400() throws Exception {
        // nome vazio — @NotBlank rejeita antes de chegar no service
        String body = objectMapper.writeValueAsString(
                Map.of("nome", "", "tipo", "AVENIDA", "cep", "01310-100", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void enumInvalido_retorna400() throws Exception {
        // valor fora do enum — Jackson lança HttpMessageNotReadableException → 400
        String body = objectMapper.writeValueAsString(
                Map.of("nome", "Avenida Paulista", "tipo", "TIPO_INVALIDO", "cep", "01310-100", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
