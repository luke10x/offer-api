package dev.luke10x.fis.offer.rest;

import dev.luke10x.fis.offer.domain.OfferService;
import dev.luke10x.fis.offer.domain.model.Description;
import dev.luke10x.fis.offer.domain.model.Money;
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
    private final OfferService service;
    private final UUIDProvider uuidProvider;
    private final TimeProvider timeProvider;

    public OfferController(OfferService service, UUIDProvider uuidProvider, TimeProvider timeProvider) {
        this.service = service;
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

        service.createOffer(offerId, description, price, start, duration);

        return new ResponseEntity<>("Created: " + offerId, HttpStatus.CREATED);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferDto> fetch(@PathVariable UUID offerId) {

        var offer = service.getOffer(offerId);

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
}

