package com.mb.product;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

public class JsonProductInventory implements ProductInventory {
    
    private final Logger logger = Logger.getLogger(JsonProductFileReader.class.getName());

    private JsonProductFileReader reader;

    public JsonProductInventory(final String fileName) throws FileNotFoundException {
        reader = new JsonProductFileReader(fileName);
    }

    public JsonProductInventory(final File file) throws FileNotFoundException {
        reader = new JsonProductFileReader(file);
    }
    
    public JsonProductInventory() throws FileNotFoundException {
        reader = new JsonProductFileReader(JsonProductFileReader.PRODUCT_FILE_NAME);
    }

    public Set<Product> getItems() {
        try {
            return reader.read();
        } catch (ParseException e) {
            logger.log(Level.WARNING, "Could not read json file");
        }
        
        return Collections.emptySet();
    }

}