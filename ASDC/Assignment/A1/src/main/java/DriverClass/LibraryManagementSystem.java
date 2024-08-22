package DriverClass;

import Dao.*;
import Log.LogWriter;
import Log.TxtLogWriter;
import Repositories.BookRepo;
import Repositories.TransactionRepo;
import Repositories.UserRepo;
import Services.*;
import DriverClass.UtilFunctions;
import Services.*;

import java.time.LocalDate;
import java.util.Scanner;

public class LibraryManagementSystem implements LibraryManagement{

    private BookManagement bookManagement;
    private TransactionManagement transactionManagement;
    private UserManagement userManagement;
    private UserRepo userRepo;
    private BookRepo bookRepo;
    private TransactionRepo transactionRepo;
    private UtilFunctions utilFunctions;
    private LogWriter logWriter;

    public LibraryManagementSystem(){
        utilFunctions = new UtilFunctions();
        bookRepo = new BookRepo();
        transactionRepo = new TransactionRepo();
        userRepo = new UserRepo();
        bookManagement = new BookManagement(utilFunctions, bookRepo);
        transactionManagement = new TransactionManagement(userRepo, bookRepo, transactionRepo);
        userManagement = new UserManagement(userRepo, utilFunctions);
        logWriter = TxtLogWriter.getInstance();
    }

    public void SetLogFilePath(String logfilePath){
        logWriter.setFilePath(logfilePath);
    }

    public void SetBookManagement(BookManagement bookManagement){
        this.bookManagement = bookManagement;
    }

    public void SetUserManagement(UserManagement userManagement){
        this.userManagement = userManagement;
    }

    public void setTransactionManagement(TransactionManagement transactionManagement){
        this.transactionManagement = transactionManagement;
    }


    public static void main(String[] args) {

        LibraryManagementSystem libraryManagement = new LibraryManagementSystem();
        libraryManagement.run();
    }


    private void displayMenu() {
        System.out.println("\n--- Welcome to this amazing Library Management System ---");
        System.out.println("1. Add Book to Shelf");
        System.out.println("2. Update Book Details");
        System.out.println("3. Remove Book from Shelf");
        System.out.println("4. Get Book Details");
        System.out.println("5. Register User");
        System.out.println("6. Update User Details");
        System.out.println("7. Delete User");
        System.out.println("8. Get User Details");
        System.out.println("9. Checkout Book");
        System.out.println("10. Return Book");
        System.out.println("11. Renew Book");
        System.out.println("12. Exit");
        System.out.print("Choose an option: ");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBookToShelfCLI(scanner);
                    break;
                case 2:
                    updateBookDetailsCLI(scanner);
                    break;
                case 3:
                    removeBookFromShelfCLI(scanner);
                    break;
                case 4:
                    getBookDetailsCLI(scanner);
                    break;
                case 5:
                    registerUserCLI(scanner);
                    break;
                case 6:
                    updateUserDetailsCLI(scanner);
                    break;
                case 7:
                    deleteUserCLI(scanner);
                    break;
                case 8:
                    getUserDetailsCLI(scanner);
                    break;
                case 9:
                    checkoutBookCLI(scanner);
                    break;
                case 10:
                    returnBookCLI(scanner);
                    break;
                case 11:
                    renewBookCLI(scanner);
                    break;
                case 12:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void addBookToShelfCLI(Scanner scanner){
        System.out.print("Enter Book Title: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter Author Name: ");
        String authorName = scanner.nextLine();
        System.out.print("Enter Author Email: ");
        String authorEmail = scanner.nextLine();
        System.out.print("Enter Publisher Name: ");
        String publisherName = scanner.nextLine();
        System.out.print("Enter Published Year: ");
        int publishedYear = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Publisher Email: ");
        String publishedEmail = scanner.nextLine();
        System.out.print("Enter Publisher Address: ");
        String publishedAddress = scanner.nextLine();

        Book book = new Book(bookTitle, authorName, publishedYear, publisherName, authorEmail, publishedEmail, publishedAddress);
        if (addBookToShelf(book)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add book.");
        }
    }


    private void updateBookDetailsCLI(Scanner scanner) {
        System.out.print("Enter Book Title to update: ");
        String booktitle = scanner.nextLine();
        System.out.print("Enter new Author Name: ");
        String authorName = scanner.nextLine();
        System.out.print("Enter new Author Email: ");
        String authorEmail = scanner.nextLine();
        System.out.print("Enter new Publisher Name: ");
        String publisherName = scanner.nextLine();
        System.out.print("Enter new Published Year: ");
        int publishedYear = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Publisher Email: ");
        String publisherEmail = scanner.nextLine();
        System.out.print("Enter new Publisher Address: ");
        String publisherAddress = scanner.nextLine();

        Book book = new Book(booktitle, authorName, publishedYear, publisherName, authorEmail, publisherEmail, publisherAddress);
        if (updateBookDetails(book)) {
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Failed to update book.");
        }
    }

    private void removeBookFromShelfCLI(Scanner scanner) {
        System.out.print("Enter Book Title to remove: ");
        String bookTitle = scanner.nextLine();
        if (removeBookFromShelf(bookTitle)) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Failed to remove book.");
        }
    }

    private void getBookDetailsCLI(Scanner scanner) {
        System.out.print("Enter Book Title: ");
        String bookTitle = scanner.nextLine();
        Book book = getBookDetails(bookTitle);
        if (book != null) {
            System.out.println(book.toString());
        } else {
            System.out.println("Book not found.");
        }
    }

    private void registerUserCLI(Scanner scanner) {
        System.out.print("Enter User Name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter Mobile Number: ");
        long mobileNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        User user = new User(userName, String.valueOf((int)(Math.random() * 90) + 10),mobileNumber, email, address, LocalDate.now(), null);

        if (registerUser(user)) {
            System.out.println("User registered successfully.");
            System.out.println("User ID : " + user.getUserID());
        } else {
            System.out.println("Failed to register user.");
        }
    }

    private void updateUserDetailsCLI(Scanner scanner) {
        System.out.print("Enter User Email to update: ");
        String userEmail = scanner.nextLine();
        System.out.print("Enter new User Name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter new Mobile Number: ");
        long mobileNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter new Address: ");
        String address = scanner.nextLine();

        User user = new User(userName, "-1", mobileNumber,userEmail, address, LocalDate.now(), null);
        if (updateUserDetails(user)) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    private void deleteUserCLI(Scanner scanner) {
        System.out.print("Enter User Email to delete: ");
        String userId = scanner.nextLine();
        if (deleteUser(userId)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    private void getUserDetailsCLI(Scanner scanner) {
        System.out.print("Enter User Email: ");
        String userMail = scanner.nextLine();
        User user = getUser(userMail);
        if (user != null) {
            System.out.println(user.toString());
        } else {
            System.out.println("User not found.");
        }
    }

    private void checkoutBookCLI(Scanner scanner) {
        System.out.print("Enter User Email: ");
        String userEmail = scanner.nextLine();
        System.out.print("Enter Book Title: ");
        String bookTitle = scanner.nextLine();
        if (checkoutBook(userEmail, bookTitle)) {
            System.out.println("Book checked out successfully.");
        } else {
            System.out.println("Failed to check out book.");
        }
    }

    private void returnBookCLI(Scanner scanner) {
        System.out.print("Enter User Email: ");
        String userEmail = scanner.nextLine();
        System.out.print("Enter Book Title: ");
        String bookTitle = scanner.nextLine();
        if (returnBook(userEmail, bookTitle)) {
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Failed to return book.");
        }
    }

    private void renewBookCLI(Scanner scanner) {
        System.out.print("Enter User Email: ");
        String userEmail = scanner.nextLine();
        System.out.print("Enter Book Title: ");
        String bookTitle = scanner.nextLine();
        if (renewBook(userEmail, bookTitle)) {
            System.out.println("Book renewed successfully.");
        } else {
            System.out.println("Failed to renew book.");
        }
    }




    @Override
    public boolean addBookToShelf(Book book) {
        return bookManagement.addBookToShelf(book);
    }

    @Override
    public boolean updateBookDetails(Book book) {
        return bookManagement.updateBookDetails(book);
    }

    @Override
    public boolean removeBookFromShelf(String bookId) {
        return bookManagement.removeBookFromShelf(bookId);
    }

    @Override
    public Book getBookDetails(String bookID) {
        return bookManagement.getBookDetails(bookID);
    }

    @Override
    public boolean registerUser(User user) {
        return userManagement.registerUser(user);
    }

    @Override
    public boolean updateUserDetails(User user) {
        return userManagement.updateUserDetails(user);
    }

    @Override
    public boolean deleteUser(String userMail) {
        return userManagement.deleteUser(userMail);
    }

    @Override
    public User getUser(String userMail) {
        return userManagement.getUser(userMail);
    }

    @Override
    public boolean checkoutBook(String userEmail, String bookTitle) {
        return transactionManagement.checkoutBook(userEmail, bookTitle);
    }

    @Override
    public boolean returnBook(String userEmail, String bookTitle) {
        return transactionManagement.returnBook(userEmail, bookTitle);
    }

    @Override
    public boolean renewBook(String userEmail, String bookTitle) {
        return transactionManagement.renewBook(userEmail, bookTitle);
    }
}