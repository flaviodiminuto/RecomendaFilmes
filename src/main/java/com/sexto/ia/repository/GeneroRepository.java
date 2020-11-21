package com.sexto.ia.repository;

import com.sexto.ia.model.Genero;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeneroRepository implements PanacheRepositoryBase<Genero, Long> {
}
