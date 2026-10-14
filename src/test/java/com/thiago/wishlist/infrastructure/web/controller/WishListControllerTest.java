package com.thiago.wishlist.infrastructure.web.controller;

import com.thiago.wishlist.application.dto.ProductsIdsResponse;
import com.thiago.wishlist.application.usecase.WishListUseCase;
import com.thiago.wishlist.domain.exception.BusinessException;
import com.thiago.wishlist.domain.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishListControllerTest {

    private static final String CUSTOMER_ID = "customer-1";
    private static final String PRODUCT_ID = "p1";

    @Mock
    private WishListUseCase wishListUseCase;

    @InjectMocks
    private WishListController controller;

    @Nested
    @DisplayName("addProduct")
    class AddProduct {

        @Test
        void addsProductSuccessfully() {
            doNothing().when(wishListUseCase).addProduct(CUSTOMER_ID, PRODUCT_ID);

            ResponseEntity<Void> response = controller.addProduct(CUSTOMER_ID, PRODUCT_ID);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            verify(wishListUseCase).addProduct(CUSTOMER_ID, PRODUCT_ID);
        }

        @Test
        void throwsExceptionWhenProductAlreadyExists() {
            doThrow(new BusinessException("Product already in list"))
                    .when(wishListUseCase).addProduct(CUSTOMER_ID, PRODUCT_ID);

            try {
                controller.addProduct(CUSTOMER_ID, PRODUCT_ID);
            } catch (BusinessException e) {
                assertEquals("Product already in list", e.getMessage());
            }
        }

        @Test
        void throwsExceptionWhenWishlistLimitReached() {
            doThrow(new BusinessException("Wishlist limit reached"))
                    .when(wishListUseCase).addProduct(CUSTOMER_ID, PRODUCT_ID);

            try {
                controller.addProduct(CUSTOMER_ID, PRODUCT_ID);
            } catch (BusinessException e) {
                assertEquals("Wishlist limit reached", e.getMessage());
            }
        }

        @Test
        void throwsExceptionWhenProductIdIsInvalid() {
            doThrow(new IllegalArgumentException("productId cannot be null or blank"))
                    .when(wishListUseCase).addProduct(CUSTOMER_ID, " ");

            try {
                controller.addProduct(CUSTOMER_ID, " ");
            } catch (IllegalArgumentException e) {
                assertEquals("productId cannot be null or blank", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("removeProduct")
    class RemoveProduct {

        @Test
        void removesProductSuccessfully() {
            doNothing().when(wishListUseCase).removeProduct(CUSTOMER_ID, PRODUCT_ID);

            ResponseEntity<Void> response = controller.removeProduct(CUSTOMER_ID, PRODUCT_ID);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(wishListUseCase).removeProduct(CUSTOMER_ID, PRODUCT_ID);
        }

        @Test
        void throwsExceptionWhenWishListNotFound() {
            doThrow(new NotFoundException("Wishlist not found"))
                    .when(wishListUseCase).removeProduct(CUSTOMER_ID, PRODUCT_ID);

            try {
                controller.removeProduct(CUSTOMER_ID, PRODUCT_ID);
            } catch (NotFoundException e) {
                assertEquals("Wishlist not found", e.getMessage());
            }
        }

        @Test
        void throwsExceptionWhenProductNotInList() {
            doThrow(new NotFoundException("Product not found in wishlist"))
                    .when(wishListUseCase).removeProduct(CUSTOMER_ID, PRODUCT_ID);

            try {
                controller.removeProduct(CUSTOMER_ID, PRODUCT_ID);
            } catch (NotFoundException e) {
                assertEquals("Product not found in wishlist", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("removeWishList")
    class RemoveWishList {

        @Test
        void removesWishListSuccessfully() {
            doNothing().when(wishListUseCase).removeWishList(CUSTOMER_ID);

            ResponseEntity<Void> response = controller.removeWishList(CUSTOMER_ID);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(wishListUseCase).removeWishList(CUSTOMER_ID);
        }

        @Test
        void throwsExceptionWhenWishListNotFound() {
            doThrow(new NotFoundException("Wishlist not found"))
                    .when(wishListUseCase).removeWishList(CUSTOMER_ID);

            try {
                controller.removeWishList(CUSTOMER_ID);
            } catch (NotFoundException e) {
                assertEquals("Wishlist not found", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("getAllProducts")
    class GetAllProducts {

        @Test
        void returnsAllProductsSuccessfully() {
            ProductsIdsResponse response = new ProductsIdsResponse(Set.of("p1", "p2"));
            when(wishListUseCase.getAllProducts(CUSTOMER_ID)).thenReturn(response);

            ResponseEntity<?> result = controller.getAllProducts(CUSTOMER_ID);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            verify(wishListUseCase).getAllProducts(CUSTOMER_ID);
        }

        @Test
        void returnsEmptyProductsWhenWishListNotFound() {
            ProductsIdsResponse response = new ProductsIdsResponse(Set.of());
            when(wishListUseCase.getAllProducts(CUSTOMER_ID)).thenReturn(response);

            ResponseEntity<?> result = controller.getAllProducts(CUSTOMER_ID);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            verify(wishListUseCase).getAllProducts(CUSTOMER_ID);
        }

        @Test
        void returnsEmptyProductsWhenNoProductsInWishList() {
            ProductsIdsResponse response = new ProductsIdsResponse(Set.of());
            when(wishListUseCase.getAllProducts(CUSTOMER_ID)).thenReturn(response);

            ResponseEntity<?> result = controller.getAllProducts(CUSTOMER_ID);

            assertEquals(HttpStatus.OK, result.getStatusCode());
        }
    }
}
