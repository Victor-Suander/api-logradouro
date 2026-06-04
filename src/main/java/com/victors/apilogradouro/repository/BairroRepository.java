package com.victors.apilogradouro.repository;

import com.victors.apilogradouro.entity.Bairro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    // usado no endpoint hierárquico para listar bairros de uma cidade específica
    Page<Bairro> findByCidadeId(Long cidadeId, Pageable pageable);
}
