package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.ProdutoEstoque;
import com.unifor.stockPlus.entity.ProdutoEstoqueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, ProdutoEstoqueId> {

    @Query("SELECT pe FROM ProdutoEstoque pe JOIN FETCH pe.produto p WHERE pe.estoque.id = :estoqueId")
    List<ProdutoEstoque> findByEstoqueId(@Param("estoqueId") Long estoqueId);

    @Query("SELECT pe FROM ProdutoEstoque pe JOIN FETCH pe.estoque e WHERE pe.produto.id = :produtoId")
    List<ProdutoEstoque> findByProdutoId(@Param("produtoId") Long produtoId);

    // Remove todas associações de determinado estoque
    void deleteByEstoqueId(Long estoqueId);

    // Optional: verificar existência de associação
    boolean existsById(ProdutoEstoqueId id);
}
