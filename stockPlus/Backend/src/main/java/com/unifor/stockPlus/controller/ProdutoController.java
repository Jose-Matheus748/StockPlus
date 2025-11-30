package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.Usuario;
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

    public ProdutoController(ProdutoService produtoService, UsuarioService usuarioService) {
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> create(@RequestBody ProdutoDTO dto, @RequestParam Long usuarioId) {
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

    @PostMapping("/{id}/add")
    public ProdutoDTO add(@PathVariable Long id, @RequestParam int quantidade) {
        return produtoService.addQuantity(id, quantidade);
    }

    @PostMapping("/{id}/remove")
    public ProdutoDTO remove(@PathVariable Long id, @RequestParam int quantidade) {
        return produtoService.removeQuantity(id, quantidade);
    }

    @GetMapping("/valor-total")
    public Double getValorTotalEstoque() {
        return produtoService.calcularValorTotalEstoque();
    }

    @GetMapping("/estoque/{estoqueId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorEstoque(@PathVariable Long estoqueId) {
        List<ProdutoDTO> produtos = produtoService.listarPorEstoque(estoqueId);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/meus-produtos")
    public List<ProdutoDTO> listarMeusProdutos(@RequestParam Long usuarioId) {
        return produtoService.listarPorUsuario(usuarioId);
    }
}
