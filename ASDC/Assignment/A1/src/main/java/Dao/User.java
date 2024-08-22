package Dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class User {

    private String name;
    private String userID;
    private long mobileNumber;
    private String email;
    private String address;
    private LocalDate dateOfEnroll;
    private List<Map<LocalDate, Book>> borrowedBooks;

    public User(){}

    public User(String name, String userID, long mobileNumber, String email, String address, LocalDate dateOfEnroll, List<Map<LocalDate, Book>> borrowedBooks) {
        this.name = name;
        this.userID = userID;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.address = address;
        this.dateOfEnroll = dateOfEnroll;
        this.borrowedBooks = borrowedBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfEnroll() {
        return dateOfEnroll;
    }

    public void setDateOfEnroll(LocalDate dateOfEnroll) {
        this.dateOfEnroll = dateOfEnroll;
    }

    public List<Map<LocalDate, Book>> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Map<LocalDate, Book>> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userID='" + userID + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dateOfEnroll=" + dateOfEnroll +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
