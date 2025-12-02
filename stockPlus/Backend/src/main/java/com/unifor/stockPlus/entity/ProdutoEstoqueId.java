package com.unifor.stockPlus.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProdutoEstoqueId implements Serializable {
    private Long produtoId;
    private Long estoqueId;

    // getters/setters gerados pelo Lombok se você usar @Data; aqui usamos padrão lombok annotations acima.
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Long getEstoqueId() { return estoqueId; }
    public void setEstoqueId(Long estoqueId) { this.estoqueId = estoqueId; }
}
