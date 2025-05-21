package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.WishlistResponse;
import com.example.oo_backend.mypage.entity.Wishlist;
import com.example.oo_backend.mypage.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<WishlistResponse> getWishlist(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        return wishlists.stream()
                .map(wishlist -> WishlistResponse.builder()
                        .bookId(wishlist.getBookId())
                        .title(wishlist.getTitle())
                        .price(wishlist.getPrice())
                        .imageUrl(wishlist.getImageUrl())
                        .datetime(wishlist.getDatetime().toString())
                        .status(wishlist.getStatus())
                        .sellerId(wishlist.getSellerId())
                        .build())
                .collect(Collectors.toList());
    }
}
