package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.model.Offer;
import dev.luke10x.fis.offer.domain.query.OfferReadRepository;
import dev.luke10x.fis.offer.domain.query.OfferSnapshot;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InMemoryOfferReadRepository implements OfferReadRepository {
    private Map<UUID, OfferSnapshot> store = new HashMap<>();

    public void putOffer(UUID offerId, OfferSnapshot offer) {
        store.put(offerId, offer);
    }

    public OfferSnapshot getOffer(UUID offerId) {
        return store.get(offerId);
    }
}
