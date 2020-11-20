package com.sexto.ia.service;

import com.sexto.ia.model.Filme;
import com.sexto.ia.repository.FilmeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class FilmeService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Inject
    FilmeRepository repository;

    public Filme findById(Long id) {
        Filme filme = repository.findById(id);
        log.info("Filme encontrado = "+filme);
        return filme;
    }

    public List<Filme> list() {
        return null;
    }

    public void save(Filme filme) {

    }
}
