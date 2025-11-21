package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.service.EstoqueService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public EstoqueDTO create(@RequestBody EstoqueDTO dto) {
        return estoqueService.create(dto);
    }

    @GetMapping
    public List<EstoqueDTO> getAll() {
        return estoqueService.getAll();
    }

    @GetMapping("/{id}")
    public EstoqueDTO get(@PathVariable Long id) {
        return estoqueService.getById(id);
    }

    @GetMapping("/{id}/produtos")
    public List<ProdutoDTO> listarProdutos(@PathVariable Long id) {
        return estoqueService.listarProdutosDoEstoque(id);
    }

    @PutMapping("/{id}")
    public EstoqueDTO update(@PathVariable Long id, @RequestBody EstoqueDTO dto) {
        return estoqueService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        estoqueService.delete(id);
    }

    @GetMapping("/{id}/valor-total")
    public ValorTotalEstoqueDTO calcularValorTotal(@PathVariable Long id) {
        return estoqueService.calcularValorTotal(id);
    }
}
