package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.EstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepo, ProdutoRepository produtoRepo) {
        this.estoqueRepository = estoqueRepo;
        this.produtoRepository = produtoRepo;
    }

    public EstoqueDTO create(EstoqueDTO dto) {
        Estoque estoque = dto.toEntity();
        estoqueRepository.save(estoque);
        return EstoqueDTO.fromEntity(estoque);
    }

    public EstoqueDTO getById(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        return EstoqueDTO.fromEntity(estoque);
    }

    public List<EstoqueDTO> getAll() {
        return estoqueRepository.findAll().stream()
                .map(EstoqueDTO::fromEntity)
                .toList();
    }

    public EstoqueDTO update(Long id, EstoqueDTO dto) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        estoque.setNome(dto.getNome());
        estoque.setDescricao(dto.getDescricao());

        estoqueRepository.save(estoque);

        return EstoqueDTO.fromEntity(estoque);
    }

    public void delete(Long id) {
        if (!estoqueRepository.existsById(id))
            throw new ResourceNotFoundException("Estoque não encontrado");

        estoqueRepository.deleteById(id);
    }

    public ValorTotalEstoqueDTO calcularValorTotal(Long estoqueId) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        if (estoque.getProdutos() == null || estoque.getProdutos().isEmpty()) {
            return new ValorTotalEstoqueDTO(0.0, 0);
        }

        double valorTotal = estoque.getProdutos().stream()
                .mapToDouble(p -> p.getPrecoUnitario() * p.getQuantidade())
                .sum();

        int quantidadeTotal = estoque.getProdutos().stream()
                .mapToInt(p -> p.getQuantidade())
                .sum();

        return new ValorTotalEstoqueDTO(valorTotal, quantidadeTotal);
    }

    public List<ProdutoDTO> listarProdutosDoEstoque(Long estoqueId) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        List<Produto> produtos = produtoRepository.findByEstoqueId(estoqueId);

        return produtos.stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }
}
