import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution {

    Utils utils;

    public Solution(){
        utils = new Utils();
    }

    public Map<String, Map<String, String>> getSolution(String rowCategory, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {
        boolean[] isNullSolutionPresent = {false};
        Map<String, Map<String, String>> ans = createSolution(rowCategory, data, dataMapping, isNullSolutionPresent);

        //If any one value of any category is null then entire solution is null.
        if (isNullSolutionPresent[0])
            return null;

        return ans;
    }


    //Check the solution of given rowCategory.
    public boolean CheckSolution(String rowCategory, Map<String, Map<String, String>> solution, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {
        boolean[] isNullSolutionPresent = {false};

        //First create solution for the category.
        Map<String, Map<String, String>> actualAns = createSolution(rowCategory, data, dataMapping, isNullSolutionPresent);

        //Now compare our solution to the given solution.
        for (Map.Entry<String, Map<String, String>> entry : actualAns.entrySet())
        {
            Map<String, String> checkingRowActual = entry.getValue();
            Map<String, String> checkingRowExpected = solution.get(entry.getKey());

            if (checkingRowActual == null || checkingRowExpected == null)
                return false;

            for(Map.Entry<String, String> entryValues : checkingRowActual.entrySet())
            {
                String actualValue = entryValues.getValue();
                String expectedValue = checkingRowExpected.get(entryValues.getKey());

                //If our ans and expected is different then return false.
                //If expected ans is yes or no and our ans is null than it will be okay, just return true.
                if (actualValue != null && expectedValue != null && !actualValue.equals(expectedValue))
                {
                    return false;
                }
            }
        }

        return true;
    }

    //Create solution from our exiting data mapping for given row category.
    public Map<String, Map<String, String>> createSolution(String rowCategory, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping, boolean[] isNullSolutionPresent)
    {
        Map<String, Map<String, String>> ans = new HashMap<>();

        List<String> values = data.get(rowCategory);

        for (Map.Entry<String, Map<String , String>> dataMappingEntry : dataMapping.entrySet()){

            if(values.contains(dataMappingEntry.getKey())){

                Map<String, String> categoryAns = new HashMap<>();

                //Get all the yes present in that mapping
                for(Map.Entry<String, String> entry : dataMappingEntry.getValue().entrySet()){
                    if(entry.getValue() != null && entry.getValue().equals("yes")){
                        categoryAns.put(utils.getCategory(entry.getKey(), data), entry.getKey());
                    }
                }

                //If we don't get yes from all the categories for that mapping we have to add null for that in our solution.
                if(categoryAns.size() != (data.keySet().size() - 1)){
                    isNullSolutionPresent[0] = true;

                    Set<String> categoryNames = categoryAns.keySet();
                    Set<String> totalCategoryNames = data.keySet();

                    for(String str : totalCategoryNames){
                        if(!rowCategory.equals(str) && !categoryNames.contains(str)){
                            categoryAns.put(str, null);
                        }
                    }
                }

                ans.put(dataMappingEntry.getKey(), categoryAns);
            }
        }

        return ans;
    }
}
