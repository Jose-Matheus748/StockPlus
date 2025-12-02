package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepo) {
        this.produtoRepository = produtoRepo;
    }

    // Criar produto (sem estoque)
    public ProdutoDTO create(ProdutoDTO dto, Usuario usuarioCriador) {

        Produto produto = dto.toEntity();
        produto.setUsuario(usuarioCriador);

        produtoRepository.save(produto);

        return ProdutoDTO.fromEntity(produto);
    }

    // Buscar por ID
    public ProdutoDTO getById(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return ProdutoDTO.fromEntity(produto);
    }

    // Listar todos
    public List<ProdutoDTO> getAll() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    // Atualizar produto
    public ProdutoDTO update(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setFornecedor(dto.getFornecedor());
        produto.setMarca(dto.getMarca());
        produto.setPrecoUnitario(dto.getPrecoUnitario());

        produtoRepository.save(produto);

        return ProdutoDTO.fromEntity(produto);
    }

    // Deletar
    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        produtoRepository.deleteById(id);
    }

    // Listar produtos por usuário
    public List<ProdutoDTO> listarPorUsuario(Long usuarioId) {
        return produtoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }
}
