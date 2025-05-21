package com.example.oo_backend.mypage.service;

import com.example.oo_backend.mypage.dto.ProfileResponse;
import com.example.oo_backend.mypage.dto.TimetableRequest;
import com.example.oo_backend.mypage.entity.Timetable;
import com.example.oo_backend.mypage.repository.TimetableRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MypageServiceImpl implements MypageService {

    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);

    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MypageServiceImpl(TimetableRepository timetableRepository, UserRepository userRepository) {
        this.timetableRepository = timetableRepository;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper 초기화
    }

    @Override
    public ProfileResponse getMypageInfo(Long userId) {
        // 1. 사용자 정보 조회: 외부에서 전달된 userId(Long)를 그대로 사용하여 DB에서 User 엔티티 조회
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.orElseGet(() -> {
            logger.warn("userId {} 에 해당하는 사용자를 찾을 수 없습니다.", userId);
            User defaultUser = new User();
            defaultUser.setUserId(userId);
            defaultUser.setName("기본 이름");
            defaultUser.setProfileImage("https://default.site/profile.png");
            return defaultUser;
        });

        // 2. 시간표 정보 조회 (DB에서 Timetable 엔티티 조회)
        // 이제 timetableRepository의 findByUserId 메서드도 Long 타입을 인자로 받도록 일관되게 수정했다고 가정합니다.
        Timetable timetable = timetableRepository.findByUserId(userId);
        Map<String, List<TimetableRequest.ClassInfo>> timetableData;
        if (timetable != null && timetable.getTimetableData() != null && !timetable.getTimetableData().isEmpty()) {
            try {
                // 저장된 JSON 문자열을 Map<String, List<TimetableRequest.ClassInfo>>로 파싱
                timetableData = objectMapper.readValue(
                        timetable.getTimetableData(),
                        new TypeReference<>() {}
                );
            } catch (Exception e) {
                logger.error("타임테이블 데이터를 파싱하지 못했습니다. userId={}", userId, e);
                timetableData = Collections.emptyMap();
            }
        } else {
            timetableData = Collections.emptyMap();
        }

        // 3. ProfileResponse DTO 구성: DB에서 조회한 User 엔티티의 정보를 사용
        return ProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                // 평점은 아직 User 엔티티에 포함되어 있지 않으므로 기본값 사용 (혹은 별도의 계산 로직 도입)
                .rating(4.8)
                .timetableData(timetableData)
                .build();
    }
}




