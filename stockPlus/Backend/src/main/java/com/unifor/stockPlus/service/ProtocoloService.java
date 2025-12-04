package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ItemProtocoloDTO;
import com.unifor.stockPlus.dto.ProtocoloDTO;
import com.unifor.stockPlus.entity.ItemProtocolo;
import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Protocolo;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.ItemProtocoloRepository;
import com.unifor.stockPlus.repository.ProdutoRepository;
import com.unifor.stockPlus.repository.ProtocoloRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProtocoloService {

    private final ProtocoloRepository protocoloRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemProtocoloRepository itemProtocoloRepository;

    public List<ProtocoloDTO> listar() {
        List<Protocolo> protocolos = protocoloRepository.findAll();

        return protocolos.stream()
                .map(protocolo -> {
                    List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());
                    return toDTO(protocolo, itens);
                })
                .collect(Collectors.toList());
    }


    public ProtocoloDTO criar(ProtocoloDTO dto) {

        Protocolo protocolo = new Protocolo();
        protocolo.setNome(dto.getNome());
        protocolo.setPreco(dto.getPreco());
        protocolo.setUsuarioId(dto.getUsuarioId());

        protocoloRepository.save(protocolo);

        if (dto.getItens() != null) {
            for (ItemProtocoloDTO itemDto : dto.getItens()) {

                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                ItemProtocolo item = new ItemProtocolo();
                item.setProduto(produto);
                item.setQuantidade(itemDto.getQuantidade());
                item.setProtocolo(protocolo);

                itemProtocoloRepository.save(item);
            }
        }

        List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());
        return toDTO(protocolo, itens);
    }

    @Transactional
    public ProtocoloDTO editar(Long id, ProtocoloDTO dto) {

        Protocolo protocolo = protocoloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));

        protocolo.setNome(dto.getNome());
        protocolo.setPreco(dto.getPreco());
        protocoloRepository.save(protocolo);

        itemProtocoloRepository.deleteByProtocoloId(protocolo.getId());

        if (dto.getItens() != null) {
            for (ItemProtocoloDTO itemDto : dto.getItens()) {

                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                ItemProtocolo novo = new ItemProtocolo();
                novo.setProduto(produto);
                novo.setQuantidade(itemDto.getQuantidade());
                novo.setProtocolo(protocolo);

                itemProtocoloRepository.save(novo);
            }
        }
        List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());
        return toDTO(protocolo, itens);
    }

    public void deletar(Long id) {
        protocoloRepository.deleteById(id);
    }

    public ProtocoloDTO adicionarItem(Long protocoloId, ItemProtocoloDTO itemDto) {

        Protocolo protocolo = protocoloRepository.findById(protocoloId)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));

        Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        ItemProtocolo item = new ItemProtocolo();
        item.setProduto(produto);     // ← apenas isso
        item.setQuantidade(itemDto.getQuantidade());
        item.setProtocolo(protocolo);

        itemProtocoloRepository.save(item);

        List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());
        return toDTO(protocolo, itens);
    }

    public void removerItem(Long itemId) {
        itemProtocoloRepository.deleteById(itemId);
    }

    public Double calcularValorTotal(Protocolo protocolo) {
        return protocolo.getItens().stream()
                .mapToDouble(i -> i.getProduto().getPrecoUnitario() * i.getQuantidade())
                .sum();
    }

    private ProtocoloDTO toDTO(Protocolo protocolo, List<ItemProtocolo> itens) {
        ProtocoloDTO dto = new ProtocoloDTO();

        dto.setId(protocolo.getId());
        dto.setNome(protocolo.getNome());
        dto.setPreco(protocolo.getPreco());
        dto.setUsuarioId(protocolo.getUsuarioId());
        dto.setValorTotal(
                itens.stream()
                        .mapToDouble(i -> i.getProduto().getPrecoUnitario() * i.getQuantidade())
                        .sum()
        );

        dto.setItens(
                itens.stream()
                        .map(this::toItemDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }



    private ItemProtocoloDTO toItemDTO(ItemProtocolo item) {
        ItemProtocoloDTO dto = new ItemProtocoloDTO();

        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoNome(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorItem(item.getProduto().getPrecoUnitario() * item.getQuantidade());

        return dto;
    }

    public ProtocoloDTO buscarPorId(Long id) {
        Protocolo protocolo = protocoloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));

        List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());

        return toDTO(protocolo, itens);
    }


    public List<ProtocoloDTO> buscarPorUsuario(Long usuarioId) {
        List<Protocolo> protocolos = protocoloRepository.findByUsuarioId(usuarioId);

        return protocolos.stream()
                .map(protocolo -> {
                    List<ItemProtocolo> itens = itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());
                    return toDTO(protocolo, itens);
                })
                .collect(Collectors.toList());
    }

}
