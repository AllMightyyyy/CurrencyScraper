package org.example.analyzer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyAnalyzer {

    private final List<Currency> currencies;

    public CurrencyAnalyzer(List<Currency> currencies) {
        this.currencies = currencies;
    }

    // Find the currency pair with the highest price
    public Currency highestPriceCurrency() {
        return currencies.stream()
                .max(Comparator.comparingDouble(Currency::getPrice))
                .orElse(null);
    }

    // Find the currency pair with the lowest price
    public Currency lowestPriceCurrency() {
        return currencies.stream()
                .min(Comparator.comparingDouble(Currency::getPrice))
                .orElse(null);
    }

    // Calculate the average price of all currency pairs
    public double averagePrice() {
        return currencies.stream()
                .mapToDouble(Currency::getPrice)
                .average()
                .orElse(0.0);
    }

    // Calculate the average change percentage of all currencies
    public double averageChangePercent() {
        return currencies.stream()
                .mapToDouble(Currency::getChangePercent)
                .average()
                .orElse(0.0);
    }

    // Find the most volatile currencies (by change percentage)
    public List<Currency> topVolatileCurrencies(int count) {
        return currencies.stream()
                .sorted(Comparator.comparingDouble(Currency::getChangePercent).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    // Calculate cumulative market performance (sum of all percentage changes)
    public double cumulativeMarketPerformance() {
        return currencies.stream()
                .mapToDouble(Currency::getChangePercent)
                .sum();
    }

    // Classify currencies by price range (low, mid, high)
    public Map<String, Long> classifyByPriceRange(double lowRange, double midRange, double highRange) {
        long lowPriceCount = currencies.stream()
                .filter(currency -> currency.getPrice() <= lowRange)
                .count();

        long midPriceCount = currencies.stream()
                .filter(currency -> currency.getPrice() > lowRange && currency.getPrice() <= midRange)
                .count();

        long highPriceCount = currencies.stream()
                .filter(currency -> currency.getPrice() > midRange)
                .count();

        return Map.of(
                "Low Price", lowPriceCount,
                "Mid Price", midPriceCount,
                "High Price", highPriceCount
        );
    }

    // Find the currencies with the highest positive change
    public List<Currency> topGainers(int count) {
        return currencies.stream()
                .filter(currency -> currency.getChangePercent() > 0)
                .sorted(Comparator.comparingDouble(Currency::getChangePercent).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    // Find the currencies with the largest losses (negative change)
    public List<Currency> topLosers(int count) {
        return currencies.stream()
                .filter(currency -> currency.getChangePercent() < 0)
                .sorted(Comparator.comparingDouble(Currency::getChangePercent))
                .limit(count)
                .collect(Collectors.toList());
    }
}
