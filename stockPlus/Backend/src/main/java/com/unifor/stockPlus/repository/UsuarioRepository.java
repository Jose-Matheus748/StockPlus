package com.unifor.stockPlus.repository;
import com.unifor.stockPlus.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpfOuCnpj(String cpfOuCnpj);
    Optional<Usuario> findByEmail(String email);
}
