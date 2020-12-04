package com.sexto.ia.repository;

import com.sexto.ia.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, Long> {

    public Optional<Usuario> findByLoginAndSenha(String login, String senha) {
        PanacheQuery<Usuario> query = find("login = ?1 AND senha = ?2", login,  senha);
        return Optional.ofNullable(query.firstResult());
    }

    public Optional<Usuario> findByLogin(String login) {
        PanacheQuery<Usuario> query = find("login", login);
        return Optional.ofNullable(query.firstResult());
    }

    public Long getMaxId() {
        String sql = "SELECT max( id ) FROM usuario";
        Query query = getEntityManager().createNativeQuery(sql);
        BigInteger id = (BigInteger) query.getSingleResult();
        return id == null ? 0 : id.longValue();
    }

    public boolean update(Usuario usuario) {
        return update("ativo = true WHERE chave = ?1 AND id = ?2", usuario.getChave(), usuario.getId()) > 0;
    }
}
