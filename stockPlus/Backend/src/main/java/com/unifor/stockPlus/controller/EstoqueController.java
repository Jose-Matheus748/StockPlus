package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.EstoqueDTO;
import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.dto.ValorTotalEstoqueDTO;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.service.EstoqueService;
import com.unifor.stockPlus.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private UsuarioService usuarioService;


    // ------------------------------------------------------------
    // CRIAR ESTOQUE (com usuário autenticado)
    // ------------------------------------------------------------
    @PostMapping
    public ResponseEntity<EstoqueDTO> create(@RequestBody EstoqueDTO dto) {

        Usuario usuario = usuarioService.getEntityById(dto.getUsuarioId());

        EstoqueDTO criado = estoqueService.create(dto, usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }


    // ------------------------------------------------------------
    // BUSCAR TODOS OS ESTOQUES (opcional — talvez admin use)
    // ------------------------------------------------------------
    @GetMapping
    public List<EstoqueDTO> getAll() {
        return estoqueService.getAll();
    }

    // ------------------------------------------------------------
    // BUSCAR ESTOQUE POR ID (sem validar usuário aqui)
    // ------------------------------------------------------------
    @GetMapping("/{id}")
    public EstoqueDTO get(@PathVariable Long id) {
        return estoqueService.getById(id);
    }

    // ------------------------------------------------------------
    // LISTAR PRODUTOS DE UM ESTOQUE
    // ------------------------------------------------------------
    @GetMapping("/{id}/produtos")
    public List<ProdutoDTO> listarProdutos(@PathVariable Long id) {
        return estoqueService.listarProdutosDoEstoque(id);
    }

    // ------------------------------------------------------------
    // ATUALIZAR ESTOQUE DO USUÁRIO
    // ------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueDTO> update(@PathVariable Long id, @RequestBody EstoqueDTO dto) {
        Long usuarioId = obterUsuarioAutenticado();
        Usuario usuario = usuarioService.getEntityById(usuarioId);

        EstoqueDTO atualizado = estoqueService.update(id, dto, usuario);
        return ResponseEntity.ok(atualizado);
    }

    // ------------------------------------------------------------
    // DELETAR ESTOQUE DO USUÁRIO
    // ------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long usuarioId = obterUsuarioAutenticado();
        Usuario usuario = usuarioService.getEntityById(usuarioId);

        estoqueService.delete(id, usuario);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------------------
    // CALCULAR VALOR TOTAL DO ESTOQUE
    // ------------------------------------------------------------
    @GetMapping("/{id}/valor-total")
    public ValorTotalEstoqueDTO calcularValorTotal(@PathVariable Long id) {
        return estoqueService.calcularValorTotal(id);
    }

    // ------------------------------------------------------------
    // LISTAR APENAS OS ESTOQUES DO USUÁRIO LOGADO
    // ------------------------------------------------------------
    @GetMapping("/meus-estoques")
    public ResponseEntity<List<EstoqueDTO>> meusEstoques(@RequestParam Long usuarioId) {
        List<EstoqueDTO> estoques = estoqueService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(estoques);
    }

    // ------------------------------------------------------------
    // CRIAR ESTOQUE (rota alternativa que você tinha)
    // ------------------------------------------------------------
    @PostMapping("/novo")
    public ResponseEntity<EstoqueDTO> criarNovoEstoque(@RequestBody EstoqueDTO estoqueDTO) {
        Long usuarioId = obterUsuarioAutenticado();
        Usuario usuario = usuarioService.getEntityById(usuarioId);

        EstoqueDTO novoEstoque = estoqueService.create(estoqueDTO, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstoque);
    }

    // ------------------------------------------------------------
    // PEGAR ID DO USUÁRIO AUTENTICADO
    // ------------------------------------------------------------
    private Long obterUsuarioAutenticado() {
        // TODO: Integrar com Spring Security ou JWT
        return 1L; // TEMPORÁRIO
    }
}
