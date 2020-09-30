package dev.luke10x.fis.offer.domain.event;

import java.time.Instant;
import java.util.UUID;

public class OfferCancelledEvent extends Event {
    private UUID offerId;
    private Instant cancellationTime;

    public OfferCancelledEvent(UUID offerId, Instant cancellationTime) {
        this.offerId = offerId;
        this.cancellationTime = cancellationTime;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Instant getCancellationTime() {
        return cancellationTime;
    }
}
