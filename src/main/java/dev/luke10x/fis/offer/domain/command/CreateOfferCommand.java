package dev.luke10x.fis.offer.domain.command;

import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class CreateOfferCommand {
    private UUID offerId;
    private Description description;
    private Money price;
    private Instant start;
    private Duration duration;

    public CreateOfferCommand(UUID offerId, Description description, Money price, Instant start, Duration duration) {
        this.offerId = offerId;
        this.description = description;
        this.start = start;
        this.price = price;
        this.duration = duration;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Description getDescription() {
        return description;
    }

    public Instant getStart() {
        return start;
    }

    public Money getPrice() {
        return price;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOfferCommand that = (CreateOfferCommand) o;
        return offerId.equals(that.offerId) &&
                description.equals(that.description) &&
                price.equals(that.price) &&
                start.equals(that.start) &&
                duration.equals(that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offerId, description, price, start, duration);
    }
}
