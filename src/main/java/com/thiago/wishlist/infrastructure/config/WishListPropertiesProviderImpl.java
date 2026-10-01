package com.thiago.wishlist.infrastructure.config;

import com.thiago.wishlist.application.config.WishListPropertiesProvider;
import com.thiago.wishlist.infrastructure.persistence.ConfigPropertyDocument;
import com.thiago.wishlist.infrastructure.repository.ConfigPropertyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class WishListPropertiesProviderImpl implements WishListPropertiesProvider {

    private static final Logger LOGGER = LogManager.getLogger(WishListPropertiesProviderImpl.class);
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
                LOGGER.warn("Value found for maxProducts {}", prop.getValue());
            }
        }
        return 6;
    }
}
