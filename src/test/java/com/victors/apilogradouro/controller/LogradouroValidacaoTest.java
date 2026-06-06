package com.victors.apilogradouro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import com.victors.apilogradouro.entity.TipoLogradouro;
import com.victors.apilogradouro.service.LogradouroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogradouroController.class)
class LogradouroValidacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LogradouroService logradouroService;

    @Test
    void deveRetornar400_quandoNomeVazio() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("nome", "", "tipo", "RUA", "cep", "12345-678", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCepInvalido() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("nome", "Avenida Paulista", "tipo", "AVENIDA", "cep", "cep-invalido", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar201_quandoDadosValidos() throws Exception {
        when(logradouroService.salvar(any())).thenReturn(
                new LogradouroResponseDTO(1L, "Avenida Paulista", TipoLogradouro.AVENIDA,
                        "01310-100", 1L, "Bela Vista", "São Paulo", "SP", null, null));

        String body = objectMapper.writeValueAsString(
                Map.of("nome", "Avenida Paulista", "tipo", "AVENIDA", "cep", "01310-100", "bairroId", 1));

        mockMvc.perform(post("/logradouros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
