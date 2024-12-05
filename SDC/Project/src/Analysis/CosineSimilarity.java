package Analysis;

import Dao.RecommendationDao;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {

    /**
     * Method to find cosine similarity.
     * @param data : All accounts Info.
     * @param accountID : Account ID for which we have to give recommendations.
     * @return : Return map in which key is the account ID and value is the cosine similarity of that account for which we have to give recommendations.
     */
    public Map<Integer, Double> findCosineSimilarity(Map<Integer, RecommendationDao> data, int accountID)
    {
        RecommendationDao accountIDforRec = data.get(accountID);

        Map<Integer, Double> cosineSimilarity = new HashMap<>();

        //Iterate through every account
        for(Map.Entry<Integer, RecommendationDao> entry : data.entrySet())
        {
            if (entry.getKey() != accountID){
                cosineSimilarity.put(entry.getKey(), calculateCosine(accountIDforRec, entry.getValue()));
            }
        }

        return cosineSimilarity;
    }


    /**
     * Method to find cosine similarity between given two accounts.
     * @param firstAccount : It is the fix parameter for account for which we have to give recommendations.
     * @param secondAccount : It is the second account Info with which we have to compare first account.
     * @return : Return cosine similarity value between first and second account.
     */
    private double calculateCosine(RecommendationDao firstAccount, RecommendationDao secondAccount)
    {
        int dotProduct = 0;
        double sqrtFirstAccount = 0.0;
        double sqrtSecondAccount = 0.0;

        Map<Integer, Double> firstAccountStokes = firstAccount.getStokes();
        Map<Integer, Double> secondAccountStokes = secondAccount.getStokes();

        //Iterate through every stock.
        for(Map.Entry<Integer, Double> entry : firstAccountStokes.entrySet())
        {
            double stokesFirstAccount = entry.getValue();
            double stokesSecondAccount = secondAccountStokes.get(entry.getKey());

            //Add multiplication of amount of stokes both had.
            dotProduct += stokesFirstAccount * stokesSecondAccount;

            //Now perform square for the denominator.
            sqrtFirstAccount += stokesFirstAccount * stokesFirstAccount;
            sqrtSecondAccount += stokesSecondAccount * stokesSecondAccount;
        }

        //Now find square root for both of them.
        sqrtFirstAccount = Math.sqrt(sqrtFirstAccount);
        sqrtSecondAccount = Math.sqrt(sqrtSecondAccount);

        //If any one of the denominator is zero than we have to return 0, otherwise it will be undefined.
        if (sqrtFirstAccount == 0 || sqrtSecondAccount == 0)
            return 0.0;

        //Formula to get cosine value.
        double cosineValue = dotProduct / (sqrtFirstAccount * sqrtSecondAccount);

        return cosineValue;
    }

}
