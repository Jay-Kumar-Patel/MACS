package User;

import Database.MysqlConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Exception.*;
import Dao.UserDao;

public class UserImpl implements User {

    /**
     * Method used to Add new user to our database.
     * @param user : User Data (Name, Role)
     * @return : If user is successfully added than return ID of that user, else return -1.
     */
    @Override
    public int addUser(UserDao user) {

        try{

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Create Query for Validation
            String readQuery = String.format("Select * from users Where Name='%s'",user.getName());

            //Check User is already exist or not.
            if (MysqlConnection.getInstance().isDataPresent(readQuery, "Check " + user.getName() + "(" + user.getRoleName() + ") is already exist or not...", false, new int[]{})){
                throw new CustomException(user.getName() + " (" + user.getRoleName() + ") is already Exist.");
            }

            //Create Query to add new user.
            String insertQuery = String.format("INSERT INTO users (name, role) VALUES ('%s', '%s')", user.getName(), user.getRoleName());

            //Execute Query
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(true)), "Creating New " + user.getRoleName());

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            //Check result of query execution
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return lastInsertedId.get(0);
            }
            else {
                throw new CustomException("Unable to add new " + user.getRoleName() + "!");
            }
        }
        catch (SQLException sqlException)
        {
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return -1;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return -1;
        }
    }

}
