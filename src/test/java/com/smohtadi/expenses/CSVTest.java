//package com.smohtadi.expenses;
//
//import com.opencsv.CSVReader;
//import org.junit.jupiter.api.Test;
//
//import java.io.Reader;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CSVTest {
//    @Test
//    public void shouldReturnTrue() {
//
//        List<String[]> list = new ArrayList<>();
//        try (Reader reader = Files.newBufferedReader(filePath)) {
//            try (CSVReader csvReader = new CSVReader(reader)) {
//                String[] line;
//                while ((line = csvReader.readNext()) != null) {
//                    list.add(line);
//                }
//            }
//        }
//        assertEquals(2, 1 + 1);
//    }
//
//}
