package com.sexto.ia.business;

import com.sexto.ia.service.CsvService;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;

public class Recomendador {
    public DataModel getModeloFilmes() throws IOException {
        File file = CsvService.readRatingFile();
        return new FileDataModel(file);
    }
}
