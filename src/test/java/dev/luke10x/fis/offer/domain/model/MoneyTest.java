package dev.luke10x.fis.offer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void printsNicely() {
        Money hundredDollars = Money.from("USD", 10000);
        assertEquals("USD 100.00", hundredDollars.toString());
    }

    @Test
    void failsWithUnsupportedCurrency() {
        Exception exception = assertThrows(Exception.class, () -> {
            Money.from("CNY", 10000);
        });

        assertEquals(exception.getMessage(), "Unsupported currency");
    }

    @Test
    void failsWithNegativeAmount() {
        Exception exception = assertThrows(Exception.class, () -> {
            Money.from("USD", -10000);
        });

        assertEquals(exception.getMessage(), "Invalid amount of money");
    }
}