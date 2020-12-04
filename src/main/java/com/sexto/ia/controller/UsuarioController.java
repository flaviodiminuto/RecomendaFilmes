package com.sexto.ia.controller;

import com.sexto.ia.exceptions.SessaoExpiradaException;
import com.sexto.ia.model.Autenticacao;
import com.sexto.ia.model.Usuario;
import com.sexto.ia.service.ControlaSessaoService;
import com.sexto.ia.service.UsuarioService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;
    @Inject
    ControlaSessaoService sessaoService;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id,@Context HttpHeaders headers) throws SessaoExpiradaException {
        NewCookie cookie = sessaoService.mantemSessao(headers.getCookies());
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if(usuarioOptional.isPresent())
            return Response.ok(usuarioOptional.get()).cookie(cookie).build();
        else
            return Response.status(Status.NOT_FOUND).cookie(cookie).build();
    }

    @POST
    public Response post(Autenticacao autenticacao){
        Optional<Usuario> usuarioOptional = usuarioService.save(autenticacao);
        if(usuarioOptional.isPresent())
            return Response.status(Status.CREATED).entity(usuarioOptional.get()).build();
        else
            return Response.status(Status.FORBIDDEN).build();
    }

    @POST
    @Path("/autenticacao")
    public Response autenticar(Autenticacao autenticacao){
        Optional<Usuario> usuarioOptional = usuarioService.autenticar(autenticacao);
        NewCookie cookie = sessaoService.getCookiePredicadoValido();

        if(usuarioOptional.isPresent())
            return Response.ok().entity(usuarioOptional.get()).cookie(cookie).build();
        else
            return Response.status(Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{id}/chave/{segredo}")
    public Response ativar(@PathParam("id")Long id,
                           @PathParam("segredo")String segredo) throws SessaoExpiradaException {
        Optional<Usuario> usuario = usuarioService.findById(id);

        if (usuario.isPresent()) {
            usuarioService.ativar(usuario.get());
            return Response.ok(usuario.get()).build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }

}
