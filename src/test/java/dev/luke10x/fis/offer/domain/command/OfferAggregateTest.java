package dev.luke10x.fis.offer.domain.command;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OfferAggregateTest {

    @Mock
    OfferWriteRepository offerWriteRepository;

    @Test
    void putsNewOfferToRepository() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);

        var aggregate = new OfferAggregate(offerWriteRepository);
        aggregate.handleCreateOfferCommand(new CreateOfferCommand(offerId, description, price, start, duration));

        verify(offerWriteRepository).putOffer(
                argThat(
                    actualOfferId -> actualOfferId.equals(offerId)
                ),
                argThat(
                        actualOffer -> actualOffer.getOfferId().equals(offerId)
                                && actualOffer.getDescription().equals(description)
                                && actualOffer.getPrice().equals(price)
                                && actualOffer.getStart().equals(start)
                                && actualOffer.getDuration().equals(duration)
                                && !actualOffer.isCancelled()
                )
        );
    }

    @Test
    void getsOfferFromRepoCancelsItAndPutsBack() {
        var offerId = UUID.randomUUID();
        var description = Description.from("New offer");
        var price = Money.from("USD", 100000);
        var start = Instant.now();
        var end = start.plus(2, ChronoUnit.DAYS);
        var duration = Duration.between(start, end);
        var offerFromRepo = new Offer(offerId, description, price, start, duration);
        when(offerWriteRepository.getOffer(any())).thenReturn(offerFromRepo);
        var cancellationTime = Instant.now();
        assertFalse(offerFromRepo.isCancelled());

        var aggregate = new OfferAggregate(offerWriteRepository);
        aggregate.handleCancelOfferCommand(new CancelOfferCommand(offerId, cancellationTime));

        verify(offerWriteRepository).putOffer(
                argThat(
                        actualOfferId -> actualOfferId.equals(offerId)
                ),
                argThat(
                        actualOffer -> actualOffer.equals(offerFromRepo) && actualOffer.isCancelled()
                )
        );
    }
}