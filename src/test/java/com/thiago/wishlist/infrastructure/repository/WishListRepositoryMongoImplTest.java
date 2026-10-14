package com.thiago.wishlist.infrastructure.repository;

import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.vo.ProductId;
import com.thiago.wishlist.infrastructure.persistence.WishListDocument;
import com.thiago.wishlist.infrastructure.persistence.WishListMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishListRepositoryMongoImplTest {

    private static final String CUSTOMER_ID = "customer-1";

    @Mock
    private WishListMongoSpringData mongoRepo;

    @Mock
    private WishListMapper wishListMapper;

    @InjectMocks
    private WishListRepositoryMongoImpl repository;

    private WishList createWishList(String... productIds) {
        Set<ProductId> ids = new HashSet<>();
        for (String id : productIds) {
            ids.add(new ProductId(id));
        }
        return new WishList("wl-1", CUSTOMER_ID, ids);
    }

    private WishListDocument createWishListDocument(String... productIds) {
        Set<String> ids = new HashSet<>();
        Collections.addAll(ids, productIds);
        return new WishListDocument("wl-1", CUSTOMER_ID, ids);
    }

    @Nested
    @DisplayName("findByCustomerId")
    class FindByCustomerId {

        @Test
        void returnsWishListWhenFound() {
            WishListDocument document = createWishListDocument("p1", "p2");
            WishList expected = createWishList("p1", "p2");

            when(mongoRepo.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(document));
            when(wishListMapper.toDomain(document)).thenReturn(expected);

            Optional<WishList> result = repository.findByCustomerId(CUSTOMER_ID);

            assertTrue(result.isPresent());
            assertEquals(expected, result.get());
            verify(mongoRepo).findByCustomerId(CUSTOMER_ID);
            verify(wishListMapper).toDomain(document);
        }

        @Test
        void returnsEmptyWhenNotFound() {
            when(mongoRepo.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

            Optional<WishList> result = repository.findByCustomerId(CUSTOMER_ID);

            assertTrue(result.isEmpty());
            verify(mongoRepo).findByCustomerId(CUSTOMER_ID);
        }

        @Test
        void returnsEmptyWhenDocumentIsNull() {
            when(mongoRepo.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

            Optional<WishList> result = repository.findByCustomerId(CUSTOMER_ID);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        void savesWishListAfterMapping() {
            WishList wishList = createWishList("p1");
            WishListDocument document = createWishListDocument("p1");

            when(wishListMapper.toDocument(wishList)).thenReturn(document);

            repository.save(wishList);

            verify(wishListMapper).toDocument(wishList);
            verify(mongoRepo).save(document);
        }

        @Test
        void callsMapperAndMongoRepoInCorrectOrder() {
            WishList wishList = createWishList("p1", "p2");
            WishListDocument document = createWishListDocument("p1", "p2");

            when(wishListMapper.toDocument(wishList)).thenReturn(document);

            repository.save(wishList);

            ArgumentCaptor<WishList> wishListCaptor = ArgumentCaptor.forClass(WishList.class);
            ArgumentCaptor<WishListDocument> documentCaptor = ArgumentCaptor.forClass(WishListDocument.class);

            verify(wishListMapper).toDocument(wishListCaptor.capture());
            verify(mongoRepo).save(documentCaptor.capture());

            assertEquals(wishList, wishListCaptor.getValue());
            assertEquals(document, documentCaptor.getValue());
        }
    }

    @Nested
    @DisplayName("deleteByCustomerId")
    class DeleteByCustomerId {

        @Test
        void deletesWishListByCustomerId() {
            repository.deleteByCustomerId(CUSTOMER_ID);

            verify(mongoRepo).deleteByCustomerId(CUSTOMER_ID);
        }

        @Test
        void callsMongoRepoDelete() {
            String customerId = "customer-2";

            repository.deleteByCustomerId(customerId);

            verify(mongoRepo).deleteByCustomerId(customerId);
        }
    }
}
