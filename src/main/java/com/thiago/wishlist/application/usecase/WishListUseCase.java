package com.thiago.wishlist.application.usecase;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.exception.BusinessException;
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

        // TODO Validar se o produto existe na lista
        if (wishList.getProductsIds().contains(new ProductId(productId))) {
            throw new BusinessException("Product already in list");
        }

        // TODO validar tamanho máximo da lista
        if (wishList.getProductsIds().size() >= wishListPropertiesProvider.getMaxProducts()) {
            throw new BusinessException("Wishlist limit reached");
        }

        wishList.getProductsIds().add(new ProductId(productId));
        wishListRepository.save(wishList);
    }
}
