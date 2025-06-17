package com.example.oo_backend.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookPreviewDto {

    // JSON에서는 "id" → 자바 필드는 productId
    @JsonProperty("id")
    private Long productId;

    private String title;
    private int price;
    private String professorName;
    private String category;
    private String imageUrl;
}

