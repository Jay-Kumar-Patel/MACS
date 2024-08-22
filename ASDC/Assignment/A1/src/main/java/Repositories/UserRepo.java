package Repositories;

import Dao.Book;
import Dao.User;
import Database.MysqlConnectionImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepo {

    private MysqlConnectionImpl mysqlConnection;

    //UserEMail,User
    Map<String, User> users = new HashMap<>();


    public UserRepo(){
        mysqlConnection = MysqlConnectionImpl.getInstance();
    }

    public boolean createUser(User user){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            users.put(user.getEmail(), user);

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

    public boolean updateUser(User user){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part

            User updateUser = users.get(user.getEmail());

            updateUser.setName(user.getName());
            updateUser.setMobileNumber(user.getMobileNumber());
            updateUser.setAddress(user.getAddress());

            users.remove(user.getEmail());
            users.put(updateUser.getEmail(), updateUser);

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

    public boolean deleteUser(String userMail){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            users.remove(userMail);

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

    public User getUser(String userMail){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            User user = users.get(userMail);

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return user;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return null;
        }
    }

    public boolean isUserExist(String userMail){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            if (!users.containsKey(userMail)){
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

    //BookName and Return Date
    public List<Map<String, LocalDate>> checkUserPendingBooks(String userMail){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            /*
            It is not handleable to implement this functionality for this dummy data which we created using CLI.
             */

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return new ArrayList<>();
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return null;
        }
    }

    public boolean isEmailAlreadyExist(String email){
        if (mysqlConnection.CheckConnection()){
            //Start MySql Connection
            mysqlConnection.StartConnection();

            //Logic Part
            for(Map.Entry<String, User> entry : users.entrySet()){
                if (entry.getValue().getEmail().equals(email)){
                    return true;
                }
            }

            //Close Mysql Connection
            mysqlConnection.CloseConnection();

            //It will depend on Logic
            return false;
        }
        else {
            System.out.println("Mysql Connection is not established!");
            return false;
        }
    }

}
