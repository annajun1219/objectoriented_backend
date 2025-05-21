package com.example.oo_backend.mypage.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private Long bookId;
    private String title;
    private double price;
    private String imageUrl;
    private String datetime;
    private String status; // "판매중", "예약중", "판매완료" 등
    private String sellerId;
}
