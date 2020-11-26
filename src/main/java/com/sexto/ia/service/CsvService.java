package com.sexto.ia.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class CsvService {
    private static final String movieFilePath = "src/main/resources/movies.csv";
    private static final String ratingFilePath = "src/main/resources/ratings.csv";
    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);
    public static CSVReader readMovieFileCSVReader() throws FileNotFoundException {
        return new CSVReader(new FileReader(movieFilePath));
    }

    public static File readRatingFile(){
        return new File(ratingFilePath);
    }

    public static void writeNewRows(File arquivo, List<String[]> linhas) throws IOException {
        logger.info(String.format("Escrevendo %d novas linhas no arquivo", linhas.size()));
        CSVWriter writer = new CSVWriter(new FileWriter(arquivo, true),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        writer.writeAll(linhas);
        writer.close();
    }

    public static String[] getLastRowFromRatingFile() throws IOException {
        File arquivo = CsvService.readRatingFile();
        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        String last = "";
        String row = "";
        while ((row = br.readLine()) != null) {
            last = row;
        }
        return last.split(",");
    }
}
