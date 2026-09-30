package com.thiago.wishlist.infrastructure.repository;

import com.thiago.wishlist.infrastructure.persistence.WishListDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishListMongoSpringData extends MongoRepository<WishListDocument, String> {

    Optional<WishListDocument> findByCustomerId(String customerId);
}
