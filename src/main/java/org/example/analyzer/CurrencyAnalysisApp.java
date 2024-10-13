package org.example.analyzer;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;

public class CurrencyAnalysisApp {

    public static void main(String[] args) {
        try {
            List<Currency> currencies = CurrencyDataReader.readCurrencyData("currency_data_2024-10-13_12-44.csv");

            CurrencyAnalyzer analyzer = new CurrencyAnalyzer(currencies);

            Currency highestPrice = analyzer.highestPriceCurrency();
            System.out.println("Highest Price Currency: " + highestPrice.getCurrencyPair() + " - $" + highestPrice.getPrice());

            Currency lowestPrice = analyzer.lowestPriceCurrency();
            System.out.println("Lowest Price Currency: " + lowestPrice.getCurrencyPair() + " - $" + lowestPrice.getPrice());

            System.out.println("Average Price: $" + analyzer.averagePrice());
            System.out.println("Average Change Percent: " + analyzer.averageChangePercent() + "%");
            System.out.println("Cumulative Market Performance: " + analyzer.cumulativeMarketPerformance() + "%");

            System.out.println("\nTop 5 Volatile Currencies:");
            analyzer.topVolatileCurrencies(5).forEach(currency ->
                    System.out.println(currency.getCurrencyPair() + " - " + currency.getChangePercent() + "%"));

            System.out.println("\nTop 5 Gainers:");
            analyzer.topGainers(5).forEach(currency ->
                    System.out.println(currency.getCurrencyPair() + " - " + currency.getChangePercent() + "%"));

            System.out.println("\nTop 5 Losers:");
            analyzer.topLosers(5).forEach(currency ->
                    System.out.println(currency.getCurrencyPair() + " - " + currency.getChangePercent() + "%"));

            System.out.println("\nPrice Range Distribution:");
            analyzer.classifyByPriceRange(1.0, 10.0, 50.0).forEach((range, count) ->
                    System.out.println(range + ": " + count + " currencies"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
