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
    private Long userId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String timetableData;
}
