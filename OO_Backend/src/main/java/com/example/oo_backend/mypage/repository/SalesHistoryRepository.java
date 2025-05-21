package com.example.oo_backend.mypage.repository;

import com.example.oo_backend.mypage.entity.SalesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesHistoryRepository extends JpaRepository<SalesHistory, Long> {
    List<SalesHistory> findByUserId(Long userId);
    List<SalesHistory> findByUserIdAndStatus(Long userId, String status);
}
