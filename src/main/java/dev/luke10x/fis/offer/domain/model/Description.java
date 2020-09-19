package dev.luke10x.fis.offer.domain.model;

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
}
