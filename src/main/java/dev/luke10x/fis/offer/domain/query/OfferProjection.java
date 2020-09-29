package dev.luke10x.fis.offer.domain.query;

import org.springframework.stereotype.Component;

@Component
public class OfferProjection {
    private OfferReadRepository repository;

    public OfferProjection(OfferReadRepository repository) {
        this.repository = repository;
    }

    public OfferSnapshot handleSingleOfferQuery(SingleOfferQuery query) {
        return repository.getOffer(query.getOfferId());
    }
}
