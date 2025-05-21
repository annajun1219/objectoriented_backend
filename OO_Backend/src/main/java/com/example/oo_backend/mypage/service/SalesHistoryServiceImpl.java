package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.SalesHistoryResponse;
import com.example.oo_backend.mypage.entity.SalesHistory;
import com.example.oo_backend.mypage.repository.SalesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesHistoryServiceImpl implements SalesHistoryService {

    private final SalesHistoryRepository salesHistoryRepository;

    @Autowired
    public SalesHistoryServiceImpl(SalesHistoryRepository salesHistoryRepository) {
        this.salesHistoryRepository = salesHistoryRepository;
    }

    @Override
    public List<SalesHistoryResponse> getSalesHistory(String userId, String status) {
        List<SalesHistory> histories = (status != null && !status.isEmpty())
                ? salesHistoryRepository.findByUserIdAndStatus(userId, status)
                : salesHistoryRepository.findByUserId(userId);

        return histories.stream().map(h -> SalesHistoryResponse.builder()
                        .bookId(h.getBookId())
                        .title(h.getTitle())
                        .price(h.getPrice())
                        .imageUrl(h.getImageUrl())
                        .datetime(h.getDatetime().toString())
                        .status(h.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}

