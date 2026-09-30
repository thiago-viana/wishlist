package com.thiago.wishlist.application.usecase;

import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.repository.WishListRepository;
import com.thiago.wishlist.domain.vo.ProductId;

import java.util.HashSet;

public class WishListUseCase {

    private final WishListRepository wishListRepository;

    public WishListUseCase(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void addProduct(String customerId, String productId) {

        WishList wishList = wishListRepository.findByCustomerId(customerId)
                .orElseGet(() -> new WishList(null, customerId, new HashSet<>()));

        // TODO validar tamanho máximo da lista

        // TODO Validar se o produto existe na lista

        wishList.getProductsIds().add(new ProductId(productId));
        wishListRepository.save(wishList);
    }
}
