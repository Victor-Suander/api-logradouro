package com.victors.apilogradouro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.service.BairroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BairroController.class)
class BairroValidacaoTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BairroService bairroService;

    @Test
    void deveRetornar400_quandoNomeVazio() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("nome", "", "cidadeId", 1));

        mockMvc.perform(post("/bairros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoCidadeIdAusente() throws Exception {
        // cidadeId ausente — @NotNull deve rejeitar
        String body = objectMapper.writeValueAsString(Collections.singletonMap("nome", "Centro"));

        mockMvc.perform(post("/bairros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar201_quandoDadosValidos() throws Exception {
        when(bairroService.salvar(any())).thenReturn(new BairroResponseDTO(1L, "Centro", 1L, "São Paulo", null, null));

        String body = objectMapper.writeValueAsString(Map.of("nome", "Centro", "cidadeId", 1));

        mockMvc.perform(post("/bairros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
