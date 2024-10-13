package org.example.analyzer;

public class Currency {
    private final String currencyPair;
    private final double price;
    private final double change;
    private final double changePercent;
    private final String timestamp;

    public Currency(String currencyPair, double price, double change, double changePercent, String timestamp) {
        this.currencyPair = currencyPair;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.timestamp = timestamp;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
