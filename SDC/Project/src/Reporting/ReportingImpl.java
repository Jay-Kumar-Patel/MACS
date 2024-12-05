package Reporting;

import Dao.DivergentAccountDao;
import Dao.UserDao;
import Database.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import Exception.*;

public class ReportingImpl implements Reporting{


    /**
     * Method to Get Account Value which consists of cash balance and stokes value hold by this account.
     * @param accountID : Account ID for which we want to calculate account value.
     * @return : Return account value of the given account ID. If account does not exist return -1.
     */
    @Override
    public double accountValue(int accountID) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check User is existed or not.
            if (!checkUserValidation(accountID, UserDao.Role.INVESTOR.toString())){
                throw new CustomException("Account ID is not Valid!");
            }

            double accountValue = 0;

            //Create Query to get the value of this account.
            String readyQuery = String.format("SELECT a.Cash, SUM(i.Stokes * s.Price) AS stokesValue " +
                    "FROM account a LEFT JOIN account_stock_info i ON a.ID = i.Account_ID " +
                    "LEFT JOIN stock s ON i.Stock_ID = s.ID " +
                    "WHERE a.ID = '%d'", accountID);

            //Execute Query
            ResultSet resultSetAccount = MysqlConnection.getInstance().FetchData(readyQuery, "Read Tables for accountValue");

            //ResultSet
            while (resultSetAccount.next())
            {
                accountValue += resultSetAccount.getDouble("Cash") + resultSetAccount.getDouble("stokesValue");
            }

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            return accountValue;
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


    /**
     * Method to Get Advisor Portfolio Value which consist of all the accounts values whose advisor is the given advisor.
     * @param advisorId : Advisor ID for which we have to find portfolio value.
     * @return : Return portfolio value if advisor exists, else return -1.
     */
    @Override
    public double advisorPortfolioValue(int advisorId) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check User is existed or not.
            if (!checkUserValidation(advisorId, UserDao.Role.ADVISOR.toString())){
                throw new CustomException("Advisor Does not Exist!");
            }

            double portfolioValue = 0;

            //Create Query
            String readQuery = String.format("SELECT a.Cash, SUM(i.Stokes * s.Price) AS stokesValue " +
                    "FROM account a LEFT JOIN account_stock_info i ON a.ID = i.Account_ID " +
                    "LEFT JOIN stock s ON i.Stock_ID = s.ID " +
                    "WHERE a.Advisor_ID = '%d'  GROUP BY a.ID", advisorId);

            //Execute Query
            ResultSet resultSetAdvisor = MysqlConnection.getInstance().FetchData(readQuery, "Read Tables for advisorPortfolioValue");

            //ResultSet
            while (resultSetAdvisor.next())
            {
                portfolioValue += resultSetAdvisor.getDouble("Cash") + resultSetAdvisor.getDouble("stokesValue");
            }

            //Close Connection.
            MysqlConnection.getInstance().CloseConnection();

            return portfolioValue;
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


    /**
     * Method to Get Investor's Profit
     * @param clientId : Client ID for which we have to calculate the profit.
     * @return : Return all accounts profit associated with this client ID.
     */
    @Override
    public Map<Integer, Double> investorProfit(int clientId) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check User is existed or not.
            if (!checkUserValidation(clientId, UserDao.Role.INVESTOR.toString())){
                throw new CustomException("Investor Does not Exist!");
            }

            Map<Integer, Double> accountProfit = new HashMap<>();

            //Create Query
            String readQuery = String.format("SELECT a.ID, SUM((i.Stokes * s.Price) - i.ACB) AS stokesValue " +
                    "FROM account a LEFT JOIN account_stock_info i ON a.ID = i.Account_ID " +
                    "LEFT JOIN stock s ON i.Stock_ID = s.ID " +
                    "WHERE a.Investor_ID = '%d' GROUP BY a.ID", clientId);

            //Execute Query
            ResultSet resultSetAdvisor = MysqlConnection.getInstance().FetchData(readQuery, "Read Tables for investorProfit");

            //ResultSet
            while (resultSetAdvisor.next())
            {
                accountProfit.put(resultSetAdvisor.getInt(1), resultSetAdvisor.getDouble(2));
            }

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            return accountProfit;
        }
        catch (SQLException sqlException)
        {
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return null;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return null;
        }
    }


    /**
     * Method to get the sector weights of given account ID.
     * @param accountId : Account ID for which we have to find sector weights.
     * @return : Return sector weights map if user exist, else return null.
     */
    @Override
    public Map<String, Integer> profileSectorWeights(int accountId) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check User is existed or not.
            if (!checkUserValidation(accountId, UserDao.Role.INVESTOR.toString())){
                throw new CustomException("Investor Does not Exist!");
            }

            //Create Query
            String readQuery = String.format("SELECT a.Cash, sect.Name, SUM(i.Stokes * s.Price) AS stokesValue " +
                    "FROM account a LEFT JOIN account_stock_info i ON a.ID = i.Account_ID " +
                    "LEFT JOIN stock s ON i.Stock_ID = s.ID " +
                    "LEFT JOIN sector sect ON s.Sector_ID = sect.ID " +
                    "WHERE a.ID = '%d' " +
                    "GROUP BY sect.Name", accountId);

            //Execute Query.
            ResultSet resultSetSectorWeights = MysqlConnection.getInstance().FetchData(readQuery, "Read Tables for profileSectorWeights");


            Map<String, Double> data = new HashMap<>();
            double total = 0;
            boolean isFirst = false;

            //Work on ResultSet.
            while (resultSetSectorWeights.next())
            {
                //When only first entry comes we have to add Cash.
                if (!isFirst){
                    isFirst = true;
                    data.put("Cash", resultSetSectorWeights.getDouble("Cash"));
                    total += resultSetSectorWeights.getDouble("Cash");
                }
                //Otherwise just calculate stokes value.
                total += resultSetSectorWeights.getDouble("stokesValue");
                data.put(resultSetSectorWeights.getString("Name"), resultSetSectorWeights.getDouble("stokesValue"));
            }

            //Close Connection.
            MysqlConnection.getInstance().CloseConnection();

            //Convert values to percentage.
            Map<String, Integer> weightPercentage = new HashMap<>();
            for (Map.Entry<String, Double> entry : data.entrySet()){
                weightPercentage.put(entry.getKey(), (int) (entry.getValue()*100/total));
            }

            return weightPercentage;
        }
        catch (SQLException sqlException)
        {
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return null;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return null;
        }
    }

    @Override
    public Set<Integer> divergentAccounts(int tolerance) {

        try {

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            DivergentAccounts divergentAccounts = new DivergentAccounts();

            //Fetch Actual Profiles
            Map<Integer, DivergentAccountDao> actualProfile = divergentAccounts.getActualAccountProfile();

            //Fetch Assigned Profiles
            Map<Integer, Map<String, Integer>> assignedProfile = divergentAccounts.getAssignProfiles();

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            if (actualProfile == null || assignedProfile == null){
                throw new CustomException("Failed to Fetch Data for divergentAccounts!");
            }

            Set<Integer> divergentAccountID = new HashSet<>();

            //Iterate through each account actual profiles.
            for(Map.Entry<Integer, DivergentAccountDao> entry : actualProfile.entrySet())
            {
                //Account Data
                DivergentAccountDao actual = entry.getValue();

                //Actual Sector and its values
                Map<String, Double> proAct = actual.getSectors();

                //Assigned Sector and its percentage.
                Map<String, Integer> proAss = assignedProfile.get(entry.getKey());

                //Add Cash Amount to total
                double total = actual.getCash();

                //Iterate through each sector in actual profile and get the total value of all the stokes in that all sectors.
                for (Map.Entry<String, Double> sectorTotal : proAct.entrySet()){
                    total += sectorTotal.getValue();
                }

                boolean isDivergentAccount = false;

                //Now Again Iterate through every sector in actual profile.
                for (Map.Entry<String, Double> acutalSectorWeights : actual.getSectors().entrySet())
                {
                    if (acutalSectorWeights.getKey() != null){

                        int percentage = (int) (acutalSectorWeights.getValue()/total);

                        //If profile is assigned by the advisor than check it not differ more than or less than tolerance
                        if (proAss.get(acutalSectorWeights.getKey()) != null){
                            int originalPer = proAss.get(acutalSectorWeights.getKey());
                            if (percentage > (originalPer+tolerance) || percentage < (originalPer-tolerance)){
                                isDivergentAccount = true;
                            }
                        }
                        //If not sector is not assigned than also check it not differ more than or less than tolerance
                        else{
                            if (percentage > tolerance || percentage < tolerance){
                                isDivergentAccount = true;
                            }
                        }
                    }

                    //If any one sector not follow the assigned profile than consider this account ID in divergentAccounts list.
                    if (isDivergentAccount){
                        divergentAccountID.add(entry.getKey());
                        break;
                    }
                }

            }

            return divergentAccountID;
        }
        catch (SQLException sqlException)
        {
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return null;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return null;
        }
    }


    /**
     * Method to check the given User ID is already exist or not for calculating reporting.
     * @param ID : User ID
     * @param type : Role of the user (In this case, only Investor is valid)
     * @return : Return true is user exist, else return false.
     */
    private boolean checkUserValidation(int ID, String type)
    {
        //Create Query
        String readQueryInvestor = String.format("Select a.ID, u.Role from account a, users u Where a.ID='%d' AND u.Role='%s'", ID, type);

        //Execute Query
        if (MysqlConnection.getInstance().isDataPresent(readQueryInvestor, "Check user is already exist or not for reporting.", false, new int[]{})){
            return true;
        }

        return false;
    }
}
