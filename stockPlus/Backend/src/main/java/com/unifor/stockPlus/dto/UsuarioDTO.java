package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.TipoUsuario;
import com.unifor.stockPlus.entity.Usuario;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpfOuCnpj;
    private TipoUsuario tipo;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpfOuCnpj(usuario.getCpfOuCnpj());
        dto.setTipo(usuario.getTipo());

        dto.setSenha(null);

        return dto;
    }

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setCpfOuCnpj(this.cpfOuCnpj);
        usuario.setTipo(this.tipo);
        return usuario;
    }
}
