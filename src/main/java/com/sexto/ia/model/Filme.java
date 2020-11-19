package com.sexto.ia.model;

public class Filme {
    private Long id;
    private String titulo;
    private String[] generos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String[] getGeneros() {
        return generos;
    }

    public void setGeneros(String generos) {
        this.generos = generos.split("\\|");
    }
}
