package com.sexto.ia.controller;

import com.sexto.ia.exceptions.SessaoExpiradaException;
import com.sexto.ia.model.Filme;
import com.sexto.ia.service.ControlaSessaoService;
import com.sexto.ia.service.FilmeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/filmes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FilmesController {

    @Inject
    FilmeService filmeService;

    @Inject
    ControlaSessaoService sessaoService;

    @GET
    public Response listaFilmes(@Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        List<Filme> filmes = filmeService.list();
        return Response.ok(filmes).cookie(cookie).build();
    }

    @GET
    @Path("/aleatorio/{quantidade}")
    public Response listaFilmesQuantidade(@PathParam("quantidade")int quantidade,@Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        List<Filme> filmes =  filmeService.listFilmeQuantidade(quantidade);
        return Response.ok(filmes).cookie(cookie).build();
    }

    @GET
    @Path("/{id}")
    public Response getFilmeById(@PathParam("id") Long id, @Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
         Filme filme = filmeService.findById(id);
         return Response.ok(filme).cookie(cookie).cookie(cookie).build();
    }

    @GET
    @Path("/genero/{genero_id}/{quantidade}")
    public Response getFilmeByGenero(@PathParam("genero_id") long generoId,
                                     @PathParam("quantidade") int quantidade,
                                     @Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        List<Filme> filmes = filmeService.findByGenero(generoId,quantidade);
        return Response.ok(filmes).cookie(cookie).build();
    }

    @POST
    public Response post(Filme filme, @Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        filmeService.save(filme);
        return Response.ok().cookie(cookie).build();
    }
}
