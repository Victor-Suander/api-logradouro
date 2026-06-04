package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.service.BairroService;
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
@RequestMapping("/bairros")
@RequiredArgsConstructor
public class BairroController {

    private final BairroService bairroService;

    @PostMapping
    public ResponseEntity<BairroResponseDTO> salvar(@Valid @RequestBody BairroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bairroService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<BairroResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bairroService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bairroService.buscarPorId(id));
    }

    // endpoint hierárquico — retorna os logradouros pertencentes a um bairro específico
    @GetMapping("/{id}/logradouros")
    public ResponseEntity<Page<Object>> listarLogradouros(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bairroService.listarLogradouros(id, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BairroRequestDTO dto) {
        return ResponseEntity.ok(bairroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bairroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
