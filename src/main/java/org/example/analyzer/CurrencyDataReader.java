package org.example.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDataReader {

    public static List<Currency> readCurrencyData(String filePath) throws IOException {
        List<Currency> currencies = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // Skip the header row

            while ((line = reader.readNext()) != null) {
                String currencyPair = line[0].trim();

                // Remove commas and parse to double
                double price = Double.parseDouble(line[1].replace(",", "").trim());
                double change = Double.parseDouble(line[2].replace(",", "").trim());
                double changePercent = Double.parseDouble(line[3].replace(",", "").replace("%", "").trim());
                String timestamp = line[4].trim();

                currencies.add(new Currency(currencyPair, price, change, changePercent, timestamp));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }
}
