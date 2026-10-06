package com.thiago.wishlist.application.dto;

import java.util.Set;

public class ProductsIdsResponse {

    private Set<String> productsIds;

    public ProductsIdsResponse(Set<String> productsIds) {
        this.productsIds = productsIds;
    }

    public Set<String> getProductsIds() {
        return productsIds;
    }

    public void setProductsIds(Set<String> productsIds) {
        this.productsIds = productsIds;
    }
}
