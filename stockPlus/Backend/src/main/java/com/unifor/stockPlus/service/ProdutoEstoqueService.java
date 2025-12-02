package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ProdutoEmEstoqueDTO;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.ProdutoEstoque;
import com.unifor.stockPlus.entity.ProdutoEstoqueId;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.EstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoEstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProdutoEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoEstoqueRepository produtoEstoqueRepository;

    public ProdutoEstoqueService(ProdutoRepository produtoRepository,
                                 EstoqueRepository estoqueRepository,
                                 ProdutoEstoqueRepository produtoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.produtoEstoqueRepository = produtoEstoqueRepository;
    }

    // ============================================================
    // ADICIONAR PRODUTO AO ESTOQUE
    // ============================================================
    public ProdutoEstoque adicionarProdutoAoEstoque(Long produtoId, Long estoqueId, int quantidade) {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        ProdutoEstoqueId id = new ProdutoEstoqueId(produtoId, estoqueId);

        ProdutoEstoque existente = produtoEstoqueRepository.findById(id).orElse(null);

        if (existente == null) {
            ProdutoEstoque novo = new ProdutoEstoque();
            novo.setId(id);
            novo.setProduto(produto);
            novo.setEstoque(estoque);
            novo.setQuantidade(Math.max(0, quantidade));
            return produtoEstoqueRepository.save(novo);
        }

        existente.setQuantidade(Math.max(0, existente.getQuantidade() + quantidade));
        return produtoEstoqueRepository.save(existente);
    }

    // ============================================================
    // ATUALIZAR QUANTIDADE
    // ============================================================
    public ProdutoEstoque atualizarQuantidade(Long produtoId, Long estoqueId, int quantidade) {

        ProdutoEstoqueId id = new ProdutoEstoqueId(produtoId, estoqueId);

        ProdutoEstoque pe = produtoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Associação produto-estoque não encontrada"));

        pe.setQuantidade(Math.max(0, quantidade));
        return produtoEstoqueRepository.save(pe);
    }

    // ============================================================
    // REMOVER PRODUTO DO ESTOQUE
    // ============================================================
    public void removerProdutoDoEstoque(Long produtoId, Long estoqueId) {
        ProdutoEstoqueId id = new ProdutoEstoqueId(produtoId, estoqueId);

        if (!produtoEstoqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação produto-estoque não encontrada");
        }

        produtoEstoqueRepository.deleteById(id);
    }

    // ============================================================
    // LISTAR PRODUTOS EM UM ESTOQUE
    // ============================================================
    public List<ProdutoEmEstoqueDTO> listarProdutosPorEstoque(Long estoqueId) {

        if (!estoqueRepository.existsById(estoqueId)) {
            throw new ResourceNotFoundException("Estoque não encontrado");
        }

        return produtoEstoqueRepository.findByEstoqueId(estoqueId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ============================================================
    // LISTAR ESTOQUES ONDE UM PRODUTO EXISTE
    // ============================================================
    public List<ProdutoEmEstoqueDTO> listarEstoquesPorProduto(Long produtoId) {

        if (!produtoRepository.existsById(produtoId)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        return produtoEstoqueRepository.findByProdutoId(produtoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ============================================================
    // REMOVER TODAS ASSOCIAÇÕES AO DELETAR ESTOQUE
    // ============================================================
    public void limparAssociacoesAoDeletarEstoque(Long estoqueId) {
        if (!estoqueRepository.existsById(estoqueId)) {
            throw new ResourceNotFoundException("Estoque não encontrado");
        }

        produtoEstoqueRepository.deleteByEstoqueId(estoqueId);
    }

    // ============================================================
    // CONVERTER ProdutoEstoque PARA DTO
    // ============================================================
    private ProdutoEmEstoqueDTO toDTO(ProdutoEstoque pe) {

        Produto p = pe.getProduto();
        Estoque e = pe.getEstoque();

        ProdutoEmEstoqueDTO dto = new ProdutoEmEstoqueDTO();
        dto.setProdutoId(p.getId());
        dto.setNome(p.getNome());
        dto.setDescricao(p.getDescricao());
        dto.setFornecedor(p.getFornecedor());
        dto.setMarca(p.getMarca());
        dto.setPrecoUnitario(p.getPrecoUnitario());
        dto.setQuantidadeNoEstoque(pe.getQuantidade());
        dto.setUsuarioId(p.getUsuario() != null ? p.getUsuario().getId() : null);

        dto.setEstoqueId(e.getId());
        dto.setNomeEstoque(e.getNome());

        return dto;
    }
}
