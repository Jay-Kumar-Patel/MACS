package Services;

import Dao.*;
import Log.LogWriter;
import Log.TxtLogWriter;
import Repositories.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TransactionManagement {

    private UserRepo userRepo;
    private BookRepo bookRepo;
    private LogWriter logWriter;
    private TransactionRepo transactionRepo;

    public TransactionManagement(UserRepo userRepo, BookRepo bookRepo, TransactionRepo transactionRepo){
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.transactionRepo = transactionRepo;
        logWriter = TxtLogWriter.getInstance();
    }

    public boolean checkoutBook(String userEmail, String bookTitle) {

        if (validateUserAndBook(userEmail, bookTitle)) {

            Transaction transaction = new Transaction();
            transaction.setBook(bookRepo.getBook(bookTitle));
            transaction.setUser(userRepo.getUser(userEmail));
            transaction.setIssueDate(LocalDate.now());
            transaction.setReturnDate(null);
            transaction.setExpectedReturnedDate(LocalDate.now().plusDays(7));
            transaction.setStatus("CheckOut");
            transaction.setRenewCount(0);
            transaction.setAmount(0);
            transaction.setBookReturned(false);

            if (transactionRepo.createTransaction(transaction)) {
                System.out.println("Book Checkout is successfully performed!");
                logWriter.Write("Success", "TransactionManagement", "Book checked out successfully!");
                return true;
            } else {
                System.out.println("Something went wrong, Please try again later!");
                transaction.setStatus("Failed");
                logWriter.Write("Failure", "TransactionManagement", "Failed to check out book!");
            }
        }

        return false;
    }

    public boolean returnBook(String userEmail, String bookTitle) {

        if (validateUserAndBook(userEmail, bookTitle)) {

            Transaction transaction = transactionRepo.getTransaction(userEmail, bookTitle);

            if (transaction != null) {

                transaction.setStatus("Returned");
                transaction.setReturnDate(LocalDate.now());

                System.out.println(transaction.getExpectedReturnedDate());

                if (transaction.getExpectedReturnedDate().isBefore(transaction.getReturnDate())){
                    long daysLate = ChronoUnit.DAYS.between(transaction.getExpectedReturnedDate(), LocalDate.now());
                    int amount = (int)daysLate * 10;
                    transaction.setAmount(amount);
                }

                transaction.setBookReturned(true);

                if (transactionRepo.updateTransaction(transaction)) {
                    System.out.println("Book is successfully returned!");
                    logWriter.Write("Success", "TransactionManagement", "Book returned successfully!");
                    return true;
                } else {
                    System.out.println("Something went wrong, Please try again later!");
                    logWriter.Write("Failure", "TransactionManagement", "Failed to return book!");
                }
            }
            else {
                System.out.println("Data Not Found!");
            }
        }
        return false;
    }


    public boolean renewBook(String userEmail, String bookTitle) {

        if (validateUserAndBook(userEmail, bookTitle)) {

            Transaction transaction = transactionRepo.getTransaction(userEmail, bookTitle);

            if (transaction != null) {
                if (transaction.getRenewCount() < 3) {

                    transaction.setReturnDate(LocalDate.now());

                    if (transaction.getExpectedReturnedDate().isBefore(transaction.getReturnDate())) {
                        long daysLate = ChronoUnit.DAYS.between(transaction.getExpectedReturnedDate(), LocalDate.now());
                        int amount = (int) daysLate * 10;
                        transaction.setAmount(amount);
                    }

                    transaction.setExpectedReturnedDate(LocalDate.now().plusDays(7));
                    transaction.setRenewCount(transaction.getRenewCount() + 1);
                    transaction.setBookReturned(false);

                    if (transactionRepo.updateTransaction(transaction)) {
                        logWriter.Write("Success", "TransactionManagement", "Book renewed successfully!");
                        return true;
                    } else {
                        logWriter.Write("Failure", "TransactionManagement", "Failed to renew book!");
                    }
                } else {
                    System.out.println("More than 3 renew is allowed!");
                }
            }
            else {
                System.out.println("Data Not Found!");
            }
        }

        return false;
    }

    private boolean validateUserAndBook(String userEmail, String bookTitle) {

        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("UserID is empty, Please enter valid userID!");
            return false;
        }

        if (bookTitle == null || bookTitle.isEmpty()){
            System.out.println("Book Title is empty, Please enter valid book title!");
            return false;
        }

        User user = userRepo.getUser(userEmail);
        Book book = bookRepo.getBook(bookTitle);

        if (user == null) {
            System.out.println("User Doesn't Exist!");
            logWriter.Write("Failure", "TransactionManagement", "User does not exist!");
            return false;
        }
        if (book == null) {
            System.out.println("Book Doesn't Exist!");
            logWriter.Write("Failure", "TransactionManagement", "Book does not exist!");
            return false;
        }

        return true;
    }
}
