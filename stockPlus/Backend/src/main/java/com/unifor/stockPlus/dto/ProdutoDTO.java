package com.unifor.stockPlus.dto;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Produto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private Double precoUnitario;
    private Long estoqueId;

    // -------- Entity -> DTO --------
    public static ProdutoDTO fromEntity(Produto produto) {
        if (produto == null) return null;

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setQuantidade(produto.getQuantidade());
        dto.setPrecoUnitario(produto.getPrecoUnitario());
        
        if (produto.getEstoque() != null) {
            dto.setEstoqueId(produto.getEstoque().getId());
        }

        return dto;
    }

    // -------- DTO -> Entity --------
    public Produto toEntity(Estoque estoque) {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setQuantidade(this.quantidade);
        produto.setPrecoUnitario(this.precoUnitario);
        produto.setEstoque(estoque);
        return produto;
    }
}
