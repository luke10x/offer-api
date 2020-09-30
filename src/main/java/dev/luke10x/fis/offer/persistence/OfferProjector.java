package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.event.Event;
import dev.luke10x.fis.offer.domain.event.OfferCancelledEvent;
import dev.luke10x.fis.offer.domain.event.OfferCreatedEvent;
import dev.luke10x.fis.offer.domain.model.Offer;
import dev.luke10x.fis.offer.domain.query.OfferReadRepository;
import dev.luke10x.fis.offer.domain.query.OfferSnapshot;
import org.springframework.stereotype.Component;

@Component
public class OfferProjector {
    OfferReadRepository readRepository;
    public OfferProjector(OfferReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    public void project(Event event) {
        if (event instanceof OfferCreatedEvent) {
            var snapshot = new OfferSnapshot(
                    ((OfferCreatedEvent)event).getOfferId(),
                    ((OfferCreatedEvent)event).getDescription(),
                    ((OfferCreatedEvent)event).getPrice(),
                    ((OfferCreatedEvent)event).getStart(),
                    ((OfferCreatedEvent)event).getDuration(),
                    false
            );
            readRepository.putOffer(((OfferCreatedEvent)event).getOfferId(), snapshot);
        }

        if (event instanceof OfferCancelledEvent) {
            var offerId = ((OfferCancelledEvent)event).getOfferId();
            var cancellationTime = ((OfferCancelledEvent)event).getCancellationTime();
            var snapshot = readRepository.getOffer(offerId);
            snapshot.setCancelled(true);
            readRepository.putOffer(((OfferCancelledEvent)event).getOfferId(), snapshot);
        }
    }
}
