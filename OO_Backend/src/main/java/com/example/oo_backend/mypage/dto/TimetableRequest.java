package com.example.oo_backend.mypage.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableRequest {
    // 예: { "월": [ { "time": "09:00-10:00", "subject": "객체지향프로그래밍", "professor": "홍길동" }, ... ], "화": [...] }
    private Map<String, List<ClassInfo>> timetableData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassInfo {
        private String time;
        private String subject;
        private String professor;
    }
}

