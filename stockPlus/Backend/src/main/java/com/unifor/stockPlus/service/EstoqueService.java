package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.EstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    public EstoqueDTO create(EstoqueDTO dto, Usuario usuario) {
        Estoque estoque = new Estoque();
        estoque.setNome(dto.getNome());
        estoque.setDescricao(dto.getDescricao());
        estoque.setUsuario(usuario);

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
                .mapToInt(Produto::getQuantidade)
                .sum();

        return new ValorTotalEstoqueDTO(valorTotal, quantidadeTotal);
    }

    public List<ProdutoDTO> listarProdutosDoEstoque(Long estoqueId) {
        estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        return produtoRepository.findByEstoqueId(estoqueId)
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public EstoqueDTO criarEstoquePadrao(Usuario usuario) {
        Estoque estoque = new Estoque();
        estoque.setNome("Estoque Padrão");
        estoque.setDescricao("Estoque padrão criado automaticamente");
        estoque.setUsuario(usuario);

        Estoque saved = estoqueRepository.save(estoque);
        return EstoqueDTO.fromEntity(saved);
    }

    public List<EstoqueDTO> listarPorUsuario(Long usuarioId) {
        return estoqueRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(EstoqueDTO::fromEntity)
                .toList();
    }

    public boolean pertenceAoUsuario(Long estoqueId, Long usuarioId) {
        return estoqueRepository.existsByIdAndUsuarioId(estoqueId, usuarioId);
    }

    public EstoqueDTO update(Long id, EstoqueDTO dto, Usuario usuarioAutenticado) {
        if (!pertenceAoUsuario(id, usuarioAutenticado.getId())) {
            throw new ResourceNotFoundException("Estoque não encontrado ou não pertence ao usuário");
        }

        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        estoque.setNome(dto.getNome());
        estoque.setDescricao(dto.getDescricao());

        Estoque atualizado = estoqueRepository.save(estoque);

        return EstoqueDTO.fromEntity(atualizado);
    }

    public void delete(Long id, Usuario usuarioAutenticado) {
        if (!pertenceAoUsuario(id, usuarioAutenticado.getId())) {
            throw new ResourceNotFoundException("Estoque não encontrado ou não pertence ao usuário");
        }

        estoqueRepository.deleteById(id);
    }
}
