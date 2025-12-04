package com.unifor.stockPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtocoloDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Double valorTotal;
    private List<ItemProtocoloDTO> itens;
    private Long usuarioId;
}
