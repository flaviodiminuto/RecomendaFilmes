package com.sexto.ia.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.sexto.ia.model.Filme;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FilmeService {
    private final String database = "recomendacao";
    private final String collection = "filmes";

    @Inject
    MongoClient mongoClient;

    Logger log = LoggerFactory.getLogger(this.getClass());


    public List<Filme> list(){
        List<Filme> list = new ArrayList<>();
        MongoCursor<Document> cursor = getCollection().find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Filme filme = new Filme();
                filme.setId(document.getLong("id"));
                filme.setTitulo(document.getString("titulo"));
                filme.setGeneros(document.getString("generos"));
                list.add(filme);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void add(Filme filme){
        Document generos = new Document();
        Arrays.stream(filme.getGeneros()).forEach(g-> generos.append("genero", g));
        Document document = new Document()
                .append("id",  filme.getId())
                .append("titulo", filme.getTitulo())
                .append("generos", generos);
        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase(database).getCollection(collection);
    }

    public Filme getFilme(Long id){
         Document document =  mongoClient.getDatabase(database)
                .getCollection(collection)
                .find(Filters.eq("id", id))
                .first();
        if (document == null) {
            return null;
        }
        Filme filme = new Filme();
        filme.setId(document.getLong("id"));
        filme.setTitulo(document.getString("titulo"));
        filme.setGeneros(document.getString("generos"));
        return filme;
    }
}
