package com.example.oo_backend.mypage.repository;

import com.example.oo_backend.mypage.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}

