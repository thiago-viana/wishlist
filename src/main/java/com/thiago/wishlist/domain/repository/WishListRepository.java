package com.thiago.wishlist.domain.repository;

import com.thiago.wishlist.domain.entity.WishList;

import java.util.Optional;

public interface WishListRepository {

    Optional<WishList> findByCustomerId(String customerId);
    void save(WishList wishList);
}
