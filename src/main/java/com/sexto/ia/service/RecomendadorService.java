package com.sexto.ia.service;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.util.List;

public class RecomendadorService {

    private final Recommender recomendador;

    public RecomendadorService(Recommender recommender) {
        this.recomendador = recommender;
    }

    public List<RecommendedItem>  recomenda(Long userId,int quantidadeDeRecomendacoes) throws TasteException {
        return recomendador.recommend(userId,quantidadeDeRecomendacoes);
    }
}
