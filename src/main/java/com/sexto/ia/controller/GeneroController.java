package com.sexto.ia.controller;

import com.sexto.ia.exceptions.SessaoExpiradaException;
import com.sexto.ia.model.Genero;
import com.sexto.ia.repository.GeneroRepository;
import com.sexto.ia.service.ControlaSessaoService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/generos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeneroController {

    @Inject
    GeneroRepository generoRepository;

    @Inject
    ControlaSessaoService sessaoService;

    @GET
    public Response listAllGeneros(@Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        List<Genero> generos =  generoRepository.listAll();
        return Response.ok(generos).cookie(cookie).build();
    }

}
