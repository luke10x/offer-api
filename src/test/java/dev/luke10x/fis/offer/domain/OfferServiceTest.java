package dev.luke10x.fis.offer.domain;

import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.domain.model.Offer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OfferServiceTest {

    @Mock
    OfferRepository offerRepository;

    @Test
    void putsNewOfferToRepository() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var t1 = Instant.now();
        var t2 = t1.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(t1, t2);

        var service = new OfferService(offerRepository);
        service.createOffer(offerId, description, price, duration);

        verify(offerRepository).putOffer(
                argThat(
                    actualOfferId -> actualOfferId.equals(offerId)
                ),
                argThat(
                        actualOffer -> actualOffer.getOfferId().equals(offerId)
                                && actualOffer.getDescription().equals(description)
                                && actualOffer.getPrice().equals(price)
                                && actualOffer.getDuration().equals(duration)
                                && !actualOffer.isCancelled()

                )
        );
    }

    @Test
    void getsOfferFromRepositoryById() {
        var offerId = UUID.randomUUID();

        var service = new OfferService(offerRepository);
        var createdOffer = service.getOffer(offerId);

        verify(offerRepository).getOffer(offerId);
    }

    @Test
    void getsOfferReturnedFromRepository() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var t1 = Instant.now();
        var t2 = t1.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(t1, t2);
        var offerFromRepo = new Offer(offerId, description, price, duration);
        when(offerRepository.getOffer(any())).thenReturn(offerFromRepo);

        var service = new OfferService(offerRepository);
        var offer = service.getOffer(offerId);

        assertEquals(offerFromRepo, offer);
    }

    @Test
    void getsOfferFromRepoCacelsItAndPutsBack() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var t1 = Instant.now();
        var t2 = t1.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(t1, t2);
        var offerFromRepo = new Offer(offerId, description, price, duration);
        when(offerRepository.getOffer(any())).thenReturn(offerFromRepo);
        var t3 = Instant.now();
        assertFalse(offerFromRepo.isCancelled());

        var service = new OfferService(offerRepository);
        service.cancelOffer(offerId, t3);

        verify(offerRepository).putOffer(
                argThat(
                        actualOfferId -> actualOfferId.equals(offerId)
                ),
                argThat(
                        actualOffer -> actualOffer.equals(offerFromRepo) && actualOffer.isCancelled()
                )
        );
    }
}