package com.thiago.wishlist.infrastructure.persistence;

import com.thiago.wishlist.domain.entity.WishList;
import com.thiago.wishlist.domain.vo.ProductId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    WishListMapper INSTANCE = Mappers.getMapper(WishListMapper.class);

    WishListDocument toDocument(WishList wishList);
    WishList toDomain(WishListDocument document);

    default Set<String> mapProductsIdsToStrings(Set<ProductId> productIds) {
        if (productIds == null) {
            return new HashSet<>();
        }
        return productIds.stream()
                .map(ProductId::value)
                .collect(Collectors.toSet());
    }

    default Set<ProductId> mapStringsToProductsIds(Set<String> productIds) {
        if (productIds == null) {
            return new HashSet<>();
        }
        return productIds.stream()
                .map(ProductId::new)
                .collect(Collectors.toSet());
    }
}
