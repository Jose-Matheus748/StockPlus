package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ProdutoEmEstoqueDTO;
import com.unifor.stockPlus.entity.ProdutoEstoque;
import com.unifor.stockPlus.service.ProdutoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto-estoque")
public class ProdutoEstoqueController {

    private final ProdutoEstoqueService service;

    public ProdutoEstoqueController(ProdutoEstoqueService service) {
        this.service = service;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<ProdutoEstoque> adicionar(
            @RequestParam Long produtoId,
            @RequestParam Long estoqueId,
            @RequestParam(defaultValue = "0") int quantidade
    ) {
        ProdutoEstoque pe = service.adicionarProdutoAoEstoque(produtoId, estoqueId, quantidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(pe);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ProdutoEstoque> atualizar(
            @RequestParam Long produtoId,
            @RequestParam Long estoqueId,
            @RequestParam int quantidade
    ) {
        ProdutoEstoque pe = service.atualizarQuantidade(produtoId, estoqueId, quantidade);
        return ResponseEntity.ok(pe);
    }

    @DeleteMapping
    public ResponseEntity<Void> remover(
            @RequestParam Long produtoId,
            @RequestParam Long estoqueId
    ) {
        service.removerProdutoDoEstoque(produtoId, estoqueId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estoque/{estoqueId}")
    public ResponseEntity<List<ProdutoEmEstoqueDTO>> listarPorEstoque(@PathVariable Long estoqueId) {
        List<ProdutoEmEstoqueDTO> list = service.listarProdutosPorEstoque(estoqueId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ProdutoEmEstoqueDTO>> listarEstoquesPorProduto(@PathVariable Long produtoId) {
        List<ProdutoEmEstoqueDTO> list = service.listarEstoquesPorProduto(produtoId);
        return ResponseEntity.ok(list);
    }
}
