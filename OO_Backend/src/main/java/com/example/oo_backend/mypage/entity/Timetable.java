package com.example.oo_backend.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String day;        // 예: "월", "화" 등
    private String subject;    // 과목명
    private String professor;  // 교수명
    private String startTime;  // 시작 시간 ("09:00")
    private String endTime;    // 종료 시간 ("10:00")
}
