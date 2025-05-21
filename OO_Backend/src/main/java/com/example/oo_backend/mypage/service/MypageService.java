package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.ProfileResponse;

public interface MypageService {
    ProfileResponse getMypageInfo(String userId);
}

