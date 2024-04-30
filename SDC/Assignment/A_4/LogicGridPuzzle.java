import java.util.*;

import static java.util.Map.entry;

public class LogicGridPuzzle {

    //Class which implement all the necessary function which need to perform operations related to this puzzle.
    LogicHelper logicHelper;

    //Constructor
    public LogicGridPuzzle(){
        logicHelper = new LogicHelper();
    }

    public static void main(String[] args) {

        LogicGridPuzzle puzzle = new LogicGridPuzzle();
        Map<String, Map<String, String>> expectedSolution = Map.ofEntries(
                entry("Ruby", Map.ofEntries(
                        entry("Male", "Jake"),
                        entry("Activity", "Sleep"),
                        entry("Kittens", "2")
                )),
                entry("Spot", Map.ofEntries(
                        entry("Male", "Batman"),
                        entry("Activity", "Ball"),
                        entry("Kittens", "1")
                )),
                entry("Starbuck", Map.ofEntries(
                        entry("Male", "Dibii"),
                        entry("Activity", "Lazer"),
                        entry("Kittens", "3")

                ))
        );

        // Set category
        puzzle.setCategory("Male", new ArrayList<>(List.of("Batman", "Jake", "Dibii")));
        puzzle.setCategory("Female", new ArrayList<>(List.of("Ruby", "Spot", "Starbuck")));
        puzzle.setCategory("Activity", new ArrayList<>(List.of("Lazer", "Sleep", "Ball")));
        puzzle.setCategory("Kittens", new ArrayList<>(List.of("1", "2", "3")));

        // Set value possibilities
        puzzle.valuePossibilities("Batman", new HashSet<>(List.of("Ball")), new HashSet<>(List.of("Starbuck")));
        puzzle.valuePossibilities("Dibii", new HashSet<>(List.of("Lazer")), null);
        puzzle.valuePossibilities("Ruby", new HashSet<>(List.of("Sleep")), null);

        // Set list order
        puzzle.listOrder("Kittens","Batman","Starbuck",2);

        Map<String, Map<String, String>> solution = puzzle.solve("Female");

        System.out.println(solution);

    }

    /**
     * Method to add new category and its value to the LogicGridPuzzle.
     * @param categoryName - Category Name
     * @param categoryValues - Category Values
     * @return - true if successfully added new category, else return false;
     */
    public boolean setCategory( String categoryName, List<String> categoryValues )  {

        if(categoryName == null || categoryName.isBlank() || categoryValues == null || categoryValues.isEmpty())
            return false;

        //Check all the values present in categoryValue is not null or empty.
        for (String str : categoryValues){
            if (str == null || str.isBlank()){
                return false;
            }
        }

        return logicHelper.setCategory(categoryName, categoryValues);
    }



    /**
     * Method to add clue to the existing puzzle
     * @param baseValue - Value for which we have to perform operations
     * @param options - Possible correct ans of the base value.
     * @param exclusions - Possible Wrong ans of the base value.
     * @return - true if clue is successfully added, else return false.
     */
    public boolean valuePossibilities( String baseValue, Set<String> options, Set<String> exclusions )
    {
        if (baseValue == null || baseValue.isBlank())
            return false;

        //Check any one of options or exclusions must be present
        if ( (options == null || options.isEmpty()) && (exclusions == null || exclusions.isEmpty()) )
            return false;

        //Check all the values present in options is not null or empty.
        if (options!=null && !options.isEmpty()){
            for (String str : options){
                if (str == null || str.isBlank())
                    return false;
            }
        }

        //Check all the values present in exclusions is not null or empty.
        if (exclusions!=null && !exclusions.isEmpty()){
            for (String str : exclusions){
                if (str == null || str.isBlank())
                    return false;
            }
        }

        return logicHelper.AddClue(baseValue, options, exclusions);
    }



    /**
     * This method is used to add clue which have comparison in itself.
     * @param orderCategory - category for which comparison happens.
     * @param firstValue - values which comes before during comparison.
     * @param secondValue - values which comes after during comparison.
     * @param orderGap - Gap between first and second value.
     * @return - true if clue is successfully added, else return false.
     */
    public boolean listOrder( String orderCategory, String firstValue, String secondValue, int orderGap )
    {
        if(orderCategory == null || orderCategory.isBlank() || firstValue == null || firstValue.isBlank() || secondValue == null || secondValue.isBlank() || orderGap < 0)
            return false;

        return logicHelper.AddListOrderClue(orderCategory, firstValue, secondValue, orderGap);
    }



    /**
     * Method to get the solution of this particular row category.
     * @param rowCategory - category for which we have find the solution
     * @return - return the data in specified format. Return null in case of error.
     */
    public Map<String, Map<String, String>> solve( String rowCategory )
    {
        if(rowCategory == null || rowCategory.isBlank())
            return null;

        return logicHelper.SolveLogicGrid(rowCategory);
    }


    /**
     * This method is used to check the solution we made till now, is correct or not.
     * @param rowCategory - For this category we have to compare the solutions.
     * @param solution - Expected Solution
     * @return - true, if actual solution is same expected solution.
     */
    public boolean check( String rowCategory, Map<String, Map<String, String>> solution )
    {
        //Check the rowCategory must not null or empty
        if (rowCategory == null || rowCategory.isBlank() || solution == null || solution.isEmpty())
            return false;

        return logicHelper.CheckLogicGrid(rowCategory, solution);
    }

}
