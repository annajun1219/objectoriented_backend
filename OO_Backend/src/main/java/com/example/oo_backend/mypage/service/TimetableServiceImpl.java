package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.dto.TimetableResponse;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public TimetableResponse getTimetable(Long userId) {
        Map<String, List<TimetableRequest.ClassInfo>> timetableMap = getTimetableMap(userId);
        return TimetableResponse.builder()
                .userId(userId)
                .timetableData(timetableMap)
                .build();
    }

    @Override
    public TimetableResponse saveOrUpdateTimetable(Long userId, TimetableRequest request) {
        // 기존 유저의 시간표 삭제
        timetableRepository.deleteByUserId(userId);

        // 요청된 시간표 데이터를 DB에 행 단위로 저장
        request.getTimetableData().forEach((day, classes) -> {
            for (TimetableRequest.ClassInfo classInfo : classes) {
                Timetable entry = Timetable.builder()
                        .userId(userId)
                        .day(day)
                        .subject(classInfo.getSubject())
                        .professor(classInfo.getProfessor())
                        .startTime(classInfo.getStartTime())
                        .endTime(classInfo.getEndTime())
                        .build();
                timetableRepository.save(entry);
            }
        });

        return getTimetable(userId);
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





