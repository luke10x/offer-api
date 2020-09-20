package dev.luke10x.fis.offer.util;

import dev.luke10x.fis.offer.rest.UUIDProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProviderImpl implements UUIDProvider {
    @Override
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
