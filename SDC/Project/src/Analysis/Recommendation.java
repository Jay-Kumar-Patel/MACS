package Analysis;

import Dao.PopularityDao;
import Dao.RecommendationDao;
import Database.MysqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Recommendation {


    /**
     * Method to prepare data for recommendation stocks.
     * @param stokesPrice : Contain all the unique stocks and its current price
     * @return : Return map in which key is the account ID and value is the details of that account regarding stock holdings.
     */
    public Map<Integer, RecommendationDao> generateDataForRecommendation(Map<Integer, Double> stokesPrice)
    {
        try {

            Map<Integer, RecommendationDao> data = new HashMap<>();

            //Create Query to fetch data.
            String readQuery = String.format("SELECT a.ID, a.Cash, i.Stock_ID, i.Stokes, s.Price " +
                    "FROM account a LEFT JOIN account_stock_info i ON a.ID = i.Account_ID " +
                    "LEFT JOIN stock s ON i.Stock_ID = s.ID GROUP BY a.ID, i.Stock_ID");

            //Execute Query
            ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQuery, "Read Tables for Recommendation");

            //Work on ResultSet.
            while (resultSet.next())
            {
                int accountID = resultSet.getInt("ID");
                double cash = resultSet.getDouble("Cash");
                int stockID = resultSet.getInt("Stock_ID");
                double stokes = resultSet.getDouble("Stokes");
                double price = resultSet.getDouble("Price");

                //Insert stock into collection of unique stocks.
                if (stockID != 0 && !stokesPrice.containsKey(stockID)){
                    stokesPrice.put(stockID, price);
                }

                //If account is already exist into our data than just append new entries into that account.
                if (data.containsKey(accountID))
                {
                    RecommendationDao recommendationDao = data.get(accountID);
                    Map<Integer, Double> map = recommendationDao.getStokes();
                    map.put(stockID, stokes);
                    recommendationDao.setStokes(map);
                    data.put(accountID, recommendationDao);
                }
                //Else create new entry for that account and add data to map.
                else{
                    RecommendationDao recommendationDao = new RecommendationDao();
                    if (stockID == 0){
                        data.put(accountID, recommendationDao);
                    }
                    else{
                        Map<Integer, Double> map = new HashMap<>();
                        map.put(stockID, stokes);
                        recommendationDao.setStokes(map);
                        recommendationDao.setCash(cash);
                        data.put(accountID, recommendationDao);
                    }
                }
            }

            return data;
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return null;
        }
    }


    /**
     * Method to make all account stock holding vector of same dimensions for cosine similarity.
     * @param accountInfo : Contains all accounts info.
     * @param stokesPrice : Contains all the unique stocks and its current price.
     */
    public void refactorData(Map<Integer, RecommendationDao> accountInfo, Map<Integer, Double> stokesPrice)
    {
        //Iterate through each account.
        for (Map.Entry<Integer, RecommendationDao> entry : accountInfo.entrySet())
        {
            RecommendationDao recommendationDao = entry.getValue();

            //Get Current account stock holding info.
            Map<Integer, Double> accountStokes = null;
            if(recommendationDao.getStokes() != null){
                accountStokes = recommendationDao.getStokes();
            }

            //If account does not hold any stock than put all the stock from stokesPrice into its holding and set as 0.
            if (accountStokes == null){
                accountStokes = new HashMap<>();
                for (Map.Entry<Integer, Double> stokes : stokesPrice.entrySet()){
                    accountStokes.put(stokes.getKey(), 0.0);
                }
                recommendationDao.setStokes(accountStokes);
            }
            //Else put only those stocks that they did not have and set them as 0.
            else{
                for (Map.Entry<Integer, Double> stokes : stokesPrice.entrySet()){
                    if (!accountStokes.containsKey(stokes.getKey())){
                        accountStokes.put(stokes.getKey(), 0.0);
                    }
                }
            }
        }
    }


    /**
     * Method to create a map of stock popularity.
     * @param stokesPopularity : Map in which we have to store data.
     * @param accountIDs : List of account IDs which we have to consider for popularity.
     * @param accountInfo : All accounts Info
     */
    public void makeStokesPopularity(List<PopularityDao> stokesPopularity, List<Integer> accountIDs, Map<Integer, RecommendationDao> accountInfo, int numComparators)
    {
        //Iterate through each account
        for (Map.Entry<Integer, RecommendationDao> entry : accountInfo.entrySet())
        {
            //Choose only those account which ID is in our list (Which we created after cosine similarity)
            if (accountIDs.contains(entry.getKey()))
            {
                RecommendationDao recommendationDao = entry.getValue();

                for (Map.Entry<Integer, Double> i : recommendationDao.getStokes().entrySet()){

                    if (i.getValue() > 0){

                        boolean isPresent = false;

                        //If Stock Already Present than just increase popularity by 1.
                        for (PopularityDao popularityDao : stokesPopularity){
                            if (popularityDao.getStockID() == i.getKey()){
                                isPresent = true;
                                popularityDao.setPopularity(popularityDao.getPopularity()+1);
                            }
                        }

                        //Else add the stock with popularity=1.
                        if (!isPresent){
                            PopularityDao popularityDao = new PopularityDao();
                            popularityDao.setStockID(i.getKey());
                            popularityDao.setPopularity(1);
                            popularityDao.setBuyOrSell(true);

                            stokesPopularity.add(popularityDao);
                        }
                    }
                }
            }
        }

        //Now Add reverse popularity (If out of 10 people, 3 people had that Stock_A it also means that 7 people doesn't have)
        List<PopularityDao> reversePopularity = new ArrayList<>();
        for (PopularityDao popularity : stokesPopularity){
            PopularityDao popularityDao = new PopularityDao();
            popularityDao.setStockID(popularity.getStockID());
            popularityDao.setPopularity(numComparators-popularity.getPopularity());

            if (popularity.isBuyOrSell()){
                popularityDao.setBuyOrSell(false);
            }
            else{
                popularityDao.setBuyOrSell(true);
            }
        }

        //Add reverse popularity to our main list
        stokesPopularity.addAll(reversePopularity);

        // Sorting the list in descending order based on popularity
        Collections.sort(stokesPopularity, new Comparator<PopularityDao>() {
            @Override
            public int compare(PopularityDao o1, PopularityDao o2) {
                // Sort in descending order based on popularity
                return Integer.compare(o2.getPopularity(), o1.getPopularity());
            }
        });
    }


    /**
     * Method to recommend stocks to given account ID.
     * @param stokesPopularity : Stokes Popularity.
     * @param maxRecommendations : Number of recommendations allowed.
     * @param numComparators : Max num of comparisons we have to consider for recommendation.
     * @param accountID : Account ID for which we have to recommend stocks.
     * @return : Return map in which key is recommended stock ID and value is boolean (true - buy and false - sell).
     */
    public Map<Integer, Boolean> recommendStocks(Map<Integer, Double> currentStocksHolding, List<PopularityDao> stokesPopularity, int maxRecommendations, int numComparators, int accountID)
    {
        Map<Integer, Boolean> map = new HashMap<>();

        //Create Arrays from stokes popularity.
        int[] stockID = new int[stokesPopularity.size()];
        int[] popularity = new int[stokesPopularity.size()];
        boolean[] buyOrSell = new boolean[stokesPopularity.size()];

        int halfComparators = numComparators/2;

        //Fill those arrays
        int i = 0;
        for (PopularityDao popularityDao : stokesPopularity){
            stockID[i] = popularityDao.getStockID();
            popularity[i] = popularityDao.getPopularity();
            buyOrSell[i] = popularityDao.isBuyOrSell();
            i++;
        }

        //Iterate through each stock present in stock popularity
        for(int index=0;index<popularity.length;index++)
        {
            if (maxRecommendations == 0)
                break;

            //Go to next popularity until we get the different popularity.
            int j = index;
            while (j < popularity.length - 1 && popularity[index] == popularity[index + 1]) {
                j++;
            }

            //If no popularity is common
            if(j == index)
            {
                if (buyOrSell[index] && popularity[index] > halfComparators){
                    if (currentStocksHolding.containsKey(stockID[index]) && currentStocksHolding.get(stockID[index]) == 0){
                        map.put(stockID[index], true);
                    }
                }
                else if (!buyOrSell[index] && popularity[index] > halfComparators){
                    if (currentStocksHolding.containsKey(stockID[index]) && currentStocksHolding.get(stockID[index]) > 0){
                        map.put(stockID[index], false);
                    }
                }
                maxRecommendations-=1;
            }
            //If popularity is same
            else{

                if ( (j-index)+1 > maxRecommendations )
                {

                }
                //If number of popularity is same but it under our recommendations than we consider all of them.
                else
                {
                    //Go through every popularity
                    for (int z=index;z<=j;z++){

                        if (buyOrSell[z] && popularity[z] > halfComparators){
                            if (currentStocksHolding.containsKey(stockID[z]) && currentStocksHolding.get(stockID[z]) == 0){
                                map.put(stockID[z], true);
                            }
                        }
                        else if (!buyOrSell[z] && popularity[z] > halfComparators){
                            if (currentStocksHolding.containsKey(stockID[z]) && currentStocksHolding.get(stockID[z]) > 0){
                                map.put(stockID[z], false);
                            }
                        }

                        maxRecommendations-=1;
                    }
                    index = j;
                }
            }
        }

        return map;
    }


    /**
     * Method to convert stock ID to stock Symbol.
     * @param recommendations : Map of stock ID and recommendation to buy or sell.
     * @return : Return map in which key is recommended stock Symbol and value is boolean (true - buy and false - sell).
     */
    public Map<String, Boolean> getStockNameFromID(Map<Integer, Boolean> recommendations)
    {
        try {
            Map<String, Boolean> map = new HashMap<>();

            //Iterate through each recommendation and replace stock ID with stock Symbol.
            for (Map.Entry<Integer, Boolean> entry : recommendations.entrySet())
            {

                //Create Query to get stock symbol
                String readQuery = String.format("SELECT Symbol from stock WHERE ID='%d'", entry.getKey());

                //Execute Query.
                ResultSet resultSet = MysqlConnection.getInstance().FetchData(readQuery, "Read Stock Table to Convert Stock ID to Stock Symbol for Recommendation");

                //Result
                while (resultSet.next())
                {
                    map.put(resultSet.getString("Symbol"), entry.getValue());
                }
            }

            return map;
        }
        catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
            return null;
        }
    }
}
