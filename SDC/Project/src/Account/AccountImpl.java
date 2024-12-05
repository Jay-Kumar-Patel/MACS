package Account;

import Dao.AccountDao;
import Dao.UserDao;
import Database.MysqlConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Exception.*;

public class AccountImpl implements Account{

    /**
     * Method to create new account.
     * @param account : Contain account details like investorID, advisorID, account name, and profile name.
     * @return : Return true if account is successfully created, else return false.
     */
    @Override
    public int createAccount(AccountDao account) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check all provided details are valid or not.
            int validProfileID = newAccountValidation(account.getInvestorID(), account.getAdvisorID(), account.getAccountName(), account.getProfileName());

            if (validProfileID == -1){
                throw new CustomException("Failed to Create Account!");
            }

            //Query to create new account.
            String insertQuery = String.format("INSERT INTO account (Investor_ID, Advisor_ID, Profile_ID, Account_Name, Reinvest) VALUES ('%d', '%d', '%d', '%s', %s)", account.getInvestorID(), account.getAdvisorID(), validProfileID, account.getAccountName(), account.getReinvest());

            //Execute Query
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(true)), "Creating New Account.");

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            //Result
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return lastInsertedId.get(0);
            }
            else {
                throw new CustomException("Unable to create new account!");
            }
        }
        catch (SQLException sqlException){
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

    /**
     * Method to Check that values given to create new account is valid or not.
     * @param investorID : Account Investor ID
     * @param advisorID : Account Advisor ID
     * @param accountName : account name assigned to this new account.
     * @param profileName : Name of the profile which assigned to this new account.
     * @return : Return true if all the provided values are valid, else return false.
     */
    private int newAccountValidation(int investorID, int advisorID, String accountName, String profileName)
    {
        try {

            //Create Query to Check given investor is already exist or not.
            String readQueryInvestor = String.format("Select * from users Where ID='%d' AND Role='%s'", investorID, UserDao.Role.INVESTOR);

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQueryInvestor, "Check investor is already exist or not for creating new account", false, new int[]{})){
                throw new CustomException("Investor Does not Exit!");
            }

            //Create Query to Check given advisor is already exist or not.
            String readQueryAdvisor = String.format("Select * from users Where ID='%d' AND Role='%s'", advisorID, UserDao.Role.ADVISOR);

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQueryAdvisor, "Check advisor is already exist or not for creating new account", false, new int[]{})){
                throw new CustomException("Advisor Does not Exit!");
            }

            //Create Query to Check given account name is already taken by this particular client or not.
            String readQueryAccountName = String.format("Select * from account Where Investor_ID='%d' AND Account_Name='%s'", investorID, accountName);

            //Execute Query
            if (MysqlConnection.getInstance().isDataPresent(readQueryAccountName, "Check account name for the given client is already exist or not for creating new account", false, new int[]{})){
                throw new CustomException("Account Name Already Exist!");
            }

            //Create Query to Check given profile is already exist or not.
            String readQueryProfile = String.format("Select * from profile Where Name='%s'", profileName);
            int[] profileID = new int[1];

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQueryProfile, "Check profile is already exist or not for creating new account", true, profileID)){
                throw new CustomException("Profile Does not Exist!");
            }

            return profileID[0];
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return -1;
        }
    }


    /**
     * Method to Change Advisor.
     * @param account : Contain details like account ID for which we have to change advisor and also new advisor ID.
     * @return : Return true if advisor is successfully changed, else return false.
     */
    @Override
    public boolean changeAdvisor(AccountDao account) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check the provided details are valid or not.
            boolean isValidAccount = changeAdvisorValidation(account.getAccountID(), account.getAdvisorID());

            if (!isValidAccount){
               throw new CustomException("Failed to Change Advisor!");
            }

            //Create Query to change Advisor.
            String updateQuery = String.format("UPDATE account SET Advisor_ID='%d' Where ID='%d'", account.getAdvisorID(), account.getAccountID());

            //Execute Query
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(updateQuery)), new ArrayList<Boolean>(Arrays.asList(false)), "Update Advisor ID");

            //Close Connection.
            MysqlConnection.getInstance().CloseConnection();

            //Result
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return true;
            }
            else {
                throw new CustomException("Unable to change advisor!");
            }
        }
        catch (SQLException sqlException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return false;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return false;
        }
    }


    /**
     * Method to check the input validation for change advisor.
     * @param accountID : Account ID for which we have to change Advisor.
     * @param advisorID : New Investor ID
     * @return : Return true if data is valid, else return false.
     */
    private boolean changeAdvisorValidation(int accountID, int advisorID)
    {
        try {

            //Create Query to check given account is already exist or not.
            String readQueryAccountID = String.format("Select * from account Where ID='%d'", accountID);

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQueryAccountID, "Check account ID is exist or not for changing Advisor", false, new int[]{})){
                throw new CustomException("Account Does not Exist!");
            }

            //Create Query to check the new advisor is already exist or not.
            String readQueryAdvisor = String.format("Select * from users Where ID='%d' AND Role='%s'", advisorID, UserDao.Role.ADVISOR);

            //Execute Query
            if (!MysqlConnection.getInstance().isDataPresent(readQueryAdvisor, "Check advisor is already exist or not for changing Advisor in given account.", false, new int[]{})){
                throw new CustomException("New Advisor Does not Exist!");
            }

            return true;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return false;
        }
    }

}
