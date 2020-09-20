package dev.luke10x.fis.offer.util;

import dev.luke10x.fis.offer.rest.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeProviderImpl implements TimeProvider {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
