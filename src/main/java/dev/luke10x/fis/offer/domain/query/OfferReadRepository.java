package dev.luke10x.fis.offer.domain.query;

import java.util.UUID;

public interface OfferReadRepository {
    void putOffer(UUID offerId, OfferSnapshot offer);
    OfferSnapshot getOffer(UUID offerId);
}
