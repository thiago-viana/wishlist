package com.thiago.wishlist.infrastructure.repository;

import com.thiago.wishlist.infrastructure.persistence.ConfigPropertyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigPropertyRepository extends MongoRepository<ConfigPropertyDocument,String> {

    ConfigPropertyDocument findByKey(String key);
}
