package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.entity.Schedule;
import com.example.oo_backend.mypage.repository.ScheduleRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody Schedule request) {
        User user = userRepository.findById(request.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        System.out.println("📩 시간표 등록 요청 들어옴: " + request.getDay() + ", 유저 ID: " + request.getUser().getUserId());


        Schedule newSchedule = Schedule.builder()
                .user(user)
                .day(request.getDay())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .subject(request.getSubject())
                .professor(request.getProfessor())
                .build();

        scheduleRepository.save(newSchedule);
        return ResponseEntity.ok("스케줄이 등록되었습니다.");
    }
}
