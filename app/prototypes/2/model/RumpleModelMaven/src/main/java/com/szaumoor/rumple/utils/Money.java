package com.szaumoor.rumple.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.szaumoor.rumple.model.entities.Bill;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public enum Money {
    ;

    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance();


    public static boolean inRange(final BigDecimal quantity) {
        return quantity != null && quantity.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns a formatted String from a BigDecimal quantity for the passed currency.
     *
     * @param quantity The BigDecimal value to be formatted
     * @return String containing the formatted amount from the passed value according to the passed
     * currency
     */
    public static String formatCurrency(final BigDecimal quantity, final Currency currency) {
        FORMATTER.setCurrency(currency);
        FORMATTER.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        return FORMATTER.format(quantity);
    }

    public static List<Currency> currenciesSortedByCode() {
        return Currency.getAvailableCurrencies()
                .stream()
                .sorted(Comparator.comparing(Currency::toString))
                .collect(Collectors.toList());
    }


    private static JsonObject getRates(final Currency currency) throws IOException {
        String connectionString = String.format("https://v6.exchangerate-api.com/v6/2edaed81acea162e6e4668ef/latest/%s", currency.getCurrencyCode());
        URL url = new URL(connectionString);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObj = root.getAsJsonObject();
        return jsonObj.get("conversion_rates").getAsJsonObject();
    }

    public static BigDecimal convert(final BigDecimal amount, final Currency from, final Currency to) throws IOException {
        return amount.multiply(getRates(from).get(to.getCurrencyCode()).getAsBigDecimal());
    }

    private static BigDecimal convert(final BigDecimal amount, final JsonObject rates, final Currency to) throws IOException {
        return amount.multiply(rates.get(to.getCurrencyCode()).getAsBigDecimal());
    }

    public static BigDecimal sumIntoCurrency(final Currency curr, final List<Bill> bills) {
        JsonObject rates = null;
        try {
            rates = getRates(curr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject finalRates = rates;
        return bills.stream()
                .map(b -> {
                    Currency currency = b.getCurrency();
                    BigDecimal total = b.getTotal();
                    try {
                        return convert(total, finalRates, currency);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
