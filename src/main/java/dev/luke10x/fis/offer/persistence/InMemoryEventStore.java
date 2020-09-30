package dev.luke10x.fis.offer.persistence;

import dev.luke10x.fis.offer.domain.event.Event;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryEventStore implements dev.luke10x.fis.offer.domain.EventStore {
    private final OfferProjector projector;

    private Map<UUID, List<Event>> store = new HashMap<>();

    public InMemoryEventStore(OfferProjector projector) {
        this.projector = projector;
    }

    public void addEvent(UUID offerId, Event event) {
        store.putIfAbsent(offerId, new ArrayList<Event>());
        store.get(offerId).add(event);
        projector.project(event);
    }

    public List<Event> getOfferEvents(UUID offerId) {
        return store.get(offerId);
    }
}