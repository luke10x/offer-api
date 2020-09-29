package dev.luke10x.fis.offer.domain.command;

import dev.luke10x.fis.offer.domain.model.Offer;

import java.util.UUID;

public interface OfferWriteRepository {
    void putOffer(UUID offerId, Offer offer);
    Offer getOffer(UUID offerId);
}
