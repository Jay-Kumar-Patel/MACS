package Analysis;

import Dao.PopularityDao;
import Dao.RecommendationDao;
import Database.MysqlConnection;

import java.sql.SQLException;
import java.util.*;
import Exception.*;

public class AnalysisImpl implements Analysis{

    /**
     * Method to Recommend Stock
     * @param accountId : Account ID for which I have to recommend stock.
     * @param maxRecommendations : Maximum this much amount of recommendation is allowed.
     * @param numComparators : This account compare with this many accounts to recommend stock.
     * @return : Return map in which stock name and recommendation to buy (true) or sell (false) that particular stock is mentioned.
     */
    @Override
    public Map<String, Boolean> stockRecommendations(int accountId, int maxRecommendations, int numComparators)
    {
        try {

            //Open Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            Recommendation recommendation = new Recommendation();

            //Get Data of all accounts like which account had how many stocks of which stock ID and all.
            Map<Integer, Double> stokesPrice = new HashMap<>();
            Map<Integer, RecommendationDao> accountInfo = recommendation.generateDataForRecommendation(stokesPrice);

            if (accountInfo == null){
                throw new CustomException("Failed to provide Recommendation!");
            }

            //Make all the account stock holding vector of same dimensions for cosine similarity
            recommendation.refactorData(accountInfo, stokesPrice);

            CosineSimilarity cosine = new CosineSimilarity();

            //Now Data is ready, so find cosine similarity between the given account and remaining all other accounts.
            Map<Integer, Double> cosineSimilarity = cosine.findCosineSimilarity(accountInfo, accountId);

            //Now sort the cosine value in descending order to get top accounts equal to numComparators.
            Map<Integer, Double> descendingOrder = sortInDescendingOrder(cosineSimilarity);

            //Now get the account ID of that top accounts
            List<Integer> accountIDs = new ArrayList<>();
            int maxComparisons = numComparators;
            for (Map.Entry<Integer, Double> entry : descendingOrder.entrySet()){
                if (maxComparisons > 0){
                    accountIDs.add(entry.getKey());
                    maxComparisons-=1;
                }
            }

            //Now make a map in which we have the stock ID and its popularity (means how many accounts had this stock)
            List<PopularityDao> stokesPopularity = new ArrayList<>();
            recommendation.makeStokesPopularity(stokesPopularity, accountIDs, accountInfo, numComparators);

            //Now finally we recommend stock to this account.
            Map<Integer, Boolean> recommendations = recommendation.recommendStocks(accountInfo.get(accountId).getStokes(), stokesPopularity, maxRecommendations, numComparators, accountId);

            //Convert Stock ID to Stock Symbol is recommendations.
            Map<String, Boolean> ans = recommendation.getStockNameFromID(recommendations);

            if (ans == null){
                throw new CustomException("Unable to find recommendations!");
            }

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            return ans;
        }
        catch (SQLException sqlException){
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
     * Method to Sort map in descending order on basis of its value.
     * @param map : Map that we have to sort.
     * @return : Return map which contain value in descending order.
     */
    private Map<Integer, Double> sortInDescendingOrder(Map<Integer, Double> map) {

        // Convert the map entries to an array for sorting
        Map.Entry<Integer, Double>[] arr = new Map.Entry[map.size()];
        int i = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            arr[i++] = entry;
        }

        // Sort the array based on the values in descending order
        Arrays.sort(arr, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                // Compare the values of two entries for sorting
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        // Create a new LinkedHashMap to maintain the order of insertion
        Map<Integer, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : arr) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



    @Override
    public Set<Set<Integer>> advisorGroups(double tolerance, int maxGroups) {
        return null;
    }
}
