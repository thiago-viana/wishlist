package com.thiago.wishlist.domain.vo;

import java.util.Objects;

public record ProductId(String value) {

    public ProductId(String value) {

        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ProductId cannot be null or blank.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductId(String value1))) return false;
        return Objects.equals(value, value1);
    }

    @Override
    public String toString() {
        return value;
    }
}
