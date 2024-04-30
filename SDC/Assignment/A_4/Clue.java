import java.util.*;

public class Clue {

    Mapping mapping;
    Utils utils;

    public Clue(){
        mapping = new Mapping();
        utils = new Utils();
    }

    //Add the clue to the option and exclusion map
    public void UpdateClueMap(String baseValue, Set<String> options, Set<String> exclusions, Map<String, Set<String>> optionsMap, Map<String, Set<String>> exclusionsMap, Map<String, List<String>> data){

        if (options != null && !options.isEmpty()){
            //Maintain value from each options belong to which group.
            Map<String, List<String>> mapOptionsToCategories = getInputCategories(options, data);

            //Iterate through each category into mapOptionsToCategories
            for(Map.Entry<String, List<String>> entry : mapOptionsToCategories.entrySet()){

                String key = entry.getKey();
                List<String> values = entry.getValue();

                //If Only one value present from that category into options than we have to directly tick that position.
                if(values.size() == 1){
                    this.utils.UpdateOptionMap(baseValue, values.get(0), optionsMap, exclusionsMap);
                }
                //So now there are multiple possibilities present, as more than one value for particular category present into options.
                //So here we put cross on those values which are not given in options from that particular category.
                else{

                    //Get original values of that particular category.
                    List<String> categoryValues = new ArrayList<>(data.get(key));

                    //Remove values present in options.
                    for(String value : values){
                        categoryValues.remove(value);
                    }

                    //Mark Cross on remaining values of that category.
                    for(String value : categoryValues){
                        this.utils.UpdateExclusionMap(baseValue, value, optionsMap, exclusionsMap);
                    }
                }
            }
        }

        if (exclusions != null && !exclusions.isEmpty()){
            for (String str : exclusions){
                this.utils.UpdateExclusionMap(baseValue, str, optionsMap, exclusionsMap);
            }
        }
    }

    //Method which connect each value present in option with its categories.
    private Map<String, List<String>> getInputCategories(Set<String> optionOrExclusion, Map<String, List<String>> data) {

        Map<String, List<String>> mapToCategories = new HashMap<>();

        //Iterate through every value present in exclusions and get a category for that value.
        for (String str : optionOrExclusion) {

            String category = utils.getCategory(str, data);

            if(category == null){
                return null;
            }

            //Already Contains Category, So just append it.
            if (mapToCategories.containsKey(category)) {
                List<String> values = mapToCategories.get(category);
                values.add(str);
                mapToCategories.put(category, values);
            }
            //Create Category
            else {
                List<String> values = new ArrayList<>();
                values.add(str);
                mapToCategories.put(category, values);
            }
        }

        return mapToCategories;
    }

    //Add the clue of type list order to the option and exclusion map
    public void UpdateListOrderMap(String orderCategory, String firstValue, String secondValue, int orderGap, Map<String, Set<String>> optionsMap, Map<String, Set<String>> exclusionsMap, Map<String, List<String>> data){

        List<String> categoryValues = data.get(orderCategory);

        if(orderGap == 0 || orderGap == 1){

            String first = categoryValues.get(0);
            String last = categoryValues.get(categoryValues.size()-1);

            //Update exclusion map
            this.utils.UpdateExclusionMap(firstValue, last, optionsMap, exclusionsMap);
            this.utils.UpdateExclusionMap(secondValue, first, optionsMap, exclusionsMap);

        }
        else{
            for (int i=0; i<orderGap; i++){

                String first = categoryValues.get(i);
                String last = categoryValues.get(categoryValues.size()-(i+1));

                //Update exclusion map
                this.utils.UpdateExclusionMap(firstValue, last, optionsMap, exclusionsMap);
                this.utils.UpdateExclusionMap(secondValue, first, optionsMap, exclusionsMap);
            }
        }
    }


    //Put mark "Yes" on the values present in option map
    public void ImplementOptions(Map<String, Set<String>> optionsMap, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping){

        for (Map.Entry<String, Set<String>> entry : optionsMap.entrySet()){
            Set<String> values = entry.getValue();

            for (String str : values){
                this.mapping.MarkTick(entry.getKey(), str, data, dataMapping);
            }
        }
    }

    //Put mark "NO" on the values present in exclusion map
    public void ImplementExclusions(Map<String, Set<String>> exclusionsMap, Map<String, Map<String, String>> dataMapping){
        for (Map.Entry<String, Set<String>> entry : exclusionsMap.entrySet()){
            Set<String> values = entry.getValue();

            for (String str : values){
                this.mapping.PutMark(entry.getKey(), str, dataMapping, "no");
            }
        }
    }

}
