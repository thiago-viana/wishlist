package com.thiago.wishlist.infrastructure.repository;

import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.repository.WishListRepository;
import com.thiago.wishlist.infrastructure.persistence.WishListDocument;
import com.thiago.wishlist.infrastructure.persistence.WishListMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WishListRepositoryMongoImpl implements WishListRepository {

    private final WishListMongoSpringData mongoRepo;
    private final WishListMapper wishListMapper;

    public WishListRepositoryMongoImpl(WishListMongoSpringData mongoRepo, WishListMapper wishListMapper) {
        this.mongoRepo = mongoRepo;
        this.wishListMapper = wishListMapper;
    }

    @Override
    public Optional<WishList> findByCustomerId(String customerId) {
        return mongoRepo.findByCustomerId(customerId)
                .map(wishListMapper::toDomain);
    }

    @Override
    public void save(WishList wishList) {
        WishListDocument document = wishListMapper.toDocument(wishList);
        mongoRepo.save(document);
    }

    @Override
    public void deleteByCustomerId(String customerId) {
        mongoRepo.deleteByCustomerId(customerId);
    }
}
