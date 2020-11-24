package com.sexto.ia.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class CsvService {

    public static CSVReader readMovieFileCSVReader() throws FileNotFoundException {
        return new CSVReader(new FileReader("src/main/resources/movies.csv"));
    }

    public static File readRatingFile(){
        return new File("src/main/resources/ratings.csv");
    }

    public static void writeFileCSV(File arquivo, List<String[]> linhas) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(arquivo, true));
        writer.writeAll(linhas);
        writer.close();
    }
}
