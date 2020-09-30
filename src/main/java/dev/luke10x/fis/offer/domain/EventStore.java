package dev.luke10x.fis.offer.domain;

import dev.luke10x.fis.offer.domain.event.Event;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void addEvent(UUID offerId, Event event);
    List<Event> getOfferEvents(UUID offerId);
}
