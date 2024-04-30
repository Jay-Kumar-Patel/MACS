import java.util.*;

public class LogicHelper {

    //Store categories and its values
    private Map<String, List<String>> data;

    //Store One-to-One mapping of existing data.
    private Map<String, Map<String, String>> dataMapping;

    //Store the number of values present in category.
    private int categorySize = -1;

    //Store Inclusions
    Map<String, Set<String>> options;

    //Store Exclusions
    Map<String, Set<String>> exclusions;

    //Necessary classes to perform operations on data and dataMapping.
    Utils utils;
    Mapping mapping;
    Clue clue;
    Solution solution;

    public LogicHelper(){
        data = new HashMap<>();
        dataMapping = new HashMap<>();
        utils = new Utils();
        mapping = new Mapping();
        clue = new Clue();
        solution = new Solution();
        options = new HashMap<>();
        exclusions = new HashMap<>();
    }


    //Add new Category and its value.
    public boolean setCategory(String category, List<String> categoryValues)
    {
        //Check if is the first category to add, than set the size of puzzle
        if(this.categorySize == -1)
        {
            this.categorySize = categoryValues.size();
            this.data.put(category, categoryValues);
            return true;
        }

        //Check the newly added category has the same size as the previous one had.
        //Also Check that the categroy name and also the value present in that category not matched with any existing one.
        if(categoryValues.size() != this.categorySize || !utils.isCategoryValid(category, categoryValues, this.data))
            return false;

        //Add Category.
        this.data.put(category, categoryValues);

        setDataMapping(categoryValues);

        return true;
    }


    //Create Mapping.
    private void setDataMapping(List<String> categoryValues)
    {
        //If two categories present we have to Initilaze mapping.
        if(this.dataMapping.isEmpty())
        {
            this.mapping.InitilazeMapping(this.data, this.dataMapping);
            return;
        }

        //Add new values to exiting mapping.
        this.mapping.CreateMapping(categoryValues, this.dataMapping);

        //Add mapping of new values to exiting category values.
        this.mapping.AddMapping(categoryValues, this.dataMapping);
    }


    //Read the clue
    public boolean AddClue(String baseValue, Set<String> options, Set<String> exclusions){

        //Check data must be present before calling this function
        if(this.data == null || this.data.isEmpty())
            return false;

        //Check the value present in option must not present in exclusions and vice-versa.
        if (options != null && !options.isEmpty() && exclusions != null && !exclusions.isEmpty()){
            for (String str : options){
                if (exclusions.contains(str))
                    return false;
            }

            for (String str : exclusions){
                if (options.contains(str))
                    return false;
            }
        }

        //Create a set and put all the inputs int that.
        Set<String> inputs = new HashSet<>();
        inputs.add(baseValue);

        if (options!=null)
            inputs.addAll(options);

        if (exclusions!=null)
            inputs.addAll(exclusions);

        //Check the basevalue, and all the values present in options and exclusions must present in existing data.
        if (!utils.isClueValid(inputs, this.data))
            return false;

        //Check the values present in option and exclusion has not the same category as the base value had.
        String baseValueCategory = utils.getCategory(baseValue, this.data);

        if(options != null){
            for(String str : options){
                if (utils.getCategory(str, this.data).equals(baseValueCategory))
                    return false;
            }
        }

        if (exclusions != null){
            for(String str : exclusions){
                if (utils.getCategory(str, this.data).equals(baseValueCategory))
                    return false;
            }
        }

        //Update Stored Clues
        this.clue.UpdateClueMap(baseValue, options, exclusions, this.options, this.exclusions, this.data);

        return true;
    }

    //Read the clue of type list order
    public boolean AddListOrderClue(String orderCategory, String firstValue, String secondValue, int orderGap){

        //Check if data is present before calling this function
        if(this.data == null || this.data.isEmpty())
            return false;

        if (firstValue.equals(secondValue))
            return false;

        Set<String> inputs = new HashSet<>();
        inputs.add(orderCategory);
        inputs.add(firstValue);
        inputs.add(secondValue);

        if (!utils.isClueValid(inputs, this.data))
            return false;

        //Check the first and second value has not the same category as ordercategory.
        String firstValueCategory = utils.getCategory(firstValue, this.data);
        String secondValueCategory = utils.getCategory(secondValue, this.data);

        if (firstValueCategory.equals(orderCategory) || secondValueCategory.equals(orderCategory))
            return false;

        if(orderGap > data.get(orderCategory).size()-1)
            return false;

        //Update Stored Clues
        this.clue.UpdateListOrderMap(orderCategory, firstValue, secondValue, orderGap, this.options, this.exclusions, this.data);

        return true;
    }

    public Map<String, Map<String, String>> SolveLogicGrid(String rowCategory)
    {
        //Check if data is present before calling this function
        if(this.data == null || this.data.isEmpty())
            return null;

        Set<String> inputs = new HashSet<>();
        inputs.add(rowCategory);

        if (!utils.isClueValid(inputs, this.data))
            return null;

        //Create temporary mapping and perform operations on that,if clue is successfully add than copy this temporary mapping to our permanent mapping.
        Map<String, Map<String, String>> performOperationMap = new HashMap<>(this.dataMapping);
        applyAllClues(performOperationMap);

        //Check can we make any box tick or mark cross by any situation.
        LoopUtilSolve(5, performOperationMap);

        //Finally get the solution.
        return this.solution.getSolution(rowCategory, data, performOperationMap);
    }


    public boolean CheckLogicGrid(String rowCategory, Map<String, Map<String, String>> solution)
    {
        //Check if data is present before calling this function
        if(this.data == null || this.data.isEmpty())
            return false;

        //Check provided solution is valid or not
        if (!utils.checkSolutionValid(this.data, solution)){
            return false;
        }

        //Create temporary mapping and perform operations on that,if clue is successfully add than copy this temporary mapping to our permanent mapping.
        Map<String, Map<String, String>> performOperationMap = new HashMap<>(this.dataMapping);
        applyAllClues(performOperationMap);

        //Check can we make any box tick or mark cross by any situation.
        LoopUtilSolve(5,performOperationMap);

        //Finally check that our solution matches or not with expected solution.
        return this.solution.CheckSolution(rowCategory, solution, data, performOperationMap);
    }


    //Fill the remaining box with cross and tick.
    public void LoopUtilSolve(int noOfTimes,  Map<String, Map<String, String>> performOperationMap)
    {
        for (int i=0;i<noOfTimes;i++){
            this.mapping.AutoFillRowCol(this.data, performOperationMap);
            this.mapping.IntutionMappingForTick(this.data, performOperationMap);
            this.mapping.IntutionMappingForCross(this.data, performOperationMap);
        }
    }

    //Apply all the clues that are provided till now.
    private void applyAllClues(Map<String, Map<String, String>> applyClueMapping){
        this.clue.ImplementOptions(this.options, this.data, applyClueMapping);
        this.clue.ImplementExclusions(this.exclusions, applyClueMapping);
    }
}
