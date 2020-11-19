package com.sexto.ia.controller;

import com.sexto.ia.business.Recomendador;
import com.sexto.ia.business.RecomendadorBuilder;
import com.sexto.ia.model.Filme;
import com.sexto.ia.service.FilmeService;
import com.sexto.ia.service.RecomendadorService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/recomendacao")
public class RecomendacaoController {

    private static RecomendadorService service = null;

    @Inject
    FilmeService filmeService;

    @GET
    @Path("/{user_id}/{quantidade_recomendacao}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Filme> recumenda(@PathParam("user_id") Long userId,
                                 @PathParam("quantidade_recomendacao") int quantidadeRecomendacao )
                                throws IOException, TasteException {
        if(service == null) {
            DataModel dataModel = new Recomendador().getModeloFilmes();
            Recommender recommender = new RecomendadorBuilder().buildRecommender(dataModel);
            service = new RecomendadorService(recommender);
        }
        List<Filme> filmes = new ArrayList<>();
        service.recomenda(userId,quantidadeRecomendacao).forEach(r-> filmes.add(filmeService.getFilme(r.getItemID())));
        return filmes;
    }
}