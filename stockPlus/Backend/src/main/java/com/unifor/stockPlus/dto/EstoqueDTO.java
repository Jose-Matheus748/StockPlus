package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.ProdutoEstoque;
import com.unifor.stockPlus.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long usuarioId;

    // Produtos dentro desse estoque, com quantidade no estoque
    private List<ProdutoEmEstoqueDTO> produtos;

    public static EstoqueDTO fromEntity(Estoque estoque) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setNome(estoque.getNome());
        dto.setDescricao(estoque.getDescricao());
        dto.setUsuarioId(
                estoque.getUsuario() != null ? estoque.getUsuario().getId() : null
        );

        // Converter ProdutoEstoque -> ProdutoEmEstoqueDTO
        if (estoque.getProdutoEstoques() != null) {
            dto.setProdutos(
                    estoque.getProdutoEstoques().stream()
                            .map(pe -> ProdutoEmEstoqueDTO.builder()
                                    .produtoId(pe.getProduto().getId())
                                    .nome(pe.getProduto().getNome())
                                    .descricao(pe.getProduto().getDescricao())
                                    .fornecedor(pe.getProduto().getFornecedor())
                                    .marca(pe.getProduto().getMarca())
                                    .precoUnitario(pe.getProduto().getPrecoUnitario())
                                    .quantidadeNoEstoque(pe.getQuantidade())
                                    .usuarioId(
                                            pe.getProduto().getUsuario() != null ?
                                                    pe.getProduto().getUsuario().getId() : null
                                    )
                                    .estoqueId(estoque.getId())
                                    .nomeEstoque(estoque.getNome())
                                    .build()
                            )
                            .toList()
            );
        }

        return dto;
    }

    public Estoque toEntity(Usuario usuario) {
        Estoque estoque = new Estoque();
        estoque.setId(this.id);
        estoque.setNome(this.nome);
        estoque.setDescricao(this.descricao);
        estoque.setUsuario(usuario);
        return estoque;
    }
}
