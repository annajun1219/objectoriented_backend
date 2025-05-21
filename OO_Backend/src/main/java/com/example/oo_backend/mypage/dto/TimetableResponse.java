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
    // 실제 시간표 데이터 구조(예: 요일 -> ClassInfo 목록)
    private Map<String, List<TimetableRequest.ClassInfo>> timetableData;

    // 별도로 시간표 요약 정보를 제공 //필요한지 확인 필요
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleInfo {
        private boolean uploaded;
        private List<String> scheduleSummary;
        private String lastUploaded;
    }
}
