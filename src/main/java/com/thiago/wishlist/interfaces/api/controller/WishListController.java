package com.thiago.wishlist.interfaces.api.controller;

import com.thiago.wishlist.application.dto.ProductsIdsResponse;
import com.thiago.wishlist.application.usecase.WishListUseCase;
import com.thiago.wishlist.interfaces.api.dto.ProductsIdsResponseDTO;
import com.thiago.wishlist.interfaces.api.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
public class WishListController {

    private final WishListUseCase wishListUseCase;

    public WishListController(WishListUseCase wishListUseCase) {
        this.wishListUseCase = wishListUseCase;
    }

    @PostMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> addProduct(@PathVariable String customerId, @PathVariable String productId) {
        wishListUseCase.addProduct(customerId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        wishListUseCase.removeProduct(customerId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> removeWishList(@PathVariable String customerId) {
        wishListUseCase.removeWishList(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/products")
    public ResponseEntity<ResponseDTO<ProductsIdsResponseDTO>> getAllProducts(@PathVariable String customerId) {

        ProductsIdsResponse productsIdsResponse = wishListUseCase.getAllProducts(customerId);
        ProductsIdsResponseDTO dto = new ProductsIdsResponseDTO(productsIdsResponse.getProductsIds());
        return ResponseEntity.ok(new ResponseDTO<>(dto, "Success", HttpStatus.OK.value()));
    }
}
