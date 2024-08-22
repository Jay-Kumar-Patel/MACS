import Dao.Book;
import Database.MysqlConnectionImpl;
import Repositories.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BookRepoTest {
    @Mock
    private MysqlConnectionImpl mysqlConnection;

    @InjectMocks
    private BookRepo bookRepo;

    @InjectMocks
    Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        book.setBookTitle("BookTitle");
        book.setAuthorName("authorName");
        book.setAuthorEmail("aEmail@gmail.com");
        book.setPublisherName("PublisherName");
        book.setPublisherEmail("pEmail@gmail.com");
        book.setPublisherAddress("PublisherAddress");
        book.setPublishedYear(2000);
    }

    @Test
    public void addBook_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        assertTrue(bookRepo.addBook(book));
    }

    @Test
    public void addBook_FailureByDatabaseError() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(bookRepo.addBook(book));
    }

    @Test
    public void editBook_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        bookRepo.addBook(book);

        assertTrue(bookRepo.editBook(book));
    }

    @Test
    public void editBook_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(bookRepo.editBook(book));
    }

    @Test
    public void deleteBook_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        bookRepo.addBook(book);

        assertTrue(bookRepo.deleteBook(book.getBookTitle()));
    }

    @Test
    public void deleteBook_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(bookRepo.deleteBook(book.getBookTitle()));
    }

    @Test
    public void getBook_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        bookRepo.addBook(book);

        assertNotNull(bookRepo.getBook(book.getBookTitle()));
    }

    @Test
    public void getBook_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertNull(bookRepo.getBook(book.getBookTitle()));
    }

    @Test
    public void bookAlreadyExist_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        bookRepo.addBook(book);

        assertTrue(bookRepo.isBookAlreadyExist(book.getBookTitle()));
    }

    @Test
    public void bookAlreadyExist_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(bookRepo.isBookAlreadyExist(book.getBookTitle()));
    }
}
