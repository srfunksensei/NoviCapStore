package com.mb.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonProductFileReader {

    private final Logger logger = Logger.getLogger(JsonProductFileReader.class.getName());

    public static final String PRODUCT_FILE_NAME = "json/products.json";

    private final Reader reader;

    public JsonProductFileReader(final String fileName) throws FileNotFoundException {
        if (fileName == null || !fileName.endsWith(".json")) {
            throw new IllegalArgumentException();
        }

        final InputStream in = accessFile(fileName);
        if (in == null) {
            throw new FileNotFoundException();
        }
        
        File tempFile;
        try {
            tempFile = File.createTempFile("products", null);
            tempFile.deleteOnExit();
            
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not create or copy temp file " + e.getMessage());
            throw new FileNotFoundException();
        }
        
        this.reader = new FileReader(tempFile);
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
    
    private InputStream accessFile(final String resource) {
        // this is the path within the jar file
        InputStream input = JsonProductInventory.class.getResourceAsStream(resource);
        if (input == null) {
            input = JsonProductInventory.class.getClassLoader().getResourceAsStream(resource);
        }

        return input;
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

        return new HashSet<>(products);
    }
}
