package com.victors.apilogradouro.repository;

import com.victors.apilogradouro.entity.Cidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    // usado no endpoint hierárquico para listar cidades de uma UF específica
    Page<Cidade> findByUfId(Long ufId, Pageable pageable);
}
