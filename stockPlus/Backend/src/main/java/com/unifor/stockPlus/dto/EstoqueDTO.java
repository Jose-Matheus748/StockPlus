package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Estoque;
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
    private Long usuarioId;  // NOVO
    private List<ProdutoDTO> produtos;

    public static EstoqueDTO fromEntity(Estoque estoque) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setNome(estoque.getNome());
        dto.setDescricao(estoque.getDescricao());
        dto.setUsuarioId(estoque.getUsuario() != null ? estoque.getUsuario().getId() : null);
        if (estoque.getProdutos() != null) {
            dto.setProdutos(estoque.getProdutos().stream()
                    .map(ProdutoDTO::fromEntity)
                    .toList());
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
