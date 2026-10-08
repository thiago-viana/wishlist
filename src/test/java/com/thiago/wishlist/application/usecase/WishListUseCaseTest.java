package com.thiago.wishlist.application.usecase;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.application.dto.ProductsIdsResponse;
import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.exception.BusinessException;
import com.thiago.wishlist.domain.exception.NotFoundException;
import com.thiago.wishlist.domain.repository.WishListRepository;
import com.thiago.wishlist.domain.vo.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishListUseCaseTest {

    private static final String CUSTOMER_ID = "customer-1";

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private WishListPropertiesProvider wishListPropertiesProvider;

    @InjectMocks
    private WishListUseCase useCase;

    private WishList wishListWith(String... productIds) {
        Set<ProductId> ids = new HashSet<>();
        for (String id : productIds) {
            ids.add(new ProductId(id));
        }
        return new WishList("wl-1", CUSTOMER_ID, ids);
    }

    @Nested
    @DisplayName("addProduct")
    class AddProduct {

        @Test
        void createsNewWishListWhenNoneExists() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());
            when(wishListPropertiesProvider.getMaxProducts()).thenReturn(20);

            useCase.addProduct(CUSTOMER_ID, "p1");

            ArgumentCaptor<WishList> captor = ArgumentCaptor.forClass(WishList.class);
            verify(wishListRepository).save(captor.capture());
            WishList saved = captor.getValue();
            assertEquals(CUSTOMER_ID, saved.getCustomerId());
            assertEquals(Set.of(new ProductId("p1")), saved.getProductsIds());
        }

        @Test
        void addsProductToExistingWishList() {
            WishList existing = wishListWith("p1");
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(existing));
            when(wishListPropertiesProvider.getMaxProducts()).thenReturn(20);

            useCase.addProduct(CUSTOMER_ID, "p2");

            verify(wishListRepository).save(existing);
            assertEquals(Set.of(new ProductId("p1"), new ProductId("p2")), existing.getProductsIds());
        }

        @Test
        void throwsWhenProductAlreadyInList() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith("p1")));

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> useCase.addProduct(CUSTOMER_ID, "p1"));

            assertEquals("Product already in list", ex.getMessage());
            verify(wishListRepository, never()).save(any());
        }

        @Test
        void throwsWhenLimitReached() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith("p1", "p2")));
            when(wishListPropertiesProvider.getMaxProducts()).thenReturn(2);

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> useCase.addProduct(CUSTOMER_ID, "p3"));

            assertEquals("Wishlist limit reached", ex.getMessage());
            verify(wishListRepository, never()).save(any());
        }

        @Test
        void allowsAddingJustBelowLimit() {
            WishList existing = wishListWith("p1");
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(existing));
            when(wishListPropertiesProvider.getMaxProducts()).thenReturn(2);

            useCase.addProduct(CUSTOMER_ID, "p2");

            verify(wishListRepository).save(existing);
            assertEquals(2, existing.getProductsIds().size());
        }

        @Test
        void rejectsBlankProductId() {
            assertThrows(IllegalArgumentException.class, () -> useCase.addProduct(CUSTOMER_ID, " "));

            verify(wishListRepository, never()).save(any());
        }

        @Test
        void rejectsBlankCustomerId() {
            assertThrows(IllegalArgumentException.class, () -> useCase.addProduct(" ", "p1"));

            verify(wishListRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeProduct")
    class RemoveProduct {

        @Test
        void removesProduct() {
            WishList existing = wishListWith("p1", "p2");
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(existing));

            useCase.removeProduct(CUSTOMER_ID, "p1");

            verify(wishListRepository).save(existing);
            assertEquals(Set.of(new ProductId("p2")), existing.getProductsIds());
        }

        @Test
        void throwsWhenWishListNotFound() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> useCase.removeProduct(CUSTOMER_ID, "p1"));

            assertEquals("Wishlist not found", ex.getMessage());
            verify(wishListRepository, never()).save(any());
        }

        @Test
        void throwsWhenProductNotFound() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith("p1")));

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> useCase.removeProduct(CUSTOMER_ID, "p2"));

            assertEquals("Product not found in wishlist", ex.getMessage());
            verify(wishListRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeWishList")
    class RemoveWishList {

        @Test
        void deletesWishList() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith("p1")));

            useCase.removeWishList(CUSTOMER_ID);

            verify(wishListRepository).deleteByCustomerId(CUSTOMER_ID);
        }

        @Test
        void throwsWhenNotFound() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> useCase.removeWishList(CUSTOMER_ID));

            assertEquals("Wishlist not found", ex.getMessage());
            verify(wishListRepository, never()).deleteByCustomerId(any());
        }
    }

    @Nested
    @DisplayName("getAllProducts")
    class GetAllProducts {

        @Test
        void returnsAllProducts() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith("p1", "p2")));

            ProductsIdsResponse response = useCase.getAllProducts(CUSTOMER_ID);

            assertEquals(Set.of("p1", "p2"), response.getProductsIds());
        }

        @Test
        void returnsEmptyWhenNotFound() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

            ProductsIdsResponse response = useCase.getAllProducts(CUSTOMER_ID);

            assertTrue(response.getProductsIds().isEmpty());
        }

        @Test
        void returnsEmptyWhenNoProducts() {
            when(wishListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishListWith()));

            ProductsIdsResponse response = useCase.getAllProducts(CUSTOMER_ID);

            assertTrue(response.getProductsIds().isEmpty());
        }
    }
}
