import Dao.Book;
import Dao.Transaction;
import Dao.User;
import Database.MysqlConnectionImpl;
import Repositories.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TransactionRepoTest {
    @Mock
    private MysqlConnectionImpl mysqlConnection;

    @InjectMocks
    private TransactionRepo transactionRepo;

    Transaction transaction;

    @InjectMocks
    User user;

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

        user.setName("User");
        user.setMobileNumber(1234567891);
        user.setDateOfEnroll(LocalDate.now());
        user.setAddress("Address");
        user.setUserID("1");
        user.setEmail("user@gmail.com");
        user.setBorrowedBooks(null);
    }

    @Test
    public void createTransaction_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        transaction = new Transaction(book, user, LocalDate.now(), null, LocalDate.now().plusDays(7), "Process", 0, 0 , false);

        assertTrue(transactionRepo.createTransaction(transaction));
    }

    @Test
    public void createTransaction_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(transactionRepo.createTransaction(transaction));
    }

    @Test
    public void getTransaction_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        transaction = new Transaction(book, user, LocalDate.now(), null, LocalDate.now().plusDays(7), "Process", 0, 0 , false);
        transactionRepo.createTransaction(transaction);

        assertNotNull(transactionRepo.getTransaction(user.getEmail(), book.getBookTitle()));
    }

    @Test
    public void getTransaction_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        user.setUserID("1");
        book.setBookTitle("Test");

        assertNull(transactionRepo.getTransaction(user.getUserID(), book.getBookTitle()));
    }

    @Test
    public void updateTransaction_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        transaction = new Transaction(book, user, LocalDate.now(), null, LocalDate.now().plusDays(7), "Process", 0, 0 , false);
        transactionRepo.createTransaction(transaction);

        assertTrue(transactionRepo.updateTransaction(transaction));
    }

    @Test
    public void updateTransaction_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(transactionRepo.updateTransaction(transaction));
    }
}
