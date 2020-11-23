package com.sexto.ia.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.sexto.ia.initilizacao.InicializaBanco;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Config {

    @Inject
    InicializaBanco service;

    @POST
    @Path("/filmes")
    @Produces(MediaType.TEXT_PLAIN)
    public String post() throws IOException, CsvValidationException{
        service.zerarValores();
        long registrosSalvos = service.loadFilmes();
        if(registrosSalvos > 0) {
            service.loadGeneros();
            service.loadVinculos();
        }
        service.zerarValores();
        return String.format("Foram sanvos %d registros com sucesso!", registrosSalvos);
    }
}
