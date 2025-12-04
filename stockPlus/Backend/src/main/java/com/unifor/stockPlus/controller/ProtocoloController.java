package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ItemProtocoloDTO;
import com.unifor.stockPlus.dto.ProtocoloDTO;
import com.unifor.stockPlus.service.ProtocoloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/protocolos")
@RequiredArgsConstructor
public class ProtocoloController {

    private final ProtocoloService protocoloService;

    @GetMapping
    public ResponseEntity<List<ProtocoloDTO>> listar() {
        return ResponseEntity.ok(protocoloService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProtocoloDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(protocoloService.buscarPorId(id));
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProtocoloDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(protocoloService.buscarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<ProtocoloDTO> criar(@RequestBody ProtocoloDTO dto) {
        ProtocoloDTO criado = protocoloService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProtocoloDTO> editar(
            @PathVariable Long id,
            @RequestBody ProtocoloDTO dto
    ) {
        return ResponseEntity.ok(protocoloService.editar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        protocoloService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<ProtocoloDTO> adicionarItem(
            @PathVariable Long id,
            @RequestBody ItemProtocoloDTO itemDto
    ) {
        return ResponseEntity.ok(protocoloService.adicionarItem(id, itemDto));
    }
}
