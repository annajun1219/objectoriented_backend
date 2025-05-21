package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.PurchaseHistoryResponse;
import java.util.List;

public interface PurchaseHistoryService {
    List<PurchaseHistoryResponse> getPurchaseHistory(String userId, String status);
}
