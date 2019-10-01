package com.mb.product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProductFileReader {

    private final Logger logger = Logger.getLogger(JsonProductFileReader.class.getName());

    public static final String PRODUCT_FILE_NAME = "json/products.json";

    private Reader reader;

    public JsonProductFileReader(final String fileName) throws FileNotFoundException {
        if (fileName == null || !fileName.endsWith(".json")) {
            throw new IllegalArgumentException();
        }

        this.reader = new FileReader(fileName);
    }

    public JsonProductFileReader(final File file) throws FileNotFoundException {
        if (file == null || !file.isFile() || !file.getName().endsWith("json")) {
            throw new IllegalArgumentException();
        }

        this.reader = new FileReader(file);
    }

    public Set<Product> read() throws ParseException {
        final String json = parseJsonFile();
        return parseJsonString(json);
    }

    private String parseJsonFile() throws ParseException {
        try {
            final JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(reader);

            return data.toJSONString();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not parse json file " + e.getMessage());
        }

        return "";
    }

    private Set<Product> parseJsonString(final String json) {
        final ObjectMapper mapper = new ObjectMapper();

        List<Product> products = new ArrayList<>();
        try {
            products = Arrays.asList(mapper.readValue(json, Product[].class));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not parse json object: " + e.getMessage());
        }

        return products.stream().collect(Collectors.toSet());
    }
}
