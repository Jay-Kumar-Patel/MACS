import Dao.Book;
import DriverClass.LibraryManagementSystem;
import DriverClass.UtilFunctions;
import Repositories.BookRepo;
import Services.BookManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BookManagementTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private LibraryManagementSystem libraryManagementSystem;

    @InjectMocks
    UtilFunctions utilFunctions;

    @InjectMocks
    BookManagement bookManagement;

    @InjectMocks
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookManagement = new BookManagement(utilFunctions, bookRepo);

        libraryManagementSystem = new LibraryManagementSystem();
        libraryManagementSystem.SetBookManagement(bookManagement);

        book = new Book();
        book.setBookTitle("The Great Book");
        book.setAuthorName("Smit");
        book.setPublishedYear(2000);
        book.setPublisherName("Articles");
        book.setAuthorEmail("smit@gmail.com");
        book.setPublisherEmail("articles@gmail.com");
        book.setPublisherAddress("Monastery Lane");
    }


    //Junit Test Case For Insert and Update Book.
    @Test
    void nullEmptyBookTitle() {
        book.setBookTitle("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty book title");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty book title");

        book.setBookTitle(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null book title");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null book title");
    }

    @Test
    void nullEmptyAuthorName() {
        book.setAuthorName("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty author name");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty author name");

        book.setAuthorName(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null author name");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null author name");
    }

    @Test
    void nullEmptyPublishedYear() {
        book.setPublishedYear(1000);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "published year less than 1900");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "published year less than 1900");

        book.setPublishedYear(2030);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "published year more than current year(2024)");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "published year more than current year(2024)");
    }

    @Test
    void nullEmptyPublishedName() {
        book.setPublisherName("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty published name");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty published name");

        book.setPublisherName(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null published name");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null published name");
    }

    @Test
    void nullEmptyAuthorEmail() {
        book.setAuthorEmail("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty author email");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty author email");

        book.setAuthorEmail(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null author email");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null author email");
    }

    @Test
    void nullEmptyPublisherEmail() {
        book.setPublisherEmail("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty publisher email");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty publisher email");

        book.setPublisherEmail(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null publisher email");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null publisher email");
    }

    @Test
    void nullEmptyPublisherAddress() {
        book.setPublisherAddress("");
        assertFalse(libraryManagementSystem.addBookToShelf(book), "empty publisher address");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "empty publisher address");

        book.setPublisherAddress(null);
        assertFalse(libraryManagementSystem.addBookToShelf(book), "null publisher address");
        assertFalse(libraryManagementSystem.updateBookDetails(book), "null publisher address");
    }


    //Mockito Test Case For Insert New Book.
    @Test
    void addBookToShelf_Success() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(false);
        when(bookRepo.addBook(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.addBookToShelf(book));
    }

    @Test
    void addBookToShelf_FailureByBookRepetition() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);

        assertFalse(libraryManagementSystem.addBookToShelf(book));
    }

    @Test
    void addBookToShelf_FailureByDatabaseError() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(false);
        when(bookRepo.addBook(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.addBookToShelf(book));
    }


    //Mockito Test Case For Update Book Details.
    @Test
    void updateBookDetails_Success() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.editBook(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.updateBookDetails(book));
    }

    @Test
    void updateBookDetails_FailureByBookRepetition() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(false);

        assertFalse(libraryManagementSystem.updateBookDetails(book));
    }

    @Test
    void updateBookDetails_FailureByDatabaseError() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.editBook(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.updateBookDetails(book));
    }


    //Junit Test Case For Book deletion.
    @Test
    void nullEmptyBookTitleForDeletion() {
        book.setBookTitle("");
        assertFalse(libraryManagementSystem.removeBookFromShelf(book.getBookTitle()), "empty book title");


        book.setBookTitle(null);
        assertFalse(libraryManagementSystem.removeBookFromShelf(book.getBookTitle()), "null book title");
    }


    //Mockito Test Case For Book deletion.
    @Test
    void deleteBook_Success() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.deleteBook(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.removeBookFromShelf(book.getBookTitle()));
    }

    @Test
    void deleteBook_FailureByBookNotFound() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(false);

        assertFalse(libraryManagementSystem.removeBookFromShelf(book.getBookTitle()));
    }

    @Test
    void deleteBook_FailureByDatabaseError() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.deleteBook(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.removeBookFromShelf(book.getBookTitle()));
    }


    //Junit Test Case to Fetch Book details.
    @Test
    void nullEmptyBookTitleForFetchData() {
        book.setBookTitle("");
        assertEquals(null, libraryManagementSystem.getBookDetails(book.getBookTitle()), "empty book title");


        book.setBookTitle(null);
        assertEquals(null, libraryManagementSystem.getBookDetails(book.getBookTitle()), "null book title");
    }


    //Mockito Test Case to Fetch Book details.
    @Test
    void getBook_Success() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.getBook(anyString())).thenReturn(book);

        assertEquals(book, libraryManagementSystem.getBookDetails(book.getBookTitle()));
    }

    @Test
    void getBook_FailureByBookNotFound() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(false);

        assertEquals(null, libraryManagementSystem.getBookDetails(book.getBookTitle()));
    }

    @Test
    void getBook_FailureByDatabaseError() {

        when(bookRepo.isBookAlreadyExist(anyString())).thenReturn(true);
        when(bookRepo.getBook(anyString())).thenReturn(null);

        assertEquals(null, libraryManagementSystem.getBookDetails(book.getBookTitle()));
    }

}
