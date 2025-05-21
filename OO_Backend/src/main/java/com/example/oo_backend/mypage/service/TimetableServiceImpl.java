package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.dto.TimetableResponse;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public TimetableResponse getTimetable(String userId) {
        Timetable timetable = timetableRepository.findByUserId(userId);
        // 실제 구현에서는 timetable.getTimetableData()를 파싱하여 Map<String, List<TimetableRequest.ClassInfo>>로 변환합니다.
        return TimetableResponse.builder()
                .userId(userId)
                .timetableData(java.util.Collections.emptyMap())
                .build();
    }

    @Override
    public TimetableResponse saveOrUpdateTimetable(String userId, TimetableRequest request) {
        Timetable timetable = timetableRepository.findByUserId(userId);
        if (timetable == null) {
            timetable = new Timetable();
            timetable.setUserId(userId);
        }
        // 예: request.getTimetableData()를 JSON 문자열로 변환 (실제 구현에는 ObjectMapper 사용)
        timetable.setTimetableData(request.getTimetableData().toString());
        timetable = timetableRepository.save(timetable);
        return TimetableResponse.builder()
                .userId(userId)
                .timetableData(java.util.Collections.emptyMap())
                .build();
    }
}

