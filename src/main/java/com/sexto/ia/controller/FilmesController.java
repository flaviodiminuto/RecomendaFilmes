package com.sexto.ia.controller;

import com.sexto.ia.model.Filme;
import com.sexto.ia.service.FilmeService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/filmes")
public class FilmesController {

    @Inject
    FilmeService filmeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Filme> listaFilmes(){
        List<Filme> filmes =  filmeService.list();
        return filmes;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Filme getFilmeById(@PathParam("id") Long id){
        return filmeService.getFilme(id);
    }
}
