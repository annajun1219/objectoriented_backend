package com.example.oo_backend.mypage.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Long bookId;
    private String title;
    private double price;
    private String imageUrl;
    private LocalDateTime datetime;
    private String status;
    private String sellerId;
}

