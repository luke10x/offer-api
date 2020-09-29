package dev.luke10x.fis.offer.domain.command;

import dev.luke10x.fis.offer.domain.model.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferAggregate {
    private OfferWriteRepository writeRepository;
    public OfferAggregate(OfferWriteRepository repository) {
        this.writeRepository = repository;
    }

    public Offer handleCreateOfferCommand(CreateOfferCommand command) {
        var offerId = command.getOfferId();
        var description = command.getDescription();
        var price = command.getPrice();
        var start = command.getStart();
        var duration = command.getDuration();

        Offer offer = new Offer(offerId, description, price, start, duration);
        writeRepository.putOffer(command.getOfferId(), offer);
        return offer;
    }

    public Offer handleCancelOfferCommand(CancelOfferCommand command) {
        Offer offer = writeRepository.getOffer(command.getOfferId());
        offer.cancel(command.getCancellationTime());
        writeRepository.putOffer(offer.getOfferId(), offer);
        return offer;
    }
}
