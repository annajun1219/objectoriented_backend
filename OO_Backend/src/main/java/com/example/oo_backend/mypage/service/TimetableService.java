package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.dto.TimetableResponse;

public interface TimetableService {
    TimetableResponse getTimetable(Long userId);
    TimetableResponse saveOrUpdateTimetable(Long userId, TimetableRequest request);
}

