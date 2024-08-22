package Dao;

import java.time.LocalDate;

public class Transaction {

    private Book book;
    private User user;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private LocalDate expectedReturnedDate;
    private String status;
    private int renewCount;
    private int amount;
    private boolean isBookReturned;

    public Transaction(Book book, User user, LocalDate issueDate, LocalDate returnDate, LocalDate expectedReturnedDate, String status, int renewCount, int amount, boolean isBookReturned) {
        this.book = book;
        this.user = user;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.expectedReturnedDate = expectedReturnedDate;
        this.status = status;
        this.renewCount = renewCount;
        this.amount = amount;
        this.isBookReturned = isBookReturned;
    }

    public Transaction(){}

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getExpectedReturnedDate() {
        return expectedReturnedDate;
    }

    public void setExpectedReturnedDate(LocalDate expectedReturnedDate) {
        this.expectedReturnedDate = expectedReturnedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(int renewCount) {
        this.renewCount = renewCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBookReturned() {
        return isBookReturned;
    }

    public void setBookReturned(boolean bookReturned) {
        isBookReturned = bookReturned;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "book=" + book +
                ", user=" + user +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                ", expectedReturnedDate=" + expectedReturnedDate +
                ", status='" + status + '\'' +
                ", renewCount=" + renewCount +
                ", amount=" + amount +
                ", isBookReturned=" + isBookReturned +
                '}';
    }
}
