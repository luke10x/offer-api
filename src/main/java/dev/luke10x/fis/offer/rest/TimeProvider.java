package dev.luke10x.fis.offer.rest;

import java.time.Instant;

public interface TimeProvider {
    Instant now();
}
