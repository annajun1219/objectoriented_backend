package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.SalesHistoryResponse;
import com.example.oo_backend.mypage.service.SalesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage/sales")
public class SalesHistoryController {

    private final SalesHistoryService salesHistoryService;

    @Autowired
    public SalesHistoryController(SalesHistoryService salesHistoryService) {
        this.salesHistoryService = salesHistoryService;
    }

    /**
     * 판매 내역을 조회
     * URL 예: GET /api/mypage/sales?userId=user-123abc&status=on_sale
     * status : "판매중", "예약중", "판매완료"
     */
    @GetMapping
    public List<SalesHistoryResponse> getSalesHistory(
            @RequestParam("userId") String userId,
            @RequestParam(value = "status", required = false) String status) {
        return salesHistoryService.getSalesHistory(userId, status);
    }
}


