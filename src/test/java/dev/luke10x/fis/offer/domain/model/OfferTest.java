package dev.luke10x.fis.offer.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void canCreateOffer() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var t1 = Instant.now();
        var t2 = t1.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(t1, t2);
        var offer = new Offer(offerId, description, price, duration);

        assertEquals(offerId, offer.getOfferId());
        assertEquals("New offer", offer.getDescription().getValue());
        assertEquals(price, offer.getPrice());
        assertEquals(duration, offer.getDuration());
        assertFalse(offer.isCancelled());
    }


    @Test
    void cancelOffer() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var t1 = Instant.now();
        var t2 = t1.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(t1, t2);
        var offer = new Offer(offerId, description, price, duration);

        offer.cancel();

        assertTrue(offer.isCancelled());
    }
}