package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    List<Estoque> findByUsuarioId(Long usuarioId);

    boolean existsByIdAndUsuarioId(Long estoqueId, Long usuarioId);
}
