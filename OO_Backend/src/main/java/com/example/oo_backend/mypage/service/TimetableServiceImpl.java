package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.dto.TimetableResponse;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class TimetableServiceImpl implements TimetableService {

    private static final Logger logger = LoggerFactory.getLogger(TimetableServiceImpl.class);

    private final TimetableRepository timetableRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
        this.objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper 초기화
    }

    @Override
    public TimetableResponse getTimetable(Long userId) {
        // DB에서 Long타입 userId를 바탕으로 Timetable 을 조회합니다.
        Timetable timetable = timetableRepository.findByUserId(userId);
        if (timetable != null && timetable.getTimetableData() != null && !timetable.getTimetableData().isEmpty()) {
            try {
                // 저장된 JSON 문자열을 Map<String, List<TimetableRequest.ClassInfo>>로 파싱
                Map<String, java.util.List<TimetableRequest.ClassInfo>> timetableDataMap =
                        objectMapper.readValue(
                                timetable.getTimetableData(),
                                new TypeReference<>() {}
                        );
                return TimetableResponse.builder()
                        .userId(userId)
                        .timetableData(timetableDataMap)
                        .build();
            } catch (Exception e) {
                logger.error("타임테이블 데이터를 파싱하지 못했습니다. userId={}", userId, e);
            }
        }
        return TimetableResponse.builder()
                .userId(userId)
                .timetableData(Collections.emptyMap())
                .build();
    }

    @Override
    public TimetableResponse saveOrUpdateTimetable(Long userId, TimetableRequest request) {
        // DB에서 해당 사용자의 Timetable 데이터를 조회, 없으면 새로 생성
        Timetable timetable = timetableRepository.findByUserId(userId);
        if (timetable == null) {
            timetable = new Timetable();
            timetable.setUserId(userId); // Timetable 엔티티의 userId도 Long 타입이어야 합니다.
        }
        try {
            // 사용자의 Map 데이터를 JSON 문자열로 변환하여 저장
            String jsonData = objectMapper.writeValueAsString(request.getTimetableData());
            timetable.setTimetableData(jsonData);
        } catch (Exception e) {
            logger.error("타임테이블 요청 데이터를 JSON 문자열로 변환하지 못했습니다. userId={}", userId, e);
        }
        timetableRepository.save(timetable);
        // 저장 후 최신 상태 데이터를 조회하여 반환
        return getTimetable(userId);
    }
}



