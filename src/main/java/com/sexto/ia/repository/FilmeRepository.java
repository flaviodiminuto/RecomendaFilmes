package com.sexto.ia.repository;

import com.sexto.ia.model.Filme;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FilmeRepository implements PanacheRepositoryBase<Filme, Long> {

    public List<Filme> listByIds(List<Long> ids) {
        if(ids.isEmpty()) return new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        ids.forEach(id -> stringBuilder.append(id).append(", "));

        String query = String.format("SELECT f FROM Filme f WHERE id IN (%s)",
                stringBuilder.substring(0,stringBuilder.length()-2));
        return  select(query).list();
    }

    public List<Filme> find(int quantidade) {
        String query = " SELECT f FROM Filme f";
        return select(query).page(Page.ofSize(quantidade)).list();
    }

    public List<Filme> list() {
        return find(50);
    }

    private PanacheQuery<Filme> select(String select){
        return find(select);
    }
}
