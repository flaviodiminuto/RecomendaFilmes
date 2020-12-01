package com.sexto.ia.controller;

import com.sexto.ia.model.Filme;
import com.sexto.ia.service.FilmeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/filmes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FilmesController {

    @Inject
    FilmeService filmeService;

    @GET
    public List<Filme> listaFilmes(){
        return filmeService.list();
    }

    @GET
    @Path("/aleatorio/{quantidade}")
    public List<Filme> listaFilmesQuantidade(@PathParam("quantidade")int quantidade){
        return filmeService.listFilmeQuantidade(quantidade);
    }

    @GET
    @Path("/{id}")
    public Filme getFilmeById(@PathParam("id") Long id){
        return filmeService.findById(id);
    }

    @GET
    @Path("/genero/{genero_id}/{quantidade}")
    public Response getFilmeByGenero(@PathParam("genero_id") long generoId, @PathParam("quantidade") int quantidade ){
        List<Filme> filmes = filmeService.findByGenero(generoId,quantidade);
        return Response.ok(filmes).build();
    }


    @POST
    public Filme post(Filme filme){
        filmeService.save(filme);
        return filme;
    }
}
