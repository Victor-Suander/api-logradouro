package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.service.UfService;
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

// TODO: adicionar anotações @Tag e @Operation do Swagger quando dependência for adicionada
// TODO: criar controller da Cidade seguindo este mesmo padrão

@RestController
@RequestMapping("/ufs")
@RequiredArgsConstructor
public class UfController {

    private final UfService ufService;

    @PostMapping
    public ResponseEntity<UfResponseDTO> salvar(
            @Valid @RequestBody UfRequestDTO dto) { // @Valid ativa as validações declaradas no UfRequestDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(ufService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<UfResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ufService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UfResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ufService.buscarPorId(id));
    }

    @GetMapping("/sigla/{sigla}")
    public ResponseEntity<UfResponseDTO> buscarPorSigla(@PathVariable String sigla) {
        return ResponseEntity.ok(ufService.buscarPorSigla(sigla));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UfResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UfRequestDTO dto) {
        return ResponseEntity.ok(ufService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ufService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
