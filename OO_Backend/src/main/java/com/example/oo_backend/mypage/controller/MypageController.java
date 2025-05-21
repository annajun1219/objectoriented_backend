package com.example.oo_backend.mypage.controller;

import com.example.oo_backend.mypage.dto.ProfileResponse;
import com.example.oo_backend.mypage.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @Autowired
    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    /**
     * 마이페이지 전체 정보를 조회
     * URL 예: GET /api/mypage?userId=user-123abc
     */
    @GetMapping
    public ProfileResponse getMypage(@RequestParam("userId") String userId) {
        return mypageService.getMypageInfo(userId);
    }
}



