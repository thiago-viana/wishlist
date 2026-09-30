package com.thiago.wishlist.interfaces.config;

import com.thiago.wishlist.application.usecase.WishListUseCase;
import com.thiago.wishlist.domain.repository.WishListRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WishListConfig {

    @Bean
    public WishListUseCase wishListUseCase(WishListRepository wishListRepository) {
        return new WishListUseCase(wishListRepository);
    }
}
