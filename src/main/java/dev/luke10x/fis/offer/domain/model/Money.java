package dev.luke10x.fis.offer.domain.model;

public class Money {
    private static enum Currency {
        EUR,
        GBP,
        USD
    }
    private Currency currency;
    private Integer minorUnits;

    private Money() {}

    public static Money from(String currency, Integer minorUnits) {

        if (minorUnits < 0) throw new IllegalArgumentException("Invalid amount of money");
        var money = new Money();
        try {
            money.currency = Currency.valueOf(currency);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unsupported currency");
        }
        money.minorUnits = minorUnits;
        return money;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String toString() {
        return this.currency + String.format(" %.2f", (double) this.minorUnits / 100);
    }
}