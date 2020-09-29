package dev.luke10x.fis.offer.domain.command;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class CancelOfferCommand {
    private UUID offerId;
    private Instant cancellationTime;

    public CancelOfferCommand(UUID offerId, Instant cancellationTime) {
        this.offerId = offerId;
        this.cancellationTime = cancellationTime;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Instant getCancellationTime() {
        return cancellationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancelOfferCommand that = (CancelOfferCommand) o;
        return offerId.equals(that.offerId) &&
                cancellationTime.equals(that.cancellationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, cancellationTime);
    }
}
