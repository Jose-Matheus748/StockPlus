package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.EstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepo, EstoqueRepository estoqueRepo) {
        this.produtoRepository = produtoRepo;
        this.estoqueRepository = estoqueRepo;
    }

    public ProdutoDTO create(ProdutoDTO dto) {
        Estoque estoque = estoqueRepository.findById(dto.getEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        Produto produto = dto.toEntity(estoque);
        produtoRepository.save(produto);

        return ProdutoDTO.fromEntity(produto);
    }

    public List<ProdutoDTO> getByEstoque(Long estoqueId) {
        List<Produto> produtos = produtoRepository.findByEstoqueId(estoqueId);

        if (produtos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado para este estoque.");
        }

        return produtos.stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public ProdutoDTO getById(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return ProdutoDTO.fromEntity(produto);
    }

    public List<ProdutoDTO> getAll() {
        return produtoRepository.findAll().stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public ProdutoDTO update(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Estoque estoque = estoqueRepository.findById(dto.getEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setFornecedor(dto.getFornecedor()); // NOVO
        produto.setMarca(dto.getMarca()); // NOVO
        produto.setQuantidade(dto.getQuantidade());
        produto.setPrecoUnitario(dto.getPrecoUnitario());
        produto.setEstoque(estoque);

        produtoRepository.save(produto);

        return ProdutoDTO.fromEntity(produto);
    }

    public void delete(Long id) {
        if (!produtoRepository.existsById(id))
            throw new ResourceNotFoundException("Produto não encontrado");

        produtoRepository.deleteById(id);
    }

    public ProdutoDTO addQuantity(Long id, int quantity) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        p.setQuantidade(p.getQuantidade() + quantity);
        produtoRepository.save(p);

        return ProdutoDTO.fromEntity(p);
    }

    public ProdutoDTO removeQuantity(Long id, int quantity) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade removida deve ser maior que zero.");
        }

        if (p.getQuantidade() < quantity) {
            throw new IllegalArgumentException("Não há quantidade suficiente no estoque para remover.");
        }

        p.setQuantidade(p.getQuantidade() - quantity);
        produtoRepository.save(p);

        return ProdutoDTO.fromEntity(p);
    }

    public Double calcularValorTotalEstoque() {
        return produtoRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getPrecoUnitario() * p.getQuantidade())
                .sum();
    }


}
