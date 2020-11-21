package com.sexto.ia.initilizacao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sexto.ia.model.Filme;
import com.sexto.ia.model.Genero;
import com.sexto.ia.repository.FilmeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CsvService {

    @Inject
    FilmeRepository repository;
    Map<String,Long> generosSet = new HashMap<>();

    @Transactional
    public long saveToDatabase() throws IOException, CsvValidationException, InterruptedException {
        if(repository.findById(1L) != null) return 0L;
        long contador = 0;
        CSVReader reader = new CSVReader(new FileReader("C:\\Projetos\\recomendacao\\src\\main\\resources\\movies.csv"));
        String [] nextLine;
        Filme filme;
        while ((nextLine = reader.readNext()) != null) {
             filme = new Filme(null, nextLine[1]);
             Set<Genero> generos = getGeneros(nextLine[2].split("\\|"));
             filme.setGeneros(generos);
            repository.persist(filme);
            contador++;
            if(contador == 1000){
                repository.flush();
                break;
            }
        }
        return contador;
    }

    private Set<Genero> getGeneros(String[] generos) {
        return Arrays.stream(generos).map(this::getGenero).collect(Collectors.toSet());
    }

    private Genero getGenero(String genero){
        Long id = generosSet.get(genero);
        Genero retorno = new Genero();
        retorno.setNome(genero);
        if(id == null) {
//            generoRepository.persist(retorno);
            generosSet.put(genero, (long) generosSet.size()+1);
        }
        return retorno;
    }





}
