package com.thiago.wishlist.infrastructure.config;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.infrastructure.persistence.ConfigPropertyDocument;
import com.thiago.wishlist.infrastructure.repository.ConfigPropertyRepository;
import org.springframework.stereotype.Component;

@Component
public class WishListPropertiesProviderImpl implements WishListPropertiesProvider {

    private final ConfigPropertyRepository configPropertyRepository;

    public WishListPropertiesProviderImpl(ConfigPropertyRepository configPropertyRepository) {
        this.configPropertyRepository = configPropertyRepository;
    }

    @Override
    public int getMaxProducts() {
        ConfigPropertyDocument prop = configPropertyRepository.findByKey("wishlist.maxProducts");
        if (prop != null) {
            try {
                return Integer.parseInt(prop.getValue());
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return 6;
    }
}
