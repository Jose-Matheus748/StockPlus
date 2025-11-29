package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.UsuarioDTO;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EstoqueService estoqueService;

    public UsuarioService(UsuarioRepository usuarioRepository, EstoqueService estoqueService) {
        this.usuarioRepository = usuarioRepository;
        this.estoqueService = estoqueService;
    }

    public UsuarioDTO getById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioDTO.fromEntity(usuario);
    }

    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpfOuCnpj(dto.getCpfOuCnpj());
        usuario.setTipo(dto.getTipo());

        usuarioRepository.save(usuario);

        return UsuarioDTO.fromEntity(usuario);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id))
            throw new ResourceNotFoundException("Usuário não encontrado");

        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return UsuarioDTO.fromEntity(usuario);
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioDTO.toEntity();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar estoque padrão após salvar usuário
        estoqueService.criarEstoquePadrao(usuarioSalvo);

        return UsuarioDTO.fromEntity(usuarioSalvo);
    }

    public Usuario getEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}
