package DriverClass;

import Dao.*;

import java.util.List;
import java.util.Map;

public interface LibraryManagement {

    //Book Related Operations
    boolean addBookToShelf(Book book);
    boolean updateBookDetails(Book book);
    boolean removeBookFromShelf(String bookId);
    Book getBookDetails(String bookID);


    // User Management
    boolean registerUser(User user);
    boolean updateUserDetails(User user);
    boolean deleteUser(String userId);
    User getUser(String userId);


    // Circulation Management
    boolean checkoutBook(String userId, String bookTitle);
    boolean returnBook(String userId, String bookTitle);
    boolean renewBook(String userId, String bookTitle);

}
