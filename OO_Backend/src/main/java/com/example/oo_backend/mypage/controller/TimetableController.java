package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.dto.TimetableResponse;
import com.example.oo_backend.mypage.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    /**
     * 시간표 데이터를 조회
     * URL 예: GET /api/mypage/timetable?userId=user-123abc
     */
    @GetMapping
    public TimetableResponse getTimetable(@RequestParam("userId") String userId) {
        return timetableService.getTimetable(userId);
    }

    /**
     * 시간표 데이터를 등록 또는 수정합니다.
     * URL 예: POST /api/mypage/timetable?userId=user-123abc
     */
    @PostMapping
    public TimetableResponse saveOrUpdateTimetable(
            @RequestParam("userId") String userId,
            @RequestBody TimetableRequest request) {
        return timetableService.saveOrUpdateTimetable(userId, request);
    }
}


