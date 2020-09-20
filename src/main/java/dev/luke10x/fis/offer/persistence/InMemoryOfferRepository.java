package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.OfferRepository;
import dev.luke10x.fis.offer.domain.model.Offer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InMemoryOfferRepository implements OfferRepository {
    private Map<UUID, Offer> store = new HashMap<>();

    public void putOffer(UUID offerId, Offer offer) {
        store.put(offerId, offer);
    }

    public Offer getOffer(UUID offerId) {
        return store.get(offerId);
    }
}
