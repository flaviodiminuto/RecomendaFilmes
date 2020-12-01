package com.sexto.ia.service;

import com.sexto.ia.model.Filme;
import com.sexto.ia.repository.FilmeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        return repository.list();
    }

    public List<Filme> list(List<Long> ids) {
        return repository.listByIds(ids);
    }

    public List<Filme> listFilmeQuantidade(int quantidade) {
        Random random = new Random(System.currentTimeMillis());
        int i = 0;
        List<Long> ids = new ArrayList<>();
        while (i++ < quantidade) ids.add((long)random.nextInt(9000));
        return repository.listByIds(ids);
    }

    @Transactional
    public void save(Filme filme) {
        repository.persist(filme);
    }

    @Transactional
    public int updateAvaliacao(Filme filme, double avaliacao){
        return repository.update(" avaliacao = ?1 WHERE id = ?2", avaliacao, filme.getId());
    }

    public List<Filme> findByGenero(long generoId, int quantidade) {
        return repository.listByGenero(generoId,quantidade);
    }
}
