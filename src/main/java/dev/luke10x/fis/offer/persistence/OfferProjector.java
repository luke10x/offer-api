package dev.luke10x.fis.offer.persistence;

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

    public void project(Offer offer) {
        var snapshot = new OfferSnapshot(
                offer.getOfferId(),
                offer.getDescription(),
                offer.getPrice(),
                offer.getStart(),
                offer.getDuration(),
                offer.isCancelled()
        );
        readRepository.putOffer(offer.getOfferId(), snapshot);
    }
}
