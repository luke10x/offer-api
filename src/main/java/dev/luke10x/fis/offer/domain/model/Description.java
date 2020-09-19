package dev.luke10x.fis.offer.domain.model;

import java.util.Objects;

public class Description {
    private String value;

    private Description() {}

    public static Description from(String value) {
        if ("".equals(value))
            throw new IllegalArgumentException("Description cannot be empty");

        var description = new Description();
        description.value = value.substring(0, Math.min(value.length(), 20));
        return description;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
