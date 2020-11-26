package com.sexto.ia.controller;

import com.sexto.ia.business.Recomendador;
import com.sexto.ia.business.RecomendadorBuilder;
import com.sexto.ia.model.Filme;
import com.sexto.ia.model.PerfilInicial;
import com.sexto.ia.model.Recomendacao;
import com.sexto.ia.service.FilmeService;
import com.sexto.ia.service.RecomendadorService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Path("/recomendacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecomendacaoController{

    private RecomendadorService recomendadorService;
    private DataModel dataModel;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    FilmeService filmeService;

    public RecomendacaoController() throws IOException, TasteException {
        dataModel = new Recomendador().getModeloFilmes();
        Recommender recommender = new RecomendadorBuilder().buildRecommender(dataModel);
        recomendadorService = new RecomendadorService(recommender, filmeService);
    }

    @GET
    @Path("/{user_id}/{quantidade_recomendacao}")
    public Response recumenda(@PathParam("user_id") Long userId,
                                 @PathParam("quantidade_recomendacao") int quantidadeRecomendacao )
                                throws TasteException {
        List<Filme> filmes  = recomendadorService.recomenda(userId,quantidadeRecomendacao);
        Recomendacao recomendacao = new Recomendacao(userId,new Date(), filmes);
        return Response.ok().entity(recomendacao).build();
    }

    @POST
    public Response montaPerfil(PerfilInicial perfilInicial) throws IOException, TasteException {
        logger.info("Inciando a montagem do novo perfil");
        Long novoUsuarioId = recomendadorService.montaPerfilInicial(perfilInicial);
        reloadRecomendador();
        Collection<Filme> filmeList = recomendadorService.recomenda(novoUsuarioId,10);
        logger.info("Recomendacoes entregues para o novo perfil");
        Recomendacao recomendacao = new Recomendacao(novoUsuarioId,new Date(), filmeList);
        return Response.ok().entity(recomendacao).build();
    }

    private void reloadRecomendador() throws IOException, TasteException {
        logger.info("Remotando o datamodel e recomendador");
        dataModel = new Recomendador().getModeloFilmes();
            Recommender recommender = new RecomendadorBuilder().buildRecommender(dataModel);
            recomendadorService = new RecomendadorService(recommender, filmeService);
    }
}