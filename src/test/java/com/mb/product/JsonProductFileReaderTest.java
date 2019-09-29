package com.mb.product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Set;

import org.assertj.core.util.Files;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class JsonProductFileReaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExtensionFileName() {
        try {
            new JsonProductInventory("test.txt");
        } catch (FileNotFoundException e) {
            Assert.fail("expected IllegalArgumentException, but instead inventory was created");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testUnknownFileByFileName() throws FileNotFoundException {
        new JsonProductInventory("test.json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectory() {
        final File f = Files.newTemporaryFolder();
        try {
            new JsonProductInventory(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Assert.fail("expected IllegalArgumentException, but instead inventory was created");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoFileExtension() {
        final File f = Files.newTemporaryFile();
        try {
            new JsonProductInventory(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Assert.fail("expected IllegalArgumentException, but instead inventory was created");
        }
    }

    private static final String TEST_JSON_FILE_NAME = "test.json";

    private File createNewFileInTempDir(final String file) {
        final File dir = Files.newTemporaryFolder();
        return Files.newFile(dir.getPath() + "/" + file);
    }

    @Test
    public void testEmptyFileRead() {
        final File f = createNewFileInTempDir(JsonProductFileReaderTest.TEST_JSON_FILE_NAME);

        try {
            final JsonProductFileReader reader = new JsonProductFileReader(f);
            reader.read();
        } catch (FileNotFoundException e) {
            Assert.fail("Expected to create new reader");
        } catch (ParseException e) {
            // expected behavior
        }
    }

    @Test
    public void testEmptyProductList() {
        final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[]");

            writer.write(sb.toString());
        } catch (Exception e1) {
            Assert.fail("Expected to write data to file");
        }

        try {
            final JsonProductFileReader reader = new JsonProductFileReader(f);
            final Set<Product> locations = reader.read();

            Assert.assertEquals(0, locations.size());
        } catch (IOException | ParseException e) {
            Assert.fail("Expected to read file");
        }
    }

    @Test
    public void testSuccessfulRead() {
        final Product expected = new Product("VOUCHER", "NoviCap Voucher", new BigDecimal(5));

        final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[").append("\n") //
                    .append("{").append("\n") //
                    .append("\"code\": \"").append(expected.getCode()).append("\"\n") //
                    .append("\"name\": \"").append(expected.getName()).append("\"\n") //
                    .append("\"price\":").append(expected.getPrice().toString()).append("\n") //
                    .append("}").append("\n") //
                    .append("]");

            writer.write(sb.toString());
        } catch (Exception e1) {
            Assert.fail("Expected to write data to file");
        }

        try {
            final JsonProductFileReader reader = new JsonProductFileReader(f);
            final Set<Product> items = reader.read();

             Assert.assertEquals(1, items.size());
             
             Product first = items.stream().findFirst().get();
             Assert.assertEquals(expected.getCode(), first.getCode());
             Assert.assertEquals(expected.getName(), first.getName());
             Assert.assertEquals(expected.getPrice(), first.getPrice());
        } catch (IOException | ParseException e) {
            Assert.fail("Expected to read file");
        }
    }

}
