package com.sexto.ia.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.sexto.ia.exceptions.SessaoExpiradaException;
import com.sexto.ia.initilizacao.InicializaBanco;
import com.sexto.ia.service.ControlaSessaoService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Config {

    @Inject
    InicializaBanco service;

    @Inject
    ControlaSessaoService sessaoService;

    @POST
    @Path("/filmes")
    @Produces(MediaType.TEXT_PLAIN)
    public Response post(@Context HttpHeaders headers) throws IOException, CsvValidationException, SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        service.zerarValores();
        long registrosSalvos = service.loadFilmes();
        if(registrosSalvos > 0) {
            service.loadGeneros();
            service.loadVinculos();
        }
        service.zerarValores();
        String retorno = String.format("Foram sanvos %d registros com sucesso!", registrosSalvos);
        return Response.ok(retorno).cookie(cookie).build();
    }
}
