package com.example.oo_backend.mypage.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableResponse {
    private String userId;
    // 조회 시 파싱한 시간표 데이터, 예: 요일 -> List<ClassInfo>
    private Map<String, List<TimetableRequest.ClassInfo>> timetableData;
}

