package dev.luke10x.fis.offer.domain.query;

import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OfferProjectionTest {

    @Mock
    OfferReadRepository offerReadRepository;

    @Test
    void getsOfferFromRepositoryById() {
        var offerId = UUID.randomUUID();

        var projection = new OfferProjection(offerReadRepository);
        var offer = projection.handleSingleOfferQuery(new SingleOfferQuery(offerId));

        verify(offerReadRepository).getOffer(offerId);
    }

    @Test
    void getsOfferReturnedFromRepository() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offerFromRepo = new OfferSnapshot(offerId, description, price, start, duration, true);
        when(offerReadRepository.getOffer(any())).thenReturn(offerFromRepo);

        var projection = new OfferProjection(offerReadRepository);
        var offer = projection.handleSingleOfferQuery(new SingleOfferQuery(offerId));

        assertEquals(offerFromRepo, offer);
    }
}