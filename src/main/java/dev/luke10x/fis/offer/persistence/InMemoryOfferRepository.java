package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.model.Offer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryOfferRepository {
    private Map<UUID, Offer> store = new HashMap<>();

    public void addOffer(UUID offerId, Offer offer) {
        store.put(offerId, offer);
    }

    public Offer getOffer(UUID offerId) {
        return store.get(offerId);
    }
}
