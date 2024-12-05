package Reporting;

import Dao.DivergentAccountDao;
import Database.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DivergentAccounts {

    /**
     * Method to Fetch Data for DivergentAccounts.
     * @return : Return map in which key is accountID and inner map had sector name and its percentage.
     */
    public Map<Integer, Map<String, Integer>> getAssignProfiles() {

        try{

            //Create Query to fetch data.
            String readQuery = String.format("SELECT a.ID, p.Cash, sec.Name, ps.Percentage FROM account a, " +
                    "profile p, profile_sector_info ps, sector sec WHERE a.Profile_ID=p.ID AND p.ID=ps.Profile_ID AND ps.Sector_ID=sec.ID;");

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQuery, "Fetch Data to get assigned profile for divergentAccounts");

            Map<Integer, Map<String, Integer>> map = new HashMap<>();

            //Work on ResultSet
            while (resultSet.next())
            {
                int accountID = resultSet.getInt("ID");
                int cash = resultSet.getInt("Cash");
                String sectorName = resultSet.getString("Name");
                int percentage = resultSet.getInt("Percentage");

                //If data doesn't contain account ID than create new entry and insert into map
                if (!map.containsKey(accountID))
                {
                    Map<String, Integer> sectorWeights = new HashMap<>();
                    sectorWeights.put(sectorName, percentage);
                    sectorWeights.put("Cash", cash);
                    map.put(accountID, sectorWeights);
                }
                //Else just append into data.
                else
                {
                    Map<String, Integer> sectorWeights = map.get(accountID);
                    sectorWeights.put(sectorName, percentage);
                    map.put(accountID, sectorWeights);
                }
            }

            return map;
        }
        catch (SQLException sqlException)
        {
            System.out.println(sqlException.getMessage());
            return null;
        }
    }


    public Map<Integer, DivergentAccountDao> getActualAccountProfile()
    {
        try {

            //Create Query
            String readQuery = String.format("SELECT a.ID, a.Profile_ID, a.Cash, asi.Stokes, s.Price, ss.Name " +
                    "FROM account a LEFT JOIN account_stock_info asi ON a.ID = asi.Account_ID " +
                    "LEFT JOIN stock s ON asi.Stock_ID = s.ID LEFT JOIN sector ss ON s.Sector_ID = ss.ID");

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQuery, "Fetch Data to get actual profile for divergentAccounts");

            Map<Integer, DivergentAccountDao> map = new HashMap<>();

            //Work on ResultSet.
            while (resultSet.next())
            {
                int accountID = resultSet.getInt("ID");
                int profileID = resultSet.getInt("Profile_ID");
                double cash = resultSet.getDouble("Cash");
                double stokes = resultSet.getDouble("Stokes");
                double stockPrice = resultSet.getDouble("Price");
                String sectorName = resultSet.getString("Name");

                //If data doesn't contain account ID than create new entry and insert into map
                if (!map.containsKey(accountID))
                {
                    DivergentAccountDao divergentAccountDao = new DivergentAccountDao();
                    divergentAccountDao.setProfileID(profileID);
                    divergentAccountDao.setCash(cash);

                    Map<String, Double> currentAccountSecInfo = new HashMap<>();
                    currentAccountSecInfo.put(sectorName, (stokes * stockPrice));
                    divergentAccountDao.setSectors(currentAccountSecInfo);

                    map.put(accountID, divergentAccountDao);
                }
                //Else just append into data.
                else
                {
                    Map<String,Double> currentAccountSecInfo = new HashMap<>();

                    //Check that sector is already exist or not. If not present than just directly add entry.
                    if (map.get(accountID).getSectors() != null){
                        currentAccountSecInfo = map.get(accountID).getSectors();
                    }

                    //If sector already exist than sum stokes values till now with the new entry and then append into map.
                    if (!currentAccountSecInfo.isEmpty()){
                        if (currentAccountSecInfo.containsKey(sectorName))
                        {
                            double currentAmount = currentAccountSecInfo.get(sectorName);
                            currentAmount += (stokes * stockPrice);
                            currentAccountSecInfo.put(sectorName, currentAmount);
                        }
                        else{
                            currentAccountSecInfo.put(sectorName, (stokes * stockPrice));
                        }
                    }
                    else{
                        currentAccountSecInfo.put(sectorName, (stokes * stockPrice));
                    }
                }
            }

            return map;
        }
        catch (SQLException sqlException)
        {
            System.out.println(sqlException.getMessage());
            return null;
        }
    }

}
