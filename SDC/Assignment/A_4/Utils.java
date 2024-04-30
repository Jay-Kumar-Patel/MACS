import java.util.*;

public class Utils {

    //Used during adding a new category
    //Verify that new category and its values are not matched with exiting data.
    public boolean isCategoryValid(String categoryName, List<String> categoryValues, Map<String, List<String>> data){

        for (Map.Entry<String, List<String>> entry : data.entrySet()){
            if (entry.getKey().equals(categoryName))
                return false;
            for(String values : entry.getValue()){
                if(categoryValues.contains(values))
                    return false;
            }
        }

        return true;
    }


    //Used during adding of clue and listorder to verify the inputs.
    public boolean isClueValid(Set<String> values, Map<String, List<String>> data)
    {
        Set<String> originalData = new HashSet<>();

        for (Map.Entry<String, List<String>> entry : data.entrySet()){
            originalData.add(entry.getKey());
            originalData.addAll(entry.getValue());
        }

        for (String str : values){
            if (!originalData.contains(str))
                return false;
        }

        return true;
    }


    //Use to add mapping of new values to each mapping into exiting one.
    public boolean SkipEntry(String key, List<String> values){

        for (String str : values){
            if (key.equals(str))
                return true;
        }

        return false;
    }

    //Method to find the category of particular value.
    public String getCategory(String value, Map<String, List<String>> data){
        for(Map.Entry<String, List<String>> entry : data.entrySet()){
            if (entry.getValue().contains(value))
                return entry.getKey();
        }

        return null;
    }


    //Add option to option map
    public void UpdateOptionMap(String from, String to, Map<String, Set<String>> options, Map<String, Set<String>> exclusions)
    {
        //Update Exclusions
        if (exclusions.keySet().contains(from)){
            Set<String> values = exclusions.get(from);
            if (values.contains(to)){
                values.remove(to);
                exclusions.put(from, values);
            }
        }

        //Add into Options
        if (options.keySet().contains(from)){
            Set<String> values = options.get(from);
            values.add(to);
            options.put(from, values);
        }
        else{
            Set<String> values = new HashSet<>();
            values.add(to);
            options.put(from, values);
        }
    }

    //Add exclusion to exclusion map
    public void UpdateExclusionMap(String from, String to, Map<String, Set<String>> options, Map<String, Set<String>> exclusions)
    {
        //Update Options
        if (options.keySet().contains(from)){
            Set<String> values = options.get(from);
            if (values.contains(to)){
                values.remove(to);
                options.put(from, values);
            }
        }

        //Add into Exclusions
        if (exclusions.keySet().contains(from)){
            Set<String> values = exclusions.get(from);
            values.add(to);
            exclusions.put(from, values);
        }
        else{
            Set<String> values = new HashSet<>();
            values.add(to);
            exclusions.put(from, values);
        }
    }


    //Check the given solution is valid in terms of size and values present in that.
    public boolean checkSolutionValid(Map<String, List<String>> data, Map<String, Map<String, String>> solution){

        Set<String> categories = data.keySet();
        List<String> values = new ArrayList<>();

        int valueSize = 0;

        //Get all the values present in our existing data.
        for(Map.Entry<String, List<String>> entry : data.entrySet()){
            valueSize = entry.getValue().size();
            values.addAll(entry.getValue());
        }

        if (categories.size() != solution.size()+1)
            return false;

        //Compare given solution with the values present in our existing data.
        for (Map.Entry<String, Map<String, String>> entry : solution.entrySet()){
            if (values.contains(entry.getKey())){

                if (valueSize != entry.getValue().size())
                    return false;

                for (Map.Entry<String, String> innerEntry : entry.getValue().entrySet()){
                    if (!categories.contains(innerEntry.getKey()) || !values.contains(innerEntry.getValue())){
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }

        return true;
    }
}
