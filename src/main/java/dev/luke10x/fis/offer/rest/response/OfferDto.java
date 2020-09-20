package dev.luke10x.fis.offer.rest.response;

import java.time.Instant;
import java.util.UUID;

public class OfferDto {
    private UUID offerId;
    private String description;
    private String currency;
    private Integer amountInMinorUnits;
    private Instant start;
    private long durationInSeconds;
    private Boolean expired;
    private Boolean cancelled;
    private Boolean active;

    public OfferDto(UUID offerId,
                    String description,
                    String currency,
                    Integer amountInMinorUnits,
                    Instant start,
                    long durationInSeconds,
                    Boolean expired,
                    Boolean cancelled,
                    Boolean active) {
        this.offerId = offerId;
        this.description = description;
        this.currency = currency;
        this.amountInMinorUnits = amountInMinorUnits;
        this.start = start;
        this.durationInSeconds = durationInSeconds;
        this.expired = expired;
        this.cancelled = cancelled;
        this.active = active;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getAmountInMinorUnits() {
        return amountInMinorUnits;
    }

    public Instant getStart() {
        return start;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public Boolean getExpired() {
        return expired;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public Boolean getActive() {
        return active;
    }
}
