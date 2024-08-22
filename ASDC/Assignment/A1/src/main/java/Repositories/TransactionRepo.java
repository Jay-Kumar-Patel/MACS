package Repositories;

import Dao.Book;
import Dao.Transaction;
import Dao.User;
import Database.MysqlConnectionImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TransactionRepo {

    private MysqlConnectionImpl mysqlConnection;

    Map<String, Transaction> transactions = new HashMap<>();


    public TransactionRepo(){
        mysqlConnection = MysqlConnectionImpl.getInstance();
    }

    public boolean createTransaction(Transaction transaction){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            transactions.put(transaction.getUser().getEmail()+transaction.getBook().getBookTitle(), transaction);

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

    public Transaction getTransaction(String userEmail, String bookTitle){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            if (transactions.containsKey(userEmail+bookTitle)){
                return transactions.get(userEmail+bookTitle);
            }

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return null;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return null;
        }
    }

    public boolean updateTransaction(Transaction transaction){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            transactions.remove(transaction.getUser().getEmail()+transaction.getBook().getBookTitle());
            transactions.put(transaction.getUser().getUserID()+transaction.getBook().getBookTitle(), transaction);

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