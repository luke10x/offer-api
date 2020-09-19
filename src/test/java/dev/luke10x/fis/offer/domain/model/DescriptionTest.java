package dev.luke10x.fis.offer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    @Test
    void descriptionHasValue() {
        var description = Description.from("Cheap cheese");

        assertEquals("Cheap cheese", description.getValue());
    }

    @Test
    void descriptionShouldNotBeEmpty() {
        Exception exception = assertThrows(Exception.class, () -> {
            Description.from("");
        });

        assertTrue(exception.getMessage().contains("cannot be empty"));
    }
    @Test
    void descriptionIsTruncated() {
        var originalValue = "Very very long description";
        var description = Description.from(originalValue);

        assertEquals(26, originalValue.length());
        assertEquals(20, description.getValue().length());
    }
}