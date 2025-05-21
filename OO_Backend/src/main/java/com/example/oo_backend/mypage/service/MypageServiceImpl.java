package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.ProfileResponse;
import com.example.oo_backend.mypage.dto.TimetableResponse;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class MypageServiceImpl implements MypageService {

    private final TimetableRepository timetableRepository;

    @Autowired
    public MypageServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public ProfileResponse getMypageInfo(String userId) {
        // 1. 사용자 정보 조회 (실제 DB 호출 대체: 여기서는 간단히 하드코딩)
        String name = "책좋아";
        String profileImage = "https://cdn.site/profile/123.png";
        double rating = 4.8;

        // 2. 시간표 정보 조회 (DB에서 Timetable 엔티티 조회)
        Timetable timetable = timetableRepository.findByUserId(userId);
        TimetableResponse.ScheduleInfo scheduleInfo;
        if (timetable != null) {
            // timetable.getTimetableData()를 파싱하는 로직을 추가할 수 있음 (예: JSON -> Map)
            scheduleInfo = TimetableResponse.ScheduleInfo.builder()
                    .uploaded(true)
                    .scheduleSummary(Arrays.asList(
                            "월 1교시 객체지향프로그래밍",
                            "화 2교시 자료구조",
                            "수 3교시 경영학원론"
                    ))
                    .lastUploaded("2025-04-26T10:20:30Z")
                    .build();
        } else {
            scheduleInfo = TimetableResponse.ScheduleInfo.builder()
                    .uploaded(false)
                    .scheduleSummary(Collections.emptyList())
                    .lastUploaded("")
                    .build();
        }

        // 3. ProfileResponse DTO 구성 (구매/판매 내역은 개별 API에서 조회하므로 여기서는 시간표 및 기본정보만)
        return ProfileResponse.builder()
                .userId(userId)
                .name(name)
                .profileImage(profileImage)
                .rating(rating)
                .scheduleInfo(scheduleInfo)
                .build();
    }
}

