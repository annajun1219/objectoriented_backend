package com.example.oo_backend.mypage.service;

import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import com.example.oo_backend.mypage.dto.MyPageResponseDto;
import com.example.oo_backend.mypage.dto.ScheduleDto;
import com.example.oo_backend.mypage.repository.ScheduleRepository;
import com.example.oo_backend.review.repository.ReviewRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final BookTransactionRepository bookTransactionRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public MyPageResponseDto getMyPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        double rating = reviewRepository.getAverageRatingByUser(user);
        int reviewCount = reviewRepository.countByTargetUser(user);
        int saleCount = bookRepository.countBySeller(user);
        int purchaseCount = bookTransactionRepository.countByBuyer(user);
        List<ScheduleDto> schedule = scheduleRepository.findByUser(user).stream()
                .map(s -> new ScheduleDto(s.getDay(), s.getTime(), s.getSubject(), s.getProfessor()))
                .collect(Collectors.toList());

        return MyPageResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getName())
                .profileImage(user.getProfileImage())
                .rating(rating)
                .reviewCount(reviewCount)
                .saleCount(saleCount)
                .purchaseCount(purchaseCount)
                .scheduleInfo(schedule)
                .build();
    }
}
