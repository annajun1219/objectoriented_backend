package com.example.oo_backend.mypage.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    // 예를 들어 JSON 문자열로 저장 (실제 구현에 따라 별도 테이블로 정규화할 수도 있음)
    @Lob
    private String timetableData;
}
