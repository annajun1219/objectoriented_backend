package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.ProfileResponse;
import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MypageServiceImpl implements MypageService {

    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;

    @Autowired
    public MypageServiceImpl(TimetableRepository timetableRepository, UserRepository userRepository) {
        this.timetableRepository = timetableRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProfileResponse getMypageInfo(Long userId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId).orElseGet(() -> {
            User defaultUser = new User();
            defaultUser.setUserId(userId);
            defaultUser.setName("기본 이름");
            defaultUser.setProfileImage("https://default.site/profile.png");
            return defaultUser;
        });

        // 2. 시간표 데이터 조회 (private 메서드 재사용)
        Map<String, List<TimetableRequest.ClassInfo>> timetableMap = getTimetableMap(userId);

        // 3. 응답 구성
        return ProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .rating(4.8) // 임시 값
                .timetableData(timetableMap)
                .build();
    }

    // 중복 로직 private 메서드로 분리
    private Map<String, List<TimetableRequest.ClassInfo>> getTimetableMap(Long userId) {
        List<Timetable> entries = timetableRepository.findByUserId(userId);
        Map<String, List<TimetableRequest.ClassInfo>> timetableMap = new HashMap<>();

        for (Timetable entry : entries) {
            TimetableRequest.ClassInfo classInfo = TimetableRequest.ClassInfo.builder()
                    .subject(entry.getSubject())
                    .professor(entry.getProfessor())
                    .startTime(entry.getStartTime())
                    .endTime(entry.getEndTime())
                    .build();
            timetableMap.computeIfAbsent(entry.getDay(), k -> new ArrayList<>()).add(classInfo);
        }
        return timetableMap;
    }
}





