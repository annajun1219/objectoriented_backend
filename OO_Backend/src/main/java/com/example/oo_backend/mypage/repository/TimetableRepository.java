package com.example.oo_backend.mypage.repository;

import com.example.oo_backend.mypage.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Timetable findByUserId(Long userId);
}

