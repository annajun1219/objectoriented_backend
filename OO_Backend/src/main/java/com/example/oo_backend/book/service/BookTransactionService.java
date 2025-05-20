package com.example.oo_backend.book.service;

import com.example.oo_backend.book.dto.BookPurchaseRequest;
import com.example.oo_backend.book.dto.BookPurchaseResponse;

import java.util.UUID;

public interface BookTransactionService {
    BookPurchaseResponse createDirectTransaction(UUID buyerId, BookPurchaseRequest request);
}
