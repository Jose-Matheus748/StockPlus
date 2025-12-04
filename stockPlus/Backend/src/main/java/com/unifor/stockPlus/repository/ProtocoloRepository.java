package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Protocolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProtocoloRepository extends JpaRepository<Protocolo, Long> {
    List<Protocolo> findByUsuarioId(Long usuarioId);
}