package dev.luke10x.fis.offer.domain;

import dev.luke10x.fis.offer.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class OfferService {
    private OfferRepository repository;
    public OfferService(OfferRepository repository) {
        this.repository = repository;
    }

    public void createOffer(UUID offerId, Description description, Money price, Instant start, Duration duration) {
        var offer = new Offer(offerId, description, price, start, duration);
        repository.putOffer(offerId, offer);
    }

    public void cancelOffer(UUID offerId, Instant now) {
        var offer = repository.getOffer(offerId);
        offer.cancel();
        repository.putOffer(offerId, offer);
    }

    public Offer getOffer(UUID offerId) {
        return repository.getOffer(offerId);
    }
}
