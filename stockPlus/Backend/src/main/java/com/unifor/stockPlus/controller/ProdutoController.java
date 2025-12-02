package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ProdutoEmEstoqueDTO;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.service.ProdutoEstoqueService;
import com.unifor.stockPlus.service.ProdutoService;
import com.unifor.stockPlus.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final ProdutoEstoqueService produtoEstoqueService;

    public ProdutoController(ProdutoService produtoService,
                             UsuarioService usuarioService,
                             ProdutoEstoqueService produtoEstoqueService) {
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.produtoEstoqueService = produtoEstoqueService;
    }

    // Criar produto
    @PostMapping
    public ResponseEntity<ProdutoDTO> create(
            @RequestBody ProdutoDTO dto,
            @RequestParam Long usuarioId) {

        Usuario usuario = usuarioService.getEntityById(usuarioId);
        ProdutoDTO novo = produtoService.create(dto, usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @GetMapping
    public List<ProdutoDTO> getAll() {
        return produtoService.getAll();
    }

    @GetMapping("/{id}")
    public ProdutoDTO get(@PathVariable Long id) {
        return produtoService.getById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        return produtoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        produtoService.delete(id);
    }

    // ===========================
    // PRODUTO ↔ ESTOQUE
    // ===========================

    // Adicionar produto ao estoque
    @PostMapping("/{produtoId}/adicionar")
    public ProdutoEmEstoqueDTO adicionarEstoque(
            @PathVariable Long produtoId,
            @RequestParam Long estoqueId,
            @RequestParam int quantidade
    ) {
        return toDTO(
                produtoEstoqueService.adicionarProdutoAoEstoque(produtoId, estoqueId, quantidade)
        );
    }

    // Atualizar quantidade
    @PutMapping("/{produtoId}/atualizar")
    public ProdutoEmEstoqueDTO atualizarQuantidade(
            @PathVariable Long produtoId,
            @RequestParam Long estoqueId,
            @RequestParam int quantidade
    ) {
        return toDTO(
                produtoEstoqueService.atualizarQuantidade(produtoId, estoqueId, quantidade)
        );
    }

    // Remover produto do estoque
    @DeleteMapping("/{produtoId}/remover")
    public ResponseEntity<Void> remover(
            @PathVariable Long produtoId,
            @RequestParam Long estoqueId
    ) {
        produtoEstoqueService.removerProdutoDoEstoque(produtoId, estoqueId);
        return ResponseEntity.noContent().build();
    }

    // Listar estoques onde o produto existe
    @GetMapping("/{produtoId}/estoques")
    public List<ProdutoEmEstoqueDTO> listarEstoquesDeUmProduto(
            @PathVariable Long produtoId
    ) {
        return produtoEstoqueService.listarEstoquesPorProduto(produtoId);
    }

    // Converter entidade para DTO
    private ProdutoEmEstoqueDTO toDTO(Object peObj) {
        return (ProdutoEmEstoqueDTO) peObj;
    }
}
