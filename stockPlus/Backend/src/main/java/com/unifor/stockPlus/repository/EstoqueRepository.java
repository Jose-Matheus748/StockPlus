package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
