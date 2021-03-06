package dev.luke10x.fis.offer.domain.model;

import dev.luke10x.fis.offer.domain.event.Event;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class Offer {
    private UUID offerId;
    private Description description;
    private Money price;
    private Instant start;
    private Duration duration;
    private Boolean cancelled;

    public Offer(UUID offerId, Description description, Money price, Instant start, Duration duration) {
        this.offerId = offerId;
        this.description = description;
        this.price = price;
        this.start = start;
        this.duration = duration;
        cancelled = false;
    }

    public void cancel(Instant cancellationTime) {
        var end = start.plus(duration);
        if (cancellationTime.isAfter(end))
            throw new UnsupportedOperationException("Cannot cancel expired offer");
        cancelled = true;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Description getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public Instant getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public Boolean isExpiredOn(Instant now) {
        var end = start.plus(duration);
        return now.isAfter(end);
    }

    public boolean isActiveOn(Instant now) {
        if (cancelled)
            return false;
        var end = start.plus(duration);
        return now.isAfter(start) && now.isBefore(end);
    }
}
