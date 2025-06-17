package com.example.oo_backend.sales.controller;

import com.example.oo_backend.sales.dto.SalesHistoryResponse;
import com.example.oo_backend.sales.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public List<SalesHistoryResponse> getSalesHistory(
            @RequestHeader("userId") Long sellerId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return salesService.getSalesHistory(sellerId, status);
    }

    @PostMapping("/create-transaction")
    public ResponseEntity<Long> createTransaction(
            @RequestParam Long bookId,
            @RequestParam Long sellerId,
            @RequestParam Long buyerId
    ) {
        Long transactionId = salesService.createTransactionIfNotExists(bookId, sellerId, buyerId);
        return ResponseEntity.ok(transactionId);
    }

    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<String> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestParam String status
    ) {
        salesService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.ok("거래 상태가 '" + status + "'(으)로 업데이트되었습니다.");
    }
}
