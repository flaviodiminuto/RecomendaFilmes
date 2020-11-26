package com.sexto.ia.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Recomendacao {
    Long userId;
    Date dataRecomendacao;
    Collection<Filme> filmes;

    public Recomendacao() {
    }

    public Recomendacao(Long userId, Date dataRecomendacao, Collection<Filme> filmes) {
        this.userId = userId;
        this.dataRecomendacao = dataRecomendacao;
        this.filmes = filmes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDataRecomendacao() {
        return dataRecomendacao;
    }

    public void setDataRecomendacao(Date dataRecomendacao) {
        this.dataRecomendacao = dataRecomendacao;
    }

    public Collection<Filme> getFilmes() {
        return filmes;
    }

    public void setFilmes(Collection<Filme> filmes) {
        this.filmes = filmes;
    }
}
