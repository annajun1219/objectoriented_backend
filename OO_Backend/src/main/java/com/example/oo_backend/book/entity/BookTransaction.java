package com.example.oo_backend.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구매자 ID
    private Long buyerId;

    // 판매자 ID (추가)
    private Long sellerId;

    // 교재 ID
    private Long productId;

    // 교재 제목 (추가)
    private String productTitle;

    // 결제 금액
    private int price;

    // 결제 방식 (예: "직거래")
    private String paymentMethod;

    // 수령인 이름
    private String recipientName;

    // 수령인 전화번호
    private String recipientPhone;

    // 거래 상태 (예: "직거래 요청", "예약 중")
    private String status;
}
