package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Produto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String fornecedor;
    private String marca;
    private Double precoUnitario;
    private Long usuarioId;

    // NOVO ➜ quantidade específica desse produto em UM estoque
    private int quantidadeNoEstoque;

    public static ProdutoDTO fromEntity(Produto produto) {
        if (produto == null) return null;

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setFornecedor(produto.getFornecedor());
        dto.setMarca(produto.getMarca());
        dto.setPrecoUnitario(produto.getPrecoUnitario());
        dto.setUsuarioId(produto.getUsuario() != null ? produto.getUsuario().getId() : null);

        // quantidadeNoEstoque NÃO É DEFINIDA AQUI
        // porque depende da tabela ProdutoEstoque
        return dto;
    }

    public Produto toEntity() {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setFornecedor(this.fornecedor);
        produto.setMarca(this.marca);
        produto.setPrecoUnitario(this.precoUnitario);
        return produto;
    }
}
