package com.example.oo_backend.book.service;


import com.example.oo_backend.mypage.entity.Schedule;
import com.example.oo_backend.mypage.repository.ScheduleRepository;
import com.example.oo_backend.book.dto.BookDetailResponse;
import com.example.oo_backend.book.dto.BookRegisterRequest;
import com.example.oo_backend.book.dto.BookRegisterResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import com.example.oo_backend.book.dto.BookPreviewDto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.CsrfDsl;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public BookRegisterResponse registerBook(BookRegisterRequest request) {
        User user = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Book book = Book.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .professorName(request.getProfessorName())
                .officialPrice(request.getOfficialPrice())
                .price(request.getPrice())
                .description(request.getDescription())
                .sellerId(request.getSellerId())
                .imageUrl(request.getImageUrl())  // 선택적 필드
                .status("판매중")                 // 초기 상태 설정
                .build();

        bookRepository.save(book);

        BookRegisterResponse response = new BookRegisterResponse();
        response.setBookId(book.getId());
        response.setMessage("교재가 등록되었습니다.");
        return response;

    }

    @Override
    public BookDetailResponse getBookDetail(Long productId, Long viewerId) {
        Book book = bookRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 교재가 존재하지 않습니다."));

        User seller = userRepository.findById(book.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

        BookDetailResponse.SellerInfo sellerInfo = BookDetailResponse.SellerInfo.builder()
                .sellerId(seller.getUserId())
                .name(seller.getName())
                .phone(seller.getPhone())
                .profileImage(seller.getProfileImage())
                .build();

        BookDetailResponse response = BookDetailResponse.builder()
                .productId(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .officialPrice(book.getOfficialPrice())
                .discountRate(
                        (book.getOfficialPrice() != 0)
                                ? (int) Math.round((1 - (double) book.getPrice() / book.getOfficialPrice()) * 100)
                                : 0
                )
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .category(book.getCategory()) // ✅ 꼭 들어가야 해
                .professorName(book.getProfessorName()) // ✅ 이것도                .status(book.getStatus())
                .createdAt(book.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .seller(sellerInfo)
                .isMyPost(viewerId != null && viewerId.equals(book.getSellerId()))
                .build();

        return response;
    }

    @Override
    public List<BookPreviewDto> getBooksByDepartment(String departmentName) {
        return bookRepository.findByCategory(departmentName).stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getRecommendedBooksBySchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByTitleAndProfessorName(s.getSubject(), s.getProfessor()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getBooksBySubjectFromSchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByTitleContaining(s.getSubject()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getBooksByProfessorFromSchedule(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        Set<Book> matchedBooks = new HashSet<>();

        for (Schedule s : schedules) {
            matchedBooks.addAll(bookRepository.findByProfessorNameContaining(s.getProfessor()));
        }

        return matchedBooks.stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByProfessorAndCategory(String keyword, String category) {
        return bookRepository.findByProfessorNameContainingAndCategory(keyword, category)
                .stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByTitleAndCategory(String keyword, String category) {
        return bookRepository.findByTitleContainingAndCategory(keyword, category)
                .stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookPreviewDto> searchByProfessor(String keyword) {
        return bookRepository.findByProfessorNameContainingIgnoreCase(keyword).stream()
                .map(book -> new BookPreviewDto(
                        book.getId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getProfessorName(),
                        book.getCategory(),
                        book.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageUsedPrice(String title) {
        List<Book> books = bookRepository.findByTitle(title);

        if (books == null || books.isEmpty()) {
            return 0.0;
        }

        if (books.size() == 1) {
            return books.get(0).getPrice() * 1.0;
        }

        double total = 0.0;
        for (Book book : books) {
            total += book.getPrice();
        }

        return total / books.size();
    }


}

