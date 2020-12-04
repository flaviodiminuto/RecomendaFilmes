package com.sexto.ia.service;

import com.sexto.ia.model.Autenticacao;
import com.sexto.ia.model.Usuario;
import com.sexto.ia.repository.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public Optional<Usuario> save(Autenticacao autenticacao){
        long ultimoId  = usuarioRepository.getMaxId() + 1;
        if(usuarioRepository.findByLogin(autenticacao.getLogin()).isPresent())
            return Optional.empty();

        Usuario usuario = new Usuario();
        usuario.setId(ultimoId);
        usuario.setChave(UUID.randomUUID().toString());
        usuario.setLogin(autenticacao.getLogin());
        usuario.setSenha(autenticacao.getSenha());
        usuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.persist(usuario);
        if(usuario.getId() != null)
            return Optional.of(usuario);
        else
            return Optional.empty();
    }

    @Transactional
    public Usuario ativar(Usuario usuario){
        if(usuario.getId() != null && !usuario.isAtivo()){
            usuario.setAtivo(true);
            atualizar(usuario);
        }
        return usuario;
    }

    @Transactional
    public Usuario atualizar(Usuario usuario){
        if(usuario.getId() == null) return null;
        usuarioRepository.update(usuario);
        return usuario;
    }

    public Optional<Usuario> autenticar(Autenticacao autenticacao){
        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByLoginAndSenha(autenticacao.getLogin(), autenticacao.getSenha());
        if(usuarioOptional.isEmpty()) return usuarioOptional;

        if(usuarioOptional.get().isAtivo())
            return usuarioOptional;
        else
            return Optional.empty();
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findByIdOptional(id);
    }
}
