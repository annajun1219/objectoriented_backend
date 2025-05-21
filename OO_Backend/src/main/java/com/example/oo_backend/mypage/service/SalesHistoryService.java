package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.SalesHistoryResponse;
import java.util.List;

public interface SalesHistoryService {
    List<SalesHistoryResponse> getSalesHistory(String userId, String status);
}

