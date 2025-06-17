package com.example.oo_backend.book.dto;

import lombok.*;

@NoArgsConstructor  // ✅ 이거 꼭 붙이기
@AllArgsConstructor
@Getter
@Setter
public class BookRegisterResponse {
    private Long bookId;
    private String message;
}
