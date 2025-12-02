package com.unifor.stockPlus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "produto_estoque")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProdutoEstoque {

    @EmbeddedId
    private ProdutoEstoqueId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("estoqueId")
    @JoinColumn(name = "estoque_id")
    private Estoque estoque;

    @Column(nullable = false)
    private Integer quantidade = 0;

    public ProdutoEstoque(Produto produto, Estoque estoque, Integer quantidade) {
        this.id = new ProdutoEstoqueId(produto.getId(), estoque.getId());
        this.produto = produto;
        this.estoque = estoque;
        this.quantidade = quantidade != null ? quantidade : 0;
    }
}
