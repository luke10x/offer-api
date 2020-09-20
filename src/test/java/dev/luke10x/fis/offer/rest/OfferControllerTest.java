package dev.luke10x.fis.offer.rest;

import dev.luke10x.fis.offer.domain.OfferService;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.domain.model.Offer;
import dev.luke10x.fis.offer.rest.request.CreateOfferDTO;
import dev.luke10x.fis.offer.rest.response.OfferDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    OfferService service;

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

        var controller = new OfferController(service, uuidProvider, timeProvider);
        var response = controller.create(new CreateOfferDTO("New Offer", "USD", 1000, now, 60 * 60 * 24));

        verify(service).createOffer(
                uuid,
                Description.from("New Offer"),
                Money.from("USD", 1000),
                now,
                duration
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
        var offerFromService = new Offer(offerId, description, price, start, duration);

        when(service.getOffer(offerId)).thenReturn(offerFromService);

        var controller = new OfferController(service, uuidProvider, timeProvider);
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
        var offerFromService = new Offer(offerId, description, price, start, duration);

        when(service.getOffer(offerId)).thenReturn(offerFromService);

        var controller = new OfferController(service, uuidProvider, timeProvider);
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
        var offerFromService = new Offer(offerId, description, price, start, duration);
        offerFromService.cancel(start);

        when(service.getOffer(offerId)).thenReturn(offerFromService);

        var controller = new OfferController(service, uuidProvider, timeProvider);
        var response = controller.fetch(offerId);

        var offerResponse = response.getBody();
        assertTrue(offerResponse.getExpired());
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertTrue(offerResponse.getCancelled());
        assertFalse(offerResponse.getActive());
    }
}