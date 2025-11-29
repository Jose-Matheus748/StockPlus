package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    // NOVO: Listar estoques por usuário
    List<Estoque> findByUsuarioId(Long usuarioId);

    // NOVO: Verificar se um estoque pertence a um usuário
    boolean existsByIdAndUsuarioId(Long estoqueId, Long usuarioId);
}
