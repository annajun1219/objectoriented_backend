package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.WishlistResponse;
import com.example.oo_backend.mypage.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * 사용자의 찜 목록을 조회
     * URL 예: GET /api/mypage/wishlist?userId=user-123abc
     */
    @GetMapping
    public List<WishlistResponse> getWishlist(@RequestParam("userId") String userId) {
        return wishlistService.getWishlist(userId);
    }
}
