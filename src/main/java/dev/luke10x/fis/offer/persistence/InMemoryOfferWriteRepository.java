package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.command.OfferWriteRepository;
import dev.luke10x.fis.offer.domain.model.Offer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InMemoryOfferWriteRepository implements OfferWriteRepository {
    private final OfferProjector projector;
    private Map<UUID, Offer> store = new HashMap<>();

    public InMemoryOfferWriteRepository(OfferProjector projector) {
        this.projector = projector;
    }

    public void putOffer(UUID offerId, Offer offer) {
        store.put(offerId, offer);
        projector.project(offer);
    }

    public Offer getOffer(UUID offerId) {
        return store.get(offerId);
    }
}
