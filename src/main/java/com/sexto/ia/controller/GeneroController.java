package com.sexto.ia.controller;

import com.sexto.ia.model.Genero;
import com.sexto.ia.repository.GeneroRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/generos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeneroController {

    @Inject
    GeneroRepository generoRepository;

    @GET
    public Response listAllGeneros(){
        List<Genero> generos =  generoRepository.listAll();
        return Response.ok(generos).build();
    }

}
