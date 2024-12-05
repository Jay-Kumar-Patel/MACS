package Database;

import Log.*;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class MysqlConnection implements DBConnection{

    //URL and Credentials to Connect with Mysql Database.
    private String URL = "";
    private String USERNAME = "";
    private String PASSWORD = "";

    //LogWriter to write logs in .txt file present at desktop.
    private LogWriter logWriter = TxtLogWriter.getInstance();

    //Mysql Connection
    private Connection connection;
    private static MysqlConnection mysqlDatabase;


    //Singleton Approach to Create Object of this class
    public static MysqlConnection getInstance()
    {
        if (mysqlDatabase == null) {
            //Use Synchronized, so that in case of multithreading or multiple user create object at the same time
            // only one thread goes inside this block.
            synchronized (MysqlConnection.class) {
                if (mysqlDatabase == null){
                    //Create one time object.
                    mysqlDatabase = new MysqlConnection();
                    //Set Properties.
                    mysqlDatabase.SetDBProperties();
                }
            }
        }

        return mysqlDatabase;
    }


    //Set the url, username and password to establish connection with mysql database.
    private void SetDBProperties() {

        //Read Value from .txt file.
        Properties identity = new Properties();
        String propertyFilename = "properties.txt";

        try {
            InputStream stream = new FileInputStream( propertyFilename );

            identity.load(stream);

            //Set Properties.
            URL = identity.getProperty("url");
            USERNAME = identity.getProperty("username");
            PASSWORD = identity.getProperty("password");
            logWriter.Write("Success", "Read File", "Set Mysql Properties...");
        } catch (Exception e) {
            logWriter.Write("Failure", "Read File", "Error in Set Mysql Properties: " + e.getMessage());
        }
    }

    /**
     * Method to start connection with our database.
     * @return : Return Connection Object (In our case mysql).
     */
    @Override
    public Object StartConnection() {
        try {
            //Start Connection if it is not null and it must not be already open.
            if (connection == null || connection.isClosed()){
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            logWriter.Write("Success", "Mysql Connectivity", "Establish and Open Connection...");
            return connection;
        }
        catch (Exception e){
            logWriter.Write("Failure", "Mysql Connectivity", "Error in Establishing Connection: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to close connection with our database.
     */
    @Override
    public void CloseConnection() {
        try {
            //Close Connection if it is not null and must not already close.
            if (connection != null && !connection.isClosed()){
                connection.close();
                logWriter.Write("Success", "Mysql Connectivity", "Close Connection...");
            }
        }
        catch (Exception e){
            logWriter.Write("Failure", "Mysql Connectivity", "Error in Closing Connection: " + e.getMessage());
        }
    }

    /**
     * Method to check the data is already exist in our database or not.
     * @param query : Query to run and fetch data.
     * @param reason : Reason to call this method.
     * @param wantID : In some case if data exist than we want its ID. (If true than consider to return ID)
     * @param ID : If wantID is true than appand that data ID into this array's zero index.
     * @return : Return true is query is successfully executed, else return false.
     */
    public boolean isDataPresent(String query, String reason, boolean wantID, int[] ID){

        try {

            Statement statement = connection.createStatement();
            statement.execute("use jspatel");

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //Execute Query
            ResultSet resultSet = preparedStatement.executeQuery();

            logWriter.Write("Success", "Data Present", reason);

            if (resultSet != null){
                try {
                    //Work on ResultSet
                    if (resultSet.next()) {
                        //If want the data ID than assign into array 0th index.
                        if (wantID){
                            ID[0] = resultSet.getInt("ID");
                        }
                        return true;
                    }
                } catch (SQLException resultSetException) {
                    logWriter.Write("Failure", "Reading ResultSet",", Error: "+ resultSetException.getMessage());
                } finally {
                    try {
                        resultSet.close();
                    } catch (SQLException resultSetClosingException) {
                        logWriter.Write("Failure", "Closing ResultSet",reason + ", Error: "+ resultSetClosingException.getMessage());
                    }
                }
            }
        }
        catch (SQLException sqlException){
            logWriter.Write("Failure", "Data Present",reason + ", Error: "+ sqlException.getMessage());
        }

        return false;
    }


    /**
     * Method to Execute Query (Insert, Update)
     * @param queries : List of queries which has to execute.
     * @param wantLastInsertedID : If we want that after inserting data we want the ID of that last inserted data than we pass true for that query.
     * If we pass false we return how many rows affected by that particular query.
     * @param reason : Reason to call this method.
     * @return : list which contain last inserted ID or number of rows affected by each query.
     */
    public List<Integer> ExecuteQueries(List<String> queries, List<Boolean> wantLastInsertedID, String reason) {

        List<Integer> lastInsertedID = new ArrayList<>();

        try {

            Statement statement = connection.createStatement();
            statement.execute("use jspatel");

            //Iterate through each query and execute it.
            for (int i=0; i<queries.size(); i++)
            {
                PreparedStatement preparedStatement = connection.prepareStatement(queries.get(i), Statement.RETURN_GENERATED_KEYS);
                int rowsAffected = preparedStatement.executeUpdate();
                //If want ID than add last inserted ID.
                if (wantLastInsertedID.get(i)){
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        lastInsertedID.add(resultSet.getInt(1));
                    }
                }
                //else add rows affected.
                else{
                    lastInsertedID.add(rowsAffected);
                }
            }

            logWriter.Write("Success", "ExecuteQueries", reason);
            return lastInsertedID;
        }
        catch (SQLException tranFailure){
            try {
                logWriter.Write("Failure", "ExecuteQueries", reason + ", Error: " + tranFailure.getMessage());
                connection.rollback();
            }
            catch (SQLException rollBackError){
                logWriter.Write("Failure", "Rollback", reason + ", Error: " + rollBackError.getMessage());
            }
            return null;
        }
    }


    /**
     * Method to Execute Query (Select)
     * @param query : Query which we have to execute.
     * @param reason : Reason to call this method.
     * @return : Return data which we get by executing above query.
     */
    public ResultSet FetchData(String query, String reason)
    {
        try {

            Statement statement = connection.createStatement();
            statement.execute("use jspatel");

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //Execute Query.
            ResultSet resultSet = preparedStatement.executeQuery();

            logWriter.Write("Success", "Fetch Data", reason);

            return resultSet;
        }
        catch (SQLException getDataFailure){
            logWriter.Write("Failure", "Fetch Data", reason + ", Error: " + getDataFailure.getMessage());
            return null;
        }
    }

}
