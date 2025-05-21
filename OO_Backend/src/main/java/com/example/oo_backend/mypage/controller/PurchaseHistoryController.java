package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.PurchaseHistoryResponse;
import com.example.oo_backend.mypage.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage/purchases")
public class PurchaseHistoryController {

    private final PurchaseHistoryService purchaseHistoryService;

    @Autowired
    public PurchaseHistoryController(PurchaseHistoryService purchaseHistoryService) {
        this.purchaseHistoryService = purchaseHistoryService;
    }

    /**
     * 구매 내역을 조회
     * URL 예: GET /api/mypage/purchases?userId=user-123abc&status=reservation
     * status : "예약중" 또는 "구매완료"
     */
    @GetMapping
    public List<PurchaseHistoryResponse> getPurchaseHistory(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "status", required = false) String status) {
        return purchaseHistoryService.getPurchaseHistory(userId, status);
    }
}

