package com.sexto.ia.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "filme_id")
    private Long id;
    private String titulo;
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="filme_genero", joinColumns=@JoinColumn(name= "filme_id"))
    private Set<Genero> generos;
    @Column(nullable = true)
    private Double avaliacao;

    public Filme() {
    }

    public Filme(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

}
