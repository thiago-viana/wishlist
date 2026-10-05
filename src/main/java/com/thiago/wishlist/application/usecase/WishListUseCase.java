package com.thiago.wishlist.application.usecase;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.exception.BusinessException;
import com.thiago.wishlist.domain.exception.NotFoundException;
import com.thiago.wishlist.domain.repository.WishListRepository;
import com.thiago.wishlist.domain.vo.ProductId;

import java.util.HashSet;

public class WishListUseCase {

    private final WishListRepository wishListRepository;
    private final WishListPropertiesProvider wishListPropertiesProvider;

    public WishListUseCase(WishListRepository wishListRepository, WishListPropertiesProvider wishListPropertiesProvider) {
        this.wishListRepository = wishListRepository;
        this.wishListPropertiesProvider = wishListPropertiesProvider;
    }

    public void addProduct(String customerId, String productId) {

        WishList wishList = wishListRepository.findByCustomerId(customerId)
                .orElseGet(() -> new WishList(null, customerId, new HashSet<>()));

        if (wishList.getProductsIds().contains(new ProductId(productId))) {
            throw new BusinessException("Product already in list");
        }

        if (wishList.getProductsIds().size() >= wishListPropertiesProvider.getMaxProducts()) {
            throw new BusinessException("Wishlist limit reached");
        }

        wishList.getProductsIds().add(new ProductId(productId));
        wishListRepository.save(wishList);
    }

    public void removeProduct(String customerId, String productId) {

        WishList wishList = wishListRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Wishlist not found"));

        if (!wishList.getProductsIds().remove(new ProductId(productId))) {
            throw new NotFoundException("Product not found in wishlist");
        }

        wishListRepository.save(wishList);
    }
}
