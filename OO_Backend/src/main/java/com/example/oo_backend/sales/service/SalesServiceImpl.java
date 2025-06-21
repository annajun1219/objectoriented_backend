package com.example.oo_backend.sales.service;

import com.example.oo_backend.sales.dto.SalesHistoryResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final BookRepository bookRepository;
    private final BookTransactionRepository bookTransactionRepository;

    @Override
    public List<SalesHistoryResponse> getSalesHistory(Long sellerId, String status) {
        if (status == null || status.equals("판매중")) {
            return bookRepository.findBySellerIdAndStatus(sellerId, "판매중").stream()
                    .map(book -> new SalesHistoryResponse(
                            book.getId(),
                            book.getTitle(),
                            book.getPrice(),
                            book.getImageUrl(),
                            book.getStatus(),
                            book.getCreatedAt().toLocalDate()
                    ))
                    .collect(Collectors.toList());
        } else if (status.equals("전체")) {
            List<SalesHistoryResponse> selling = bookRepository.findBySellerIdAndStatus(sellerId, "판매중").stream()
                    .map(book -> new SalesHistoryResponse(
                            book.getId(),
                            book.getTitle(),
                            book.getPrice(),
                            book.getImageUrl(),
                            book.getStatus(),
                            book.getCreatedAt().toLocalDate()
                    ))
                    .collect(Collectors.toList());

            List<SalesHistoryResponse> reserved = bookTransactionRepository.findBySellerIdAndStatus(sellerId, "예약중").stream()
                    .map(tx -> new SalesHistoryResponse(
                            tx.getProductId(),
                            tx.getProductTitle(),
                            tx.getPrice(),
                            null,
                            tx.getStatus(),
                            tx.getCreatedAt().toLocalDate()
                    ))
                    .collect(Collectors.toList());

            selling.addAll(reserved);
            return selling;
        } else {
            return bookTransactionRepository.findBySellerIdAndStatus(sellerId, status).stream()
                    .map(tx -> new SalesHistoryResponse(
                            tx.getProductId(),
                            tx.getProductTitle(),
                            tx.getPrice(),
                            null,
                            tx.getStatus(),
                            tx.getCreatedAt().toLocalDate()
                    ))
                    .collect(Collectors.toList());
        }
    }


    @Override
    public Long createTransactionIfNotExists(Long bookId, Long sellerId, Long buyerId) {
        boolean exists = bookTransactionRepository.existsByProductIdAndSellerIdAndBuyerId(bookId, sellerId, buyerId);

        if (exists) {
            // 이미 존재하는 경우 기존 거래 ID 반환
            return bookTransactionRepository.findByProductIdAndSellerIdAndBuyerId(bookId, sellerId, buyerId)
                    .orElseThrow(() -> new IllegalStateException("거래가 존재하지만 조회에 실패했습니다."))
                    .getId();
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 책을 찾을 수 없습니다."));

        BookTransaction newTransaction = new BookTransaction();
        newTransaction.setProductId(bookId);
        newTransaction.setProductTitle(book.getTitle());
        newTransaction.setPrice(book.getPrice());
        newTransaction.setSellerId(sellerId);
        newTransaction.setBuyerId(buyerId);
        newTransaction.setStatus("예약중");

        BookTransaction saved = bookTransactionRepository.save(newTransaction);
        return saved.getId(); // ✅ 생성된 거래 ID 반환
    }

    @Override
    public void updateBookStatus(Long bookId, String status) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
        book.setStatus(status);
        bookRepository.save(book);
    }




    @Override
    public void updateTransactionStatus(Long transactionId, String newStatus) {
        Optional<BookTransaction> optionalTransaction = bookTransactionRepository.findById(transactionId);

        if (optionalTransaction.isPresent()) {
            BookTransaction transaction = optionalTransaction.get();
            transaction.setStatus(newStatus);
            bookTransactionRepository.save(transaction);
        } else {
            // 거래가 없지만 판매완료로 바꾸려는 경우 → Book 상태 직접 바꿔주기
            if (newStatus.equals("판매완료")) {
                // transactionId == bookId 라고 가정하고 Book 찾기
                Book book = bookRepository.findById(transactionId)
                        .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
                book.setStatus("판매완료");
                bookRepository.save(book);
            } else {
                throw new IllegalArgumentException("해당 거래를 찾을 수 없습니다.");
            }
        }
    }

}
