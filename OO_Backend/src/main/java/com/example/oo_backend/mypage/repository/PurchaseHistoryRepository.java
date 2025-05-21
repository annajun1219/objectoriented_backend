package com.example.oo_backend.mypage.repository;

import com.example.oo_backend.mypage.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByUserId(String userId);
    List<PurchaseHistory> findByUserIdAndStatus(String userId, String status);
}

