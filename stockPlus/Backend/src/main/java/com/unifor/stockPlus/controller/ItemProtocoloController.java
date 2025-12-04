package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ItemProtocoloDTO;
import com.unifor.stockPlus.entity.ItemProtocolo;
import com.unifor.stockPlus.service.ItemProtocoloService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/protocolos/itens")
@RequiredArgsConstructor
public class ItemProtocoloController {

    private final ItemProtocoloService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemProtocoloDTO> buscar(@PathVariable Long id) {
        ItemProtocolo item = itemService.buscar(id);
        return ResponseEntity.ok(toDTO(item));
    }

    @PostMapping
    public ResponseEntity<ItemProtocoloDTO> criar(@RequestBody CriarItemRequest request) {
        ItemProtocolo item = itemService.criar(
                request.getProtocoloId(),
                request.getProdutoId(),
                request.getQuantidade()
        );
        return ResponseEntity.status(201).body(toDTO(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        itemService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Setter @Getter
    public static class CriarItemRequest {
        private Long protocoloId;
        private Long produtoId;
        private Integer quantidade;
    }

    private ItemProtocoloDTO toDTO(ItemProtocolo item) {
        ItemProtocoloDTO dto = new ItemProtocoloDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoNome(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorItem(item.getQuantidade() * item.getProduto().getPrecoUnitario());
        return dto;
    }
}
