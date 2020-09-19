package dev.luke10x.fis.offer.rest;

import dev.luke10x.fis.offer.domain.OfferService;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.rest.request.CreateOfferDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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
        controller.create(new CreateOfferDTO("New Offer", "USD", 1000, now, 60 * 60 * 24));

        verify(service).createOffer(
                uuid,
                Description.from("New Offer"),
                Money.from("USD", 1000),
                now,
                duration
        );
    }
}