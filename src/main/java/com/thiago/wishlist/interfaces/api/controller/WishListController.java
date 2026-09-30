package com.thiago.wishlist.interfaces.api.controller;

import com.thiago.wishlist.application.usecase.WishListUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlists")
public class WishListController {

    @Autowired
    private WishListUseCase wishListUseCase;

    @PostMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> addProduct(@PathVariable String customerId, @PathVariable String productId) {
        wishListUseCase.addProduct(customerId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
