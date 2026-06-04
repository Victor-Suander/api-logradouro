package com.victors.apilogradouro.repository;

import com.victors.apilogradouro.entity.Logradouro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogradouroRepository extends JpaRepository<Logradouro, Long> {

    boolean existsByCep(String cep);

    boolean existsByCepAndIdNot(String cep, Long id);

    boolean existsByBairroId(Long bairroId);

    Page<Logradouro> findByBairroId(Long bairroId, Pageable pageable);

    Optional<Logradouro> findByCep(String cep);
}
