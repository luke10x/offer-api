package dev.luke10x.fis.offer.rest.request;

import java.time.Duration;
import java.time.Instant;

public class CreateOfferDTO {
    private String description;
    private Instant start;
    private long durationInSeconds;
    private String currency;
    private Integer amountInMinorUnits;

    public CreateOfferDTO(
            String description,
            String currency,
            Integer amountInMinorUnits,
            Instant start,
            long durationInSeconds
    ) {
        this.description = description;
        this.start = start;
        this.durationInSeconds = durationInSeconds;
        this.currency = currency;
        this.amountInMinorUnits = amountInMinorUnits;
    }

    public String getDescription() {
        return description;
    }

    public Instant getStart() {
        return start;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getAmountInMinorUnits() {
        return amountInMinorUnits;
    }
}
