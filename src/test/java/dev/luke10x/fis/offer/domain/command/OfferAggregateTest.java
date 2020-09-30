package dev.luke10x.fis.offer.domain.command;

import dev.luke10x.fis.offer.domain.event.OfferCancelledEvent;
import dev.luke10x.fis.offer.domain.event.OfferCreatedEvent;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.persistence.InMemoryEventStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OfferAggregateTest {

    @Mock
    InMemoryEventStore offerWriteRepository;

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

        verify(offerWriteRepository).addEvent(
                argThat(
                    actualOfferId -> actualOfferId.equals(offerId)
                ),
                argThat(
                        event -> event instanceof OfferCreatedEvent
                                && ((OfferCreatedEvent) event).getOfferId().equals(offerId)
                                && ((OfferCreatedEvent) event).getDescription().equals(description)
                                && ((OfferCreatedEvent) event).getPrice().equals(price)
                                && ((OfferCreatedEvent) event).getStart().equals(start)
                                && ((OfferCreatedEvent) event).getDuration().equals(duration)
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
        var event = new OfferCreatedEvent(offerId, description, price, start, duration);
        when(offerWriteRepository.getOfferEvents(any())).thenReturn(Arrays.asList(event));
        var cancellationTime = Instant.now();

        var aggregate = new OfferAggregate(offerWriteRepository);
        aggregate.handleCancelOfferCommand(new CancelOfferCommand(offerId, cancellationTime));

        verify(offerWriteRepository).addEvent(
                argThat(actualOfferId -> actualOfferId.equals(offerId)),
                argThat(actualEvent -> actualEvent instanceof OfferCancelledEvent)
        );
    }
}