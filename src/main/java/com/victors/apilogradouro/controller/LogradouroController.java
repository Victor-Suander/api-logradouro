package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.LogradouroRequestDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import com.victors.apilogradouro.service.LogradouroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logradouros")
@RequiredArgsConstructor
public class LogradouroController {

    private final LogradouroService logradouroService;

    @PostMapping
    public ResponseEntity<LogradouroResponseDTO> salvar(@Valid @RequestBody LogradouroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logradouroService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<LogradouroResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(logradouroService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogradouroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(logradouroService.buscarPorId(id));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<LogradouroResponseDTO> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(logradouroService.buscarPorCep(cep));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogradouroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LogradouroRequestDTO dto) {
        return ResponseEntity.ok(logradouroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logradouroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
