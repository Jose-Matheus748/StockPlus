package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.UsuarioDTO;
import com.unifor.stockPlus.entity.Usuario;
import com.unifor.stockPlus.exceptions.BadRequestException;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public UsuarioDTO create(UsuarioDTO dto) {
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }

        if (usuarioRepo.existsByCpfOuCnpj(dto.getCpfOuCnpj())) {
            throw new BadRequestException("CPF/CNPJ já cadastrado");
        }

        Usuario usuario = dto.toEntity();
        usuarioRepo.save(usuario);

        return UsuarioDTO.fromEntity(usuario);
    }

    public UsuarioDTO getById(Long id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return UsuarioDTO.fromEntity(usuario);
    }

    public List<UsuarioDTO> getAll() {
        return usuarioRepo.findAll()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpfOuCnpj(dto.getCpfOuCnpj());
        usuario.setTipo(dto.getTipo());

        usuarioRepo.save(usuario);

        return UsuarioDTO.fromEntity(usuario);
    }

    public void delete(Long id) {
        if (!usuarioRepo.existsById(id))
            throw new ResourceNotFoundException("Usuário não encontrado");

        usuarioRepo.deleteById(id);
    }

    public UsuarioDTO login(String email, String senha) {
        var usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return UsuarioDTO.fromEntity(usuario);
    }

}
