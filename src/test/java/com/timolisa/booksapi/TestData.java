package com.timolisa.booksapi;

import com.timolisa.booksapi.domain.BookEntity;
import com.timolisa.booksapi.dto.Book;

public final class TestData {
    private TestData() {}

    public static Book testBook() {
        return Book.builder()
                .isbn("02345678")
                .title("The Waves")
                .author("Chris Wallace")
                .build();
    }

    public static BookEntity testBookEntity() {
        return BookEntity.builder()
                .isbn("02345678")
                .title("The Waves")
                .author("Chris Wallace")
                .build();
    }
}
