package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoEmEstoqueDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.service.EstoqueService;
import com.unifor.stockPlus.service.ProdutoEstoqueService;
import com.unifor.stockPlus.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final UsuarioService usuarioService;
    private final ProdutoEstoqueService produtoEstoqueService;

    @Autowired
    public EstoqueController(EstoqueService estoqueService,
                             UsuarioService usuarioService,
                             ProdutoEstoqueService produtoEstoqueService) {
        this.estoqueService = estoqueService;
        this.usuarioService = usuarioService;
        this.produtoEstoqueService = produtoEstoqueService;
    }

    // Criar estoque
    @PostMapping
    public ResponseEntity<EstoqueDTO> create(
            @RequestBody EstoqueDTO dto,
            @RequestParam Long usuarioId
    ) {
        Usuario usuario = usuarioService.getEntityById(usuarioId);
        EstoqueDTO criado = estoqueService.create(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public List<EstoqueDTO> getAll() {
        return estoqueService.getAll();
    }

    @GetMapping("/{id}")
    public EstoqueDTO get(@PathVariable Long id) {
        return estoqueService.getById(id);
    }

    // Listar produtos dentro do estoque
    @GetMapping("/{id}/produtos")
    public List<ProdutoEmEstoqueDTO> listarProdutos(@PathVariable Long id) {
        return produtoEstoqueService.listarProdutosPorEstoque(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueDTO> update(
            @PathVariable Long id,
            @RequestBody EstoqueDTO dto,
            @RequestParam Long usuarioId
    ) {
        Usuario usuario = usuarioService.getEntityById(usuarioId);
        EstoqueDTO atualizado = estoqueService.update(id, dto, usuario);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        Usuario usuario = usuarioService.getEntityById(usuarioId);
        estoqueService.delete(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/valor-total")
    public ValorTotalEstoqueDTO calcularValorTotal(@PathVariable Long id) {
        return estoqueService.calcularValorTotal(id);
    }

    @GetMapping("/meus-estoques")
    public ResponseEntity<List<EstoqueDTO>> meusEstoques(@RequestParam Long usuarioId) {
        List<EstoqueDTO> estoques = estoqueService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(estoques);
    }
}
