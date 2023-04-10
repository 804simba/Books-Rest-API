package com.timolisa.booksapi.services.impl;

import com.timolisa.booksapi.TestData;
import com.timolisa.booksapi.domain.BookEntity;
import com.timolisa.booksapi.dto.Book;
import com.timolisa.booksapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.timolisa.booksapi.TestData.testBook;
import static com.timolisa.booksapi.TestData.testBookEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void shouldCreateABook() {
        Book book = TestData.testBook();

        BookEntity bookEntity = testBookEntity();

        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);

        final Book actual = bookService.save(book);
        assertEquals(book, actual);
    }

    @Test
    public void testThatFindByIdReturnsEmptyWhenNoBook() {
        final String isbn = "123123123";

        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> result = bookService.findById(isbn);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatFindByIdReturnsBookWhenExists() {
        final Book book = TestData.testBook();
        final BookEntity bookEntity = testBookEntity();

        // when the repo gets a book isbn, it should return
        // an Optional of bookEntity
        when(bookRepository.findById(eq(book.getIsbn())))
                .thenReturn(Optional.of(bookEntity));

        final Optional<Book> result = bookService.findById(book.getIsbn());

        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testListBookReturnsEmptyListWhenNoBookExists() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        final List<Book> result = bookService.listBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void testListBookReturnsBooksWhenExists() {
        final BookEntity bookEntity = testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));

        final List<Book> result = bookService.listBooks();
        assertEquals(1, result.size());
    }

    @Test
    public void testBookExistsReturnFalseWhenBookDoesNotExist() {
        when(bookRepository.existsById(any())).thenReturn(false);
        final boolean result = bookService.isBookExists(testBook());
        assertFalse(result);
    }

    @Test
    public void testBookExistsReturnTrueWhenBookExists() {
        when(bookRepository.existsById(any())).thenReturn(true);
        final boolean result = bookService.isBookExists(testBook());
        assertTrue(result);
    }

    @Test
    public void testDeleteBookDeletesBook() {
        // mockito when is used when a method returns something.
        // mockito verify is used when there is no return value.
        String isbn = "021122223";
        bookService.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }
}