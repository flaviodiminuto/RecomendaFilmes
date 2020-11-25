package com.sexto.ia.model;

import java.util.List;

public class PerfilInicial {
    private List<Long> filmesMaisLegais;
    private List<Long> filmesNormais;
    private List<Long> filmesMenosLegais;

    public List<Long> getFilmesMaisLegais() {
        return filmesMaisLegais;
    }

    public void setFilmesMaisLegais(List<Long> filmesMaisLegais) {
        this.filmesMaisLegais = filmesMaisLegais;
    }

    public List<Long> getFilmesNormais() {
        return filmesNormais;
    }

    public void setFilmesNormais(List<Long> filmesNormais) {
        this.filmesNormais = filmesNormais;
    }

    public List<Long> getFilmesMenosLegais() {
        return filmesMenosLegais;
    }

    public void setFilmesMenosLegais(List<Long> filmesMenosLegais) {
        this.filmesMenosLegais = filmesMenosLegais;
    }
}
