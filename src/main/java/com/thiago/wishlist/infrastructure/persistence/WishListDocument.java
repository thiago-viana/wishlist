package com.thiago.wishlist.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "wishlists")
public class WishListDocument {

    @Id
    private String id;
    private String customerId;
    private Set<String> productsIds;

    public WishListDocument(String id, String customerId, Set<String> productsIds) {
        this.id = id;
        this.customerId = customerId;
        this.productsIds = productsIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Set<String> getProductsIds() {
        return productsIds;
    }

    public void setProductsIds(Set<String> productsIds) {
        this.productsIds = productsIds;
    }
}