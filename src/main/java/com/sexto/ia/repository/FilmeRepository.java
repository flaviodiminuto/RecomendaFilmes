package com.sexto.ia.repository;

import com.sexto.ia.model.Filme;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FilmeRepository implements PanacheRepositoryBase<Filme, Long> {
}
