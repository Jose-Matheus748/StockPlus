package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Estoque;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EstoqueDTO {

    private Long id;
    private String nome;
    private String descricao;

    // ---------- Entity -> DTO ----------
    public static EstoqueDTO fromEntity(Estoque estoque) {
        if (estoque == null) return null;

        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setNome(estoque.getNome());
        dto.setDescricao(estoque.getDescricao());
        return dto;
    }

    // ---------- DTO -> Entity ----------
    public Estoque toEntity() {
        Estoque estoque = new Estoque();
        estoque.setId(this.id);
        estoque.setNome(this.nome);
        estoque.setDescricao(this.descricao);
        return estoque;
    }
}
