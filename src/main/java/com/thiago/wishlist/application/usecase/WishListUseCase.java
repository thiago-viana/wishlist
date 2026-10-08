package com.thiago.wishlist.application.usecase;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.application.dto.ProductsIdsResponse;
import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.exception.BusinessException;
import com.thiago.wishlist.domain.exception.NotFoundException;
import com.thiago.wishlist.domain.repository.WishListRepository;
import com.thiago.wishlist.domain.vo.ProductId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class WishListUseCase {

    private final WishListRepository wishListRepository;
    private final WishListPropertiesProvider wishListPropertiesProvider;

    public WishListUseCase(WishListRepository wishListRepository, WishListPropertiesProvider wishListPropertiesProvider) {
        this.wishListRepository = wishListRepository;
        this.wishListPropertiesProvider = wishListPropertiesProvider;
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId cannot be null or blank");
        }
    }

    private void validateProductId(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId cannot be null or blank");
        }
    }

    public void addProduct(String customerId, String productId) {

        validateCustomerId(customerId);
        validateProductId(productId);

        WishList wishList = wishListRepository.findByCustomerId(customerId)
                .orElseGet(() -> new WishList(null, customerId, new HashSet<>()));

        if (wishList.productIdExists(new ProductId(productId))) {
            throw new BusinessException("Product already in list");
        }

        if (!wishList.canAddProductId(wishListPropertiesProvider.getMaxProducts())) {
            throw new BusinessException("Wishlist limit reached");
        }

        wishList.getProductsIds().add(new ProductId(productId));
        wishListRepository.save(wishList);
    }

    public void removeProduct(String customerId, String productId) {

        validateCustomerId(customerId);
        validateProductId(productId);

        WishList wishList = wishListRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Wishlist not found"));

        if (!wishList.getProductsIds().remove(new ProductId(productId))) {
            throw new NotFoundException("Product not found in wishlist");
        }

        wishListRepository.save(wishList);
    }

    public void removeWishList(String customerId) {

        validateCustomerId(customerId);

        wishListRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Wishlist not found"));

        wishListRepository.deleteByCustomerId(customerId);
    }

    public ProductsIdsResponse getAllProducts(String customerId) {

        validateCustomerId(customerId);

        Optional<WishList> wishList = wishListRepository.findByCustomerId(customerId);

        Set<ProductId> productsIds = wishList
                .map(WishList::getProductsIds)
                .orElseGet(Collections::emptySet);

        Set<String> ids = productsIds.stream()
                .map(ProductId::toString)
                .collect(Collectors.toSet());

        return new ProductsIdsResponse(ids);
    }
}
