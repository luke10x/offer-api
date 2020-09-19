package dev.luke10x.fis.offer.domain;

import dev.luke10x.fis.offer.domain.model.Offer;

import java.util.UUID;

public interface OfferRepository {
    public void putOffer(UUID offerId, Offer offer);
    public Offer getOffer(UUID offerId);
}
