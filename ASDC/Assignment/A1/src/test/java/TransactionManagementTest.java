import Dao.Book;
import Dao.Transaction;
import Dao.User;
import Services.*;
import DriverClass.LibraryManagementSystem;
import Log.LogWriter;
import Log.TxtLogWriter;
import Repositories.BookRepo;
import Repositories.TransactionRepo;
import Repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TransactionManagementTest {


    @Mock
    private UserRepo userRepo;
    @Mock
    private BookRepo bookRepo;
    @Mock
    private TransactionRepo transactionRepo;
    @InjectMocks
    private TransactionManagement transactionManagement;
    @InjectMocks
    private User user;
    @InjectMocks
    private Book book;
    @InjectMocks
    private Transaction transaction;
    @InjectMocks
    LibraryManagementSystem libraryManagementSystem;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        transactionManagement = new TransactionManagement(userRepo, bookRepo, transactionRepo);
        libraryManagementSystem = new LibraryManagementSystem();
        libraryManagementSystem.setTransactionManagement(transactionManagement);

        user = new User();
        user.setUserID("1");
        user.setEmail("jay@gmail.com");
        user.setName("John Doe");
        user.setMobileNumber(1234567890);
        user.setAddress("123, Example St.");

        book = new Book();
        book.setBookTitle("The Great Book");
        book.setAuthorName("Smit");
        book.setPublishedYear(2000);
        book.setPublisherName("Articles");
        book.setAuthorEmail("smit@gmail.com");
        book.setPublisherEmail("articles@gmail.com");
        book.setPublisherAddress("Monastery Lane");
    }


    //Junit test cases to borrow book
    @Test
    void nullEmptyUserIdCheckoutBook() {
        assertFalse(libraryManagementSystem.checkoutBook("", book.getBookTitle()), "empty user id");
        assertFalse(libraryManagementSystem.checkoutBook(null, book.getBookTitle()), "null user id");
    }

    @Test
    void nullEmptyBookTitleCheckoutBook() {
        assertFalse(libraryManagementSystem.checkoutBook(user.getUserID(), ""), "empty book title");
        assertFalse(libraryManagementSystem.checkoutBook(user.getUserID(), null), "null book title");
    }


    //Mockito Test Cases to borrow Book.
    @Test
    void checkoutBook_Success() {

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.createTransaction(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.checkoutBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void checkoutBook_FailureByUserNotFound() {
        when(userRepo.getUser(anyString())).thenReturn(null);

        assertFalse(libraryManagementSystem.checkoutBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void checkoutBook_FailureByBookNotFound() {
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(null);

        assertFalse(libraryManagementSystem.checkoutBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void checkoutBook_FailureByDatabaseError() {
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.createTransaction(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.checkoutBook(user.getUserID(), book.getBookTitle()));
    }

    //Junit Test cases to return book.
    @Test
    void nullEmptyUserIdReturnBook() {
        assertFalse(libraryManagementSystem.returnBook("", book.getBookTitle()), "empty user id");
        assertFalse(libraryManagementSystem.returnBook(null, book.getBookTitle()), "null user id");
    }

    @Test
    void nullEmptyBookTitleReturnBook() {
        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), ""), "empty book title");
        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), null), "null book title");
    }


    //Mockito Test Cases to return Book.
    @Test
    void returnBook_Success() {

        transaction = new Transaction(book, user, LocalDate.now(), null, LocalDate.now().plusDays(7), "Testing", 0, 0, false);

        System.out.println(transaction.getStatus());

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void returnBook_SuccessWithLateReturn() {

        transaction = new Transaction(book, user, LocalDate.now().minusDays(10), null, LocalDate.now().minusDays(7), "Testing", 0, 0, false);

        System.out.println(transaction.getIssueDate());
        System.out.println(transaction.isBookReturned());

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void returnBook_FailureByUserNotFound() {
        when(userRepo.getUser(anyString())).thenReturn(null);

        System.out.println(transaction.getUser());

        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void returnBook_FailureByBookNotFound() {
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(null);

        System.out.println(transaction.getBook());

        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void returnBook_FailureByTransactionNotFound() {
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(null);

        System.out.println(transaction.getAmount());

        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void returnBook_FailureByDatabaseError() {

        transaction = new Transaction(book, user, LocalDate.now(), null, LocalDate.now().plusDays(7), "Testing", 0, 0, false);

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.returnBook(user.getUserID(), book.getBookTitle()));
    }


    //Junit Test cases to renew book.
    @Test
    void nullEmptyUserIdRenewBook() {
        assertFalse(libraryManagementSystem.renewBook("", book.getBookTitle()), "empty user id");
        assertFalse(libraryManagementSystem.renewBook(null, book.getBookTitle()), "null user id");
    }

    @Test
    void nullEmptyBookTitleRenewBook() {
        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), ""), "empty book title");
        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), null), "null book title");
    }


    //Mockito Test Case to renew Book.
    @Test
    void renewBook_Success() {

        transaction.setRenewCount(2);
        transaction.setExpectedReturnedDate(LocalDate.now());

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_SuccessWithLatereturn() {

        transaction = new Transaction(book, user, LocalDate.now().minusDays(10), null, LocalDate.now().minusDays(7), "Testing", 0, 0, false);

        transaction.setRenewCount(2);
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_FailureByRenewLimitExceeded() {
        transaction.setRenewCount(4);
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);

        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_FailureByUserNotFound() {
        transaction.setRenewCount(2);
        when(userRepo.getUser(anyString())).thenReturn(null);

        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_FailureByBookNotFound() {
        transaction.setRenewCount(2);
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);

        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_FailureByTransactionNotFound() {
        transaction.setRenewCount(2);
        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(null);

        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    @Test
    void renewBook_FailureByDatabaseError() {
        transaction.setRenewCount(2);
        transaction.setExpectedReturnedDate(LocalDate.now());

        when(userRepo.getUser(anyString())).thenReturn(user);
        when(bookRepo.getBook(anyString())).thenReturn(book);
        when(transactionRepo.getTransaction(anyString(), anyString())).thenReturn(transaction);
        when(transactionRepo.updateTransaction(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.renewBook(user.getUserID(), book.getBookTitle()));
    }

    //Junit Test case for LogWriter
    @Test
    void InvalidFilePath() {

        String invalidLogFilePath = null;
        LogWriter logWriter = TxtLogWriter.getInstance();
        String type = "ERROR";
        String module = "TestModule";
        String logMessage = "This is a test log message";

        libraryManagementSystem.SetLogFilePath(invalidLogFilePath);
        assertFalse(logWriter.Write(type, module, logMessage));
    }

}
