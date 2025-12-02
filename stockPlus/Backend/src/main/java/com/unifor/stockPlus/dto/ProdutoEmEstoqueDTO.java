package com.unifor.stockPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEmEstoqueDTO {

    private Long produtoId;
    private String nome;
    private String descricao;
    private String fornecedor;
    private String marca;
    private Double precoUnitario;

    private Integer quantidadeNoEstoque; // quantidade desse produto neste estoque

    private Long usuarioId;

    // Novos campos necessários para a relação N:N
    private Long estoqueId;
    private String nomeEstoque;
}
