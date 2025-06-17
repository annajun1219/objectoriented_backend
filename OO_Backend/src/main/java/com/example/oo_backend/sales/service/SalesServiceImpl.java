package com.example.oo_backend.sales.service;

import com.example.oo_backend.sales.dto.SalesHistoryResponse;
import com.example.oo_backend.book.entity.Book;
import com.example.oo_backend.book.entity.BookTransaction;
import com.example.oo_backend.book.repository.BookRepository;
import com.example.oo_backend.book.repository.BookTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void createTransactionIfNotExists(Long bookId, Long sellerId, Long buyerId) {
        // ✅ 필드명이 productId 이므로, repository 메서드도 productId 기준으로!
        boolean exists = bookTransactionRepository.existsByProductIdAndSellerIdAndBuyerId(bookId, sellerId, buyerId);

        if (!exists) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 책을 찾을 수 없습니다."));

            BookTransaction newTransaction = new BookTransaction();
            newTransaction.setProductId(bookId); // ✅ productId로 세팅
            newTransaction.setProductTitle(book.getTitle());
            newTransaction.setPrice(book.getPrice());
            newTransaction.setSellerId(sellerId);
            newTransaction.setBuyerId(buyerId);
            newTransaction.setStatus("예약중");

            bookTransactionRepository.save(newTransaction);
        }
    }

    @Override
    public void updateTransactionStatus(Long transactionId, String newStatus) {
        BookTransaction transaction = bookTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래를 찾을 수 없습니다."));

        transaction.setStatus(newStatus);
        bookTransactionRepository.save(transaction);
    }
}
