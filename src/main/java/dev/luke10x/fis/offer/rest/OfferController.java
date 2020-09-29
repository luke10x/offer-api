package dev.luke10x.fis.offer.rest;

import dev.luke10x.fis.offer.domain.command.CancelOfferCommand;
import dev.luke10x.fis.offer.domain.command.CreateOfferCommand;
import dev.luke10x.fis.offer.domain.command.OfferAggregate;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
import dev.luke10x.fis.offer.domain.query.OfferProjection;
import dev.luke10x.fis.offer.domain.query.SingleOfferQuery;
import dev.luke10x.fis.offer.rest.request.CreateOfferDTO;
import dev.luke10x.fis.offer.rest.response.OfferDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@Controller
public class OfferController {
    private final OfferAggregate aggregate;
    private final OfferProjection projection;
    private final UUIDProvider uuidProvider;
    private final TimeProvider timeProvider;

    public OfferController(OfferAggregate aggregate, OfferProjection projection,  UUIDProvider uuidProvider, TimeProvider timeProvider) {
        this.aggregate = aggregate;
        this.projection = projection;
        this.uuidProvider = uuidProvider;
        this.timeProvider = timeProvider;
    }

    @PostMapping("/offers")
    public ResponseEntity<String> create(@RequestBody CreateOfferDTO dto) {
        var offerId = uuidProvider.randomUUID();
        var description = Description.from(dto.getDescription());
        var price = Money.from(dto.getCurrency(), dto.getAmountInMinorUnits());
        var start = timeProvider.now();
        var duration = Duration.ofSeconds(dto.getDurationInSeconds());

        var command = new CreateOfferCommand(offerId, description, price, start, duration);
        aggregate.handleCreateOfferCommand(command);

        return new ResponseEntity<>("Created: " + offerId, HttpStatus.CREATED);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferDto> fetch(@PathVariable UUID offerId) {

        var Query = new SingleOfferQuery(offerId);
        var offer = projection.handleSingleOfferQuery(new SingleOfferQuery(offerId));

        var now = timeProvider.now();

        var status = offer.isActiveOn(now) ? HttpStatus.OK : HttpStatus.GONE;

        return new ResponseEntity<>(
                new OfferDto(
                        offerId,
                        offer.getDescription().getValue(),
                        offer.getPrice().getCurrency(),
                        offer.getPrice().getMinorUnits(),
                        offer.getStart(),
                        offer.getDuration().getSeconds(),
                        offer.isExpiredOn(now),
                        offer.isCancelled(),
                        offer.isActiveOn(now)
                ),
                status
        );
    }

    @DeleteMapping("/offers/{offerId}")
    public ResponseEntity<String> cancel(@PathVariable UUID offerId) {
        var now = timeProvider.now();

        var command = new CancelOfferCommand(offerId, now);
        aggregate.handleCancelOfferCommand(command);
        return new ResponseEntity<>(
                "Deleted: " + offerId.toString(),
                HttpStatus.OK
        );
    }
}

