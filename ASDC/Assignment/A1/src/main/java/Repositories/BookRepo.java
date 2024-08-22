package Repositories;

import Dao.Book;
import Database.MysqlConnectionImpl;

import java.util.HashMap;
import java.util.Map;

public class BookRepo {

    private MysqlConnectionImpl mysqlConnection;

    Map<String, Book> books = new HashMap<>();

    public BookRepo(){
        mysqlConnection = MysqlConnectionImpl.getInstance();
    }

    public boolean addBook(Book book){

        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            books.put(book.getBookTitle(), book);

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return true;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return false;
        }
    }

    public boolean editBook(Book book){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part

            Book updateBook = books.get(book.getBookTitle());

            updateBook.setAuthorName(book.getAuthorName());
            updateBook.setAuthorEmail(book.getAuthorEmail());
            updateBook.setPublisherName(book.getPublisherName());
            updateBook.setPublishedYear(book.getPublishedYear());
            updateBook.setPublisherEmail(book.getPublisherEmail());
            updateBook.setPublisherAddress(book.getPublisherAddress());

            books.remove(book.getBookTitle());
            books.put(updateBook.getBookTitle(), updateBook);

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return true;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return false;
        }
    }

    public boolean deleteBook(String bookTitle){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            books.remove(bookTitle);

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return true;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return false;
        }
    }

    public Book getBook(String bookTitle){

        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            Book book = books.get(bookTitle);

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return book;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return null;
        }
    }

    public boolean isBookAlreadyExist(String bookTitle){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            if (!books.containsKey(bookTitle)){
                return false;
            }

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return true;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return false;
        }
    }
}
