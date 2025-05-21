package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.WishlistResponse;
import java.util.List;

public interface WishlistService {
    List<WishlistResponse> getWishlist(Long userId);
}

