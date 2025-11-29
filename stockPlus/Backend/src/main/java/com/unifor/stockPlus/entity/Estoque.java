package com.unifor.stockPlus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "estoques")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;       // Ex: "Estoque Principal"
    private String descricao;  // opcional

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos;
}
