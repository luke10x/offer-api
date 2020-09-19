package dev.luke10x.fis.offer.domain.model;

import java.time.Duration;
import java.util.UUID;

public class Offer {
    private UUID offerId;
    private Description description;
    private Money price;
    private Duration duration;
    private Boolean cancelled;

    public Offer(UUID offerId, Description description, Money price, Duration duration) {
        this.offerId = offerId;
        this.description = description;
        this.price = price;
        this.duration = duration;
        cancelled = false;
    }

    public void cancel() {
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

    public Duration getDuration() {
        return duration;
    }

    public Boolean isCancelled() {
        return cancelled;
    }
}
