package com.sexto.ia.initilizacao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sexto.ia.repository.FilmeRepository;
import com.sexto.ia.service.CsvService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
public class InicializaBanco {

    private final String BASE_INSERT = "INSERT INTO filme (titulo) VALUES %s";
    private Map<Long, List<String>> filme_genero = new HashMap<>();
    private Set<String> generoSet = new TreeSet<>();

    @Inject
    FilmeRepository repository;

    @Transactional
    public Long loadFilmes() throws IOException, CsvValidationException {

        //Ler arquivo converdo array[]
        if(repository.findById(1L) != null) return 0L;
        long contador = 0;
        CSVReader reader = CsvService.readMovieFileCSVReader();
        String[] nextLine;
        long cont = 0;
        StringBuilder valores = new StringBuilder();
        //Gerar uma string de insert
        while ((nextLine = reader.readNext()) != null) {
            contador++;
            ++cont;
            valores.append("('").append(nextLine[1]
                    .replaceAll("&","e")
                    .replaceAll("'", ""))
                    .append("'),\n");
            String[] generos = nextLine[2].split("\\|");
            //Preencher para o filme  chave valor variavel(Long, List<String>) (1,[Adventure|Animation|Children|Comedy|Fantasy])
            filme_genero.put(contador, Arrays.asList(generos));
            //Preencher para o genero chave valor list(String)
            generoSet.addAll(Arrays.asList(generos));

            if(cont >= 1000){
                String insert = String.format(BASE_INSERT,valores.substring(0,valores.length()-2)).concat(";");
                commit(insert);
                valores = new StringBuilder();
                cont = 0;
            }
        }
        commit(String.format(BASE_INSERT,valores.substring(0,valores.length()-2)).concat(";"));
        return contador;
    }

    @Transactional
    public void loadGeneros(){
        String insert = "INSERT INTO genero (nome) values ('%s')";
        generoSet.forEach(genero -> commit(String.format(insert,genero)));
    }

    @Transactional
    public void loadVinculos(){
        //para cada filme salvar na tabela intermediaria os vinculos com os generos
        String insert = "INSERT INTO filme_genero (filme_id,generos_genero_id)VALUES (%d,%d);";
        filme_genero.forEach((filme_id, generosFilme ) -> {
            List<String> generoTabela = new ArrayList<>(generoSet);
            generosFilme.forEach(genero -> {
                String ins = String.format(insert, filme_id, generoTabela.indexOf(genero) + 1);
                commit(ins);
            });
        });
    }

    private void commit(String insert) {
        EntityManager em = repository.getEntityManager();
        Query query = em.createNativeQuery(insert);
        query.executeUpdate();
        em.flush();
    }

    public void zerarValores(){
        this.filme_genero = new HashMap<>();
        this.generoSet = new HashSet<>();
    }
}
