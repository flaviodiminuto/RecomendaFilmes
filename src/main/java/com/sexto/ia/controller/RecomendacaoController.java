package com.sexto.ia.controller;

import com.sexto.ia.business.Recomendador;
import com.sexto.ia.business.RecomendadorBuilder;
import com.sexto.ia.model.Filme;
import com.sexto.ia.service.FilmeService;
import com.sexto.ia.service.RecomendadorService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/recomendacao")
public class RecomendacaoController {

    private static RecomendadorService service = null;

    @Inject
    FilmeService filmeService;

    @GET
    @Path("/{user_id}/{quantidade_recomendacao}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<?> recumenda(@PathParam("user_id") Long userId,
                                 @PathParam("quantidade_recomendacao") int quantidadeRecomendacao )
                                throws IOException, TasteException {
        int quantidadeEfetiva = quantidadeRecomendacao;
        if (service == null) {
            DataModel dataModel = new Recomendador().getModeloFilmes();
            Recommender recommender = new RecomendadorBuilder().buildRecommender(dataModel);
            service = new RecomendadorService(recommender);
        }
        int quantidadeObtida = 0;
        Set<Filme> filmes = new HashSet<>();
        do {
            List<RecommendedItem> recomendacoes = service.recomenda(userId, quantidadeEfetiva);
            recomendacoes.stream()
                    .forEach(r ->{
                       Filme filme = filmeService.findById(r.getItemID());
                       if(filme != null){
                           filme.setAvaliacao((double) r.getValue());
                           filmes.add(filme);
                       }
                    });
            quantidadeObtida = filmes.size();
            quantidadeEfetiva += 10;
        }while (quantidadeRecomendacao > quantidadeObtida);
        List<Filme> finalList = new ArrayList<>();
        List<Filme> filmesList = new ArrayList<>(filmes);
        for (int i = 0; i < quantidadeRecomendacao; i++) {
            finalList.add(filmesList.get(i));
        }
            return finalList;
    }
}