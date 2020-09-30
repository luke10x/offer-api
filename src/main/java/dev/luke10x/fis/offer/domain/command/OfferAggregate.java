package dev.luke10x.fis.offer.domain.command;

import dev.luke10x.fis.offer.domain.event.Event;
import dev.luke10x.fis.offer.domain.event.OfferCancelledEvent;
import dev.luke10x.fis.offer.domain.event.OfferCreatedEvent;
import dev.luke10x.fis.offer.domain.model.Offer;
import dev.luke10x.fis.offer.persistence.InMemoryEventStore;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class OfferAggregate {
    private InMemoryEventStore writeRepository;

    public OfferAggregate(InMemoryEventStore repository) {
        this.writeRepository = repository;
    }

    public List<Event> handleCreateOfferCommand(CreateOfferCommand command) {
        var offerId = command.getOfferId();
        var description = command.getDescription();
        var price = command.getPrice();
        var start = command.getStart();
        var duration = command.getDuration();

        var event = new OfferCreatedEvent(offerId, description, price, start, duration);
        writeRepository.addEvent(command.getOfferId(), event);

        return Arrays.asList(event);
    }

    public List<Event> handleCancelOfferCommand(CancelOfferCommand command) {
        Offer offer = OfferAggregate.recreateOfferState(writeRepository, command.getOfferId());

        offer.cancel(command.getCancellationTime());

        var event = new OfferCancelledEvent(command.getOfferId(), command.getCancellationTime());
        writeRepository.addEvent(command.getOfferId(), event);

        return Arrays.asList(event);
    }

    public static Offer apply(Offer offer, Event event) {
        if (event instanceof OfferCreatedEvent) {
            return new Offer(
                    ((OfferCreatedEvent)event).getOfferId(),
                    ((OfferCreatedEvent)event).getDescription(),
                    ((OfferCreatedEvent)event).getPrice(),
                    ((OfferCreatedEvent)event).getStart(),
                    ((OfferCreatedEvent)event).getDuration()
            );
        }
        if (event instanceof OfferCancelledEvent) {
            offer.cancel(((OfferCancelledEvent)event).getCancellationTime());
        }
        return offer;
    }

    public static Offer recreateOfferState(InMemoryEventStore writeRepository, UUID offerId) {
        var events = writeRepository.getOfferEvents(offerId);
        Offer offer = null;
        for (var event: events) {
            offer = apply(offer, event);
        }
        return offer;
    }
}
