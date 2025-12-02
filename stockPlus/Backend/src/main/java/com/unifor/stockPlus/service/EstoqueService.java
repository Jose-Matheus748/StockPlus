package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.ProdutoEstoque;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.EstoqueRepository;
import com.unifor.stockPlus.repository.ProdutoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final ProdutoEstoqueService produtoEstoqueService;

    public EstoqueService(EstoqueRepository estoqueRepository,
                          ProdutoEstoqueRepository produtoEstoqueRepository,
                          ProdutoEstoqueService produtoEstoqueService) {
        this.estoqueRepository = estoqueRepository;
        this.produtoEstoqueRepository = produtoEstoqueRepository;
        this.produtoEstoqueService = produtoEstoqueService;
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

        if (estoque.getProdutoEstoques() == null || estoque.getProdutoEstoques().isEmpty()) {
            return new ValorTotalEstoqueDTO(0.0, 0);
        }

        double valorTotal = estoque.getProdutoEstoques().stream()
                .mapToDouble(pe -> pe.getProduto().getPrecoUnitario() * pe.getQuantidade())
                .sum();

        int quantidadeTotal = estoque.getProdutoEstoques().stream()
                .mapToInt(ProdutoEstoque::getQuantidade)
                .sum();

        return new ValorTotalEstoqueDTO(valorTotal, quantidadeTotal);
    }

    public List<ProdutoDTO> listarProdutosDoEstoque(Long estoqueId) {

        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        return produtoEstoqueRepository.findByEstoqueId(estoqueId)
                .stream()
                .map(pe -> {
                    ProdutoDTO dto = ProdutoDTO.fromEntity(pe.getProduto());
                    dto.setQuantidadeNoEstoque(pe.getQuantidade());
                    return dto;
                })
                .toList();
    }

    public EstoqueDTO criarEstoquePadrao(Usuario usuario) {
        Estoque estoque = new Estoque();
        estoque.setNome("Estoque Padrão");
        estoque.setDescricao("Seu primeiro estoque");
        estoque.setUsuario(usuario);

        return EstoqueDTO.fromEntity(estoqueRepository.save(estoque));
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

        return EstoqueDTO.fromEntity(estoqueRepository.save(estoque));
    }

    public void delete(Long id, Usuario usuarioAutenticado) {

        if (!pertenceAoUsuario(id, usuarioAutenticado.getId())) {
            throw new ResourceNotFoundException("Estoque não encontrado ou não pertence ao usuário");
        }

        // remove somente associações
        produtoEstoqueService.limparAssociacoesAoDeletarEstoque(id);

        // remove estoque
        estoqueRepository.deleteById(id);
    }
}
