package dev.luke10x.fis.offer.domain.query;

import java.util.Objects;
import java.util.UUID;

public class SingleOfferQuery {
    private final UUID offerId;

    public SingleOfferQuery(UUID offerId) {
        this.offerId = offerId;
    }

    public UUID getOfferId() {
        return offerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleOfferQuery that = (SingleOfferQuery) o;
        return Objects.equals(offerId, that.offerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId);
    }
}
