package com.sexto.ia.service;

import com.sexto.ia.model.Filme;
import com.sexto.ia.repository.FilmeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
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
        return repository.list();
    }

    public List<Filme> list(List<Long> ids) {
        return repository.listByIds(ids);
    }

    public List<Filme> listFilmeQuantidade(int quantidade) {
        Long[] ids = {1L, 2L, 4L};
        return repository.listByIds(Arrays.asList(ids.clone()));
    }

    @Transactional
    public void save(Filme filme) {
        repository.persist(filme);
    }

    @Transactional
    public int updateAvaliacao(Filme filme, double avaliacao){
        return repository.update(" avaliacao = ?1 WHERE id = ?2", avaliacao, filme.getId());
    }
}
