package com.sexto.ia.controller;

import com.sexto.ia.business.Recomendador;
import com.sexto.ia.business.RecomendadorBuilder;
import com.sexto.ia.model.Filme;
import com.sexto.ia.model.PerfilInicial;
import com.sexto.ia.service.FilmeService;
import com.sexto.ia.service.RecomendadorService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Path("/recomendacao")
public class RecomendacaoController {

    private static RecomendadorService service = null;
    private static DataModel dataModel = null;

    @Inject
    FilmeService filmeService;

    @GET
    @Path("/{user_id}/{quantidade_recomendacao}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<?> recumenda(@PathParam("user_id") Long userId,
                                 @PathParam("quantidade_recomendacao") int quantidadeRecomendacao )
                                throws IOException, TasteException {

        int quantidadeParaBusca = quantidadeRecomendacao;
        if(dataModel == null)
            dataModel = new Recomendador().getModeloFilmes();
        if (service == null) {
            Recommender recommender = new RecomendadorBuilder().buildRecommender(dataModel);
            service = new RecomendadorService(recommender);
        }
        int quantidadeObtida = 0;
        List<Filme> filmes;
        do {
            List<RecommendedItem> recomendacoes = service.recomenda(userId, quantidadeParaBusca);
            List<Long> ids = recomendacoes
                    .stream()
                    .map(RecommendedItem::getItemID)
                    .collect(Collectors.toList());

            filmes = filmeService.list(ids);

// -- ALERTA DE GANBIARRA --2 atualiza a aviação dos filmes conforme são recalculados os modelos
            filmes.forEach(filme -> {
                RecommendedItem recomendacao =
                        recomendacoes.stream().filter(item -> item.getItemID()  == filme.getId()).findFirst().get();
                if(filme.getAvaliacao() == null || filme.getAvaliacao().floatValue() != recomendacao.getValue() ){
                    filme.setAvaliacao((double) recomendacao.getValue());
                    filmeService.updateAvaliacao(filme, recomendacao.getValue());
                }
            });
            quantidadeObtida = filmes.size();
            quantidadeParaBusca += 10;
        }while (quantidadeRecomendacao > quantidadeObtida);
        return filmes;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response montaPerfil(PerfilInicial perfilInicial){

        return Response.ok().build();
    }
}