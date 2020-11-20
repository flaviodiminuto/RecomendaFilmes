package com.sexto.ia.initilizacao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sexto.ia.model.Filme;
import com.sexto.ia.repository.FilmeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;

@ApplicationScoped
public class CsvService {

    @Inject
    FilmeRepository repository;

    @Transactional
    public long saveToDatabase() throws IOException, CsvValidationException {
        if(repository.findById(1L) != null) return 0L;
        long contador = 0;
        CSVReader reader = new CSVReader(new FileReader("C:\\Projetos\\recomendacao\\src\\main\\resources\\movies.csv"));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Filme filme = new Filme(null, nextLine[1]);
            repository.persist(filme);
            contador++;
        }
        return contador;
    }
}
