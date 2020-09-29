package dev.luke10x.fis.offer.rest;

import dev.luke10x.fis.offer.domain.command.CancelOfferCommand;
import dev.luke10x.fis.offer.domain.command.CreateOfferCommand;
import dev.luke10x.fis.offer.domain.command.OfferAggregate;
import dev.luke10x.fis.offer.domain.query.OfferProjection;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.domain.query.OfferSnapshot;
import dev.luke10x.fis.offer.domain.query.SingleOfferQuery;
import dev.luke10x.fis.offer.rest.request.CreateOfferDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OfferControllerTest {

    @Mock
    OfferAggregate aggregate;

    @Mock
    OfferProjection projection;

    @Mock
    UUIDProvider uuidProvider;

    @Mock
    TimeProvider timeProvider;

    @Test
    void createCallsServiceWithProvidedUUIDAndTime() {
        var uuid = UUID.randomUUID();
        when(uuidProvider.randomUUID()).thenReturn(uuid);
        var now = Instant.now();
        when(timeProvider.now()).thenReturn(now);
        Duration duration = Duration.ofSeconds(60 * 60 * 24);

        var controller = new OfferController(aggregate, projection, uuidProvider, timeProvider);
        var response = controller.create(new CreateOfferDTO("New Offer", "USD", 1000, now, 60 * 60 * 24));

        verify(aggregate).handleCreateOfferCommand(
                new CreateOfferCommand(
                        uuid,
                        Description.from("New Offer"),
                        Money.from("USD", 1000),
                        now,
                        duration
                )
        );
        assertThat(response.getBody()).contains("Created: " + uuid.toString());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void fetchOffer() {
        var now = Instant.now();
        when(timeProvider.now()).thenReturn(now);
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = now.minus(2, ChronoUnit.HOURS);
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offerFromService = new OfferSnapshot(offerId, description, price, start, duration, false);

        when(projection.handleSingleOfferQuery(new SingleOfferQuery(offerId))).thenReturn(offerFromService);

        var controller = new OfferController(aggregate, projection, uuidProvider, timeProvider);
        var response = controller.fetch(offerId);

        var offerResponse = response.getBody();
        assertFalse(offerResponse.getExpired());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void fetchExpiredOffer() {
        var now = Instant.now();
        when(timeProvider.now()).thenReturn(now);
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = now.minus(3, ChronoUnit.DAYS);
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offerFromService = new OfferSnapshot(offerId, description, price, start, duration, false);

        when(projection.handleSingleOfferQuery(new SingleOfferQuery(offerId))).thenReturn(offerFromService);

        var controller = new OfferController(aggregate, projection, uuidProvider, timeProvider);
        var response = controller.fetch(offerId);

        var offerResponse = response.getBody();
        assertTrue(offerResponse.getExpired());
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertTrue(offerResponse.getExpired());
    }

    @Test
    void fetchCancelledOffer() {
        var now = Instant.now();
        when(timeProvider.now()).thenReturn(now);
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = now.minus(3, ChronoUnit.DAYS);
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offerFromService = new OfferSnapshot(offerId, description, price, start, duration, true);

        when(projection.handleSingleOfferQuery(new SingleOfferQuery(offerId))).thenReturn(offerFromService);

        var controller = new OfferController(aggregate, projection, uuidProvider, timeProvider);
        var response = controller.fetch(offerId);

        var offerResponse = response.getBody();
        assertTrue(offerResponse.getExpired());
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertTrue(offerResponse.getCancelled());
        assertFalse(offerResponse.getActive());
    }

    @Test
    void cancellOffer() {
        var offerId = UUID.randomUUID();
        var now = Instant.now();
        when(timeProvider.now()).thenReturn(now);

        var controller = new OfferController(aggregate, projection, uuidProvider, timeProvider);
        var response = controller.cancel(offerId);

        verify(aggregate).handleCancelOfferCommand(new CancelOfferCommand(offerId, now));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}