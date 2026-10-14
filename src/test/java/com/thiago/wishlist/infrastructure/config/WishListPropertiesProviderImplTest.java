package com.thiago.wishlist.infrastructure.config;

import com.thiago.wishlist.infrastructure.persistence.ConfigPropertyDocument;
import com.thiago.wishlist.infrastructure.repository.ConfigPropertyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishListPropertiesProviderImplTest {

    private static final String MAX_PRODUCTS_KEY = "wishlist.maxProducts";

    @Mock
    private ConfigPropertyRepository configPropertyRepository;

    @InjectMocks
    private WishListPropertiesProviderImpl propertiesProvider;

    @Nested
    @DisplayName("getMaxProducts")
    class GetMaxProducts {

        @Test
        void returnsMaxProductsWhenPropertyExists() {
            ConfigPropertyDocument prop = new ConfigPropertyDocument();
            prop.setKey(MAX_PRODUCTS_KEY);
            prop.setValue("10");
            when(configPropertyRepository.findByKey(MAX_PRODUCTS_KEY)).thenReturn(prop);

            int result = propertiesProvider.getMaxProducts();

            assertEquals(10, result);
        }

        @Test
        void returnsDefaultWhenPropertyDoesNotExist() {
            when(configPropertyRepository.findByKey(MAX_PRODUCTS_KEY)).thenReturn(null);

            int result = propertiesProvider.getMaxProducts();

            assertEquals(6, result);
        }

        @Test
        void returnsDefaultWhenPropertyValueIsInvalid() {
            ConfigPropertyDocument prop = new ConfigPropertyDocument();
            prop.setKey(MAX_PRODUCTS_KEY);
            prop.setValue("invalid");
            when(configPropertyRepository.findByKey(MAX_PRODUCTS_KEY)).thenReturn(prop);

            int result = propertiesProvider.getMaxProducts();

            assertEquals(6, result);
        }

        @Test
        void returnsMaxProductsWithDifferentValidValues() {
            ConfigPropertyDocument prop = new ConfigPropertyDocument();
            prop.setKey(MAX_PRODUCTS_KEY);
            prop.setValue("20");
            when(configPropertyRepository.findByKey(MAX_PRODUCTS_KEY)).thenReturn(prop);

            int result = propertiesProvider.getMaxProducts();

            assertEquals(20, result);
        }
    }
}
