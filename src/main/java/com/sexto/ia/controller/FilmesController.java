package com.sexto.ia.controller;

import com.sexto.ia.model.Filme;
import com.sexto.ia.service.FilmeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/filmes")
public class FilmesController {

    @Inject
    FilmeService filmeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Filme> listaFilmes(){
        return filmeService.list();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/aleatorio/{quantidade}")
    public List<Filme> listaFilmesQuantidade(@PathParam("quantidade")int quantidade){
        return filmeService.listFilmeQuantidade(quantidade);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Filme getFilmeById(@PathParam("id") Long id){
        return filmeService.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Filme post(Filme filme){
        filmeService.save(filme);
        return filme;
    }
}
