package com.example.oo_backend.mypage.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 판매자 식별
    private Long bookId;
    private String title;
    private double price;
    private String imageUrl;
    private LocalDateTime datetime;
    private String status; // "판매중", "예약중", "판매완료"
}
