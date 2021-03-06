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
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

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
        var start = Instant.now();
        var cancellation = start.plus(1, ChronoUnit.DAYS);
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

        offer.cancel(cancellation);

        assertTrue(offer.isCancelled());
    }

    @Test
    void cancelExpiredOffer() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var cancellation = start.plus(3, ChronoUnit.DAYS);
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

        Exception exception = assertThrows(Exception.class, () -> {
            offer.cancel(cancellation);
        });

        assertTrue(exception.getMessage().contains("Cannot cancel expired offer"));
        assertFalse(offer.isCancelled());
    }

    @Test
    void offerIsExpiredAfterItsEndDate() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

        var before = start.minus(1, ChronoUnit.DAYS);
        var during = start.plus(1, ChronoUnit.DAYS);
        var after = start.plus(3, ChronoUnit.DAYS);

        assertFalse(offer.isExpiredOn(before));
        assertFalse(offer.isExpiredOn(during));
        assertTrue(offer.isExpiredOn(after));
    }

    @Test
    void offerIsActiveBetweenItsStartAndEndDates() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

        var before = start.minus(1, ChronoUnit.DAYS);
        var during = start.plus(1, ChronoUnit.DAYS);
        var after = start.plus(3, ChronoUnit.DAYS);

        assertFalse(offer.isActiveOn(before));
        assertTrue(offer.isActiveOn(during));
        assertFalse(offer.isActiveOn(after));
    }

    @Test
    void offerIsNotActiveWhenItIsCancelled() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offer = new Offer(offerId, description, price, start, duration);

        var now = start.plus(1, ChronoUnit.DAYS);
        offer.cancel(now);

        assertTrue(offer.isCancelled());
        assertFalse(offer.isActiveOn(now));
    }
}