package com.example.oo_backend.mypage.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long userId;
    private String name;
    private String profileImage;
    private double rating; //user entity에 rating 추가 필요
    // timetableData를 직접 포함하여 시간표 정보를 전달
    private Map<String, List<TimetableRequest.ClassInfo>> timetableData;
}

