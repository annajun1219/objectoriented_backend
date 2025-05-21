package com.example.oo_backend.mypage.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String userId;
    private String name;
    private String profileImage;
    private double rating;
    // 시간표 정보는 별도 DTO(여기서는 TimetableResponse.ScheduleInfo 사용)
    private TimetableResponse.ScheduleInfo scheduleInfo;
}

