package com.unifor.stockPlus.service;

import com.unifor.stockPlus.entity.ItemProtocolo;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Protocolo;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.ItemProtocoloRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import com.unifor.stockPlus.repository.ProtocoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemProtocoloService {

    private final ItemProtocoloRepository itemRepo;
    private final ProtocoloRepository protocoloRepo;
    private final ProdutoRepository produtoRepo;


    /**
     * Criar item dentro de um protocolo já existente
     */
    public ItemProtocolo criar(Long protocoloId, Long produtoId, Integer quantidade) {

        Protocolo protocolo = protocoloRepo.findById(protocoloId)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));

        Produto produto = produtoRepo.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ItemProtocolo item = new ItemProtocolo();
        item.setProtocolo(protocolo);
        item.setProduto(produto);       // JPA salva produtoid automaticamente
        item.setQuantidade(quantidade);

        return itemRepo.save(item);
    }


    /**
     * Buscar um item específico
     */
    public ItemProtocolo buscar(Long id) {
        return itemRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }


    /**
     * Remover item do protocolo
     */
    public void remover(Long itemId) {
        if (!itemRepo.existsById(itemId)) {
            throw new ResourceNotFoundException("Item não encontrado");
        }

        itemRepo.deleteById(itemId);
    }
}
