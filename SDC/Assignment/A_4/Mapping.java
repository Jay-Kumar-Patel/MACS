import java.util.*;

public class Mapping
{
    Utils utils;

    public Mapping(){
        utils = new Utils();
    }

    //Initialize Mapping when only two category present into puzzle.
    public void InitilazeMapping(Map<String, List<String>> data, Map<String, Map<String, String>> data_mapping){

        //Get Values
        List<String> FirstEntryValues = new ArrayList<>();
        List<String> SecondEntryValues = new ArrayList<>();

        int index = 1;

        for (Map.Entry<String, List<String>> entry : data.entrySet()){
            if (index == 1){
                FirstEntryValues = entry.getValue();
                index++;
            }
            else {
                SecondEntryValues = entry.getValue();
            }
        }

        //Create Mapping
        Map<String, String> FirstEntryMapping = new HashMap<>();
        Map<String, String> SecondEntryMapping = new HashMap<>();

        for(String str : FirstEntryValues){
            FirstEntryMapping.put(str, null);
        }

        for(String str : SecondEntryValues){
            SecondEntryMapping.put(str, null);
        }

        //Assign Mapping
        for(String str : FirstEntryValues){
            data_mapping.put(str, SecondEntryMapping);
        }

        for(String str : SecondEntryValues){
            data_mapping.put(str, FirstEntryMapping);
        }
    }

    //Add the new values to each mapping.
    public void AddMapping(List<String> values, Map<String, Map<String, String>> data_mapping){

        for (Map.Entry<String, Map<String, String>> entry : data_mapping.entrySet()){

            if(!utils.SkipEntry(entry.getKey(), values)){
                Map<String, String> entryValues = entry.getValue();
                for(String str : values){
                    entryValues.put(str, null);
                }
            }
        }
    }


    //Create mapping for new values.
    public void CreateMapping(List<String> values, Map<String, Map<String, String>> data_mapping){

        Set<String> uniqueValues = new HashSet<>();

        //Get all unique values from exiting mapping.
        for (Map.Entry<String, Map<String, String>> entry : data_mapping.entrySet()){
            uniqueValues.add(entry.getKey());
            for(Map.Entry<String, String> currEntryValues : entry.getValue().entrySet()){
                uniqueValues.add(currEntryValues.getKey());
            }
        }

        //Create values for new upcoming values.
        Map<String, String> mapping = new HashMap<>();
        for (String str : uniqueValues){
            mapping.put(str, null);
        }

        //Assign Mapping to new values.
        for(String str : values){
            data_mapping.put(str, mapping);
        }
    }

    //Mark Cross on give from and to into dataMapping.
    public boolean PutMark(String from, String to, Map<String, Map<String, String>> dataMapping, String markSign)
    {
        //Get Mapping from dataMapping
        Map<String, String> fromMapping = new HashMap<>(dataMapping.get(from));
        Map<String, String> toMapping = new HashMap<>(dataMapping.get(to));

        //If we have to mark yes than check it was not already marked by no.
        if (markSign.equals("yes")){
            if (fromMapping.get(to) != null && fromMapping.get(to).equals("no"))
                return false;

            if (toMapping.get(from) != null && toMapping.get(from).equals("no"))
                return false;
        }
        //If we have to mark no than check it was not already marked by yes.
        else{
            if (fromMapping.get(to) != null && fromMapping.get(to).equals("yes"))
                return false;

            if (toMapping.get(from) != null && toMapping.get(from).equals("yes"))
                return false;
        }

        //Update the dataMapping
        fromMapping.put(to, markSign);
        toMapping.put(from, markSign);

        //Update Mapping
        dataMapping.put(from, fromMapping);
        dataMapping.put(to, toMapping);

        return true;
    }


    //Tick the box and also mark cross on the remaining boxes in that particular row and col.
    public boolean MarkTick(String from, String to, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {

        if (!PutMark(from, to, dataMapping, "yes"))
            return false;

        //Now mark cross in entire row and col.
        String fromCategory = utils.getCategory(from, data);
        String toCategory = utils.getCategory(to, data);

        List<String> fromValues = new ArrayList<>(data.get(fromCategory));
        List<String> toValues = new ArrayList<>(data.get(toCategory));

        fromValues.remove(from);
        toValues.remove(to);

        boolean ans = true;

        //Mark cross in row
        for (String str : toValues){
            ans &= PutMark(from, str, dataMapping, "no");
        }

        //Mark cross in col
        for (String str : fromValues){
            ans &= PutMark(to, str, dataMapping, "no");
        }

        return ans;
    }


    //Check is there any row where all the values are marked cross, if yes than put tick on the remaining value.
    public boolean AutoFillRowCol(Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {
        boolean change = false;

        //Iterate through every mapping into dataMapping
        for (Map.Entry<String, Map<String, String>> dataMappingEntry : dataMapping.entrySet()){

            //Get the category of current key into dataMapping
            String keyCategory = utils.getCategory(dataMappingEntry.getKey(), data);

            //Iterate through every category other than keyCategory.
            for (Map.Entry<String, List<String>> dataEntry : data.entrySet()){

                if(!dataEntry.getKey().equals(keyCategory)){

                    Set<String> values = new HashSet<>(dataEntry.getValue());

                    //Count the number of false present in particular row.
                    int falseCount = 0;

                    //Maintain if any tick present in that row.
                    boolean isTickPresent = false;

                    //Hold the key in which null value is present in that row.
                    String key = null;

                    //Iterate through each value of current loop of dataMapping.
                    for(Map.Entry<String, String> entry : dataMappingEntry.getValue().entrySet()){

                        //We only check those value who belong to our current category.
                        if(values.contains(entry.getKey())){
                            if(entry.getValue() == null)
                                key = entry.getKey();
                            else if (entry.getValue().equals("yes")) {
                                isTickPresent = true;
                                break;
                            }
                            else
                                falseCount++;
                        }
                    }

                    //If No tick present and only one box is remaining to mark.
                    if (!isTickPresent){
                        if(falseCount == values.size()-1){
                            MarkTick(dataMappingEntry.getKey(),key,data, dataMapping);
                            change = true;
                        }
                    }
                }
            }
        }

        return change;
    }


    //Fill the grid with tick
    public boolean IntutionMappingForTick(Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {
        Map<String, Set<String>> map = new HashMap<>();

        //Get all the yes from entire data mapping.
        for (Map.Entry<String, Map<String, String>> entry : dataMapping.entrySet())
        {
            Set<String> set = new HashSet<>();
            for(Map.Entry<String, String> entryValuesMap : entry.getValue().entrySet()){
                if(entryValuesMap.getValue() != null && entryValuesMap.getValue().equals("yes")){
                    set.add(entryValuesMap.getKey());
                }
            }
            if(!set.isEmpty()){
                map.put(entry.getKey(), set);
            }
        }

        Map<String, String> changes = new HashMap<>();

        //Find if any change possible
        for (Map.Entry<String, Set<String>> entry : map.entrySet())
        {
            for (String str : entry.getValue()){

                List<String> set = new ArrayList<>(map.get(str));

                set.remove(entry.getKey());

                for (String checkStr : set)
                {
                    Set<String> crossCheck = map.get(checkStr);

                    if(crossCheck == null){
                        changes.put(entry.getKey(), checkStr);
                    }
                    else{
                        if (!crossCheck.contains(entry.getKey())){
                            changes.put(entry.getKey(), checkStr);
                        }
                    }
                }
            }
        }


        if(changes.size() > 0){
            applyChanges(changes, data, dataMapping, "yes");
            return true;
        }

        return false;
    }

    //Apply those chnages on grid
    private boolean applyChanges(Map<String, String> map, Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping, String markSign)
    {
        boolean changeHappened = false;

        if (markSign.equals("yes")){
            for(Map.Entry<String, String> entry : map.entrySet()){
                MarkTick(entry.getKey(), entry.getValue(), data, dataMapping);
                changeHappened = true;
            }
        }
        else{
            for(Map.Entry<String, String> entry : map.entrySet()){
                PutMark(entry.getKey(), entry.getValue(), dataMapping, "no");
                changeHappened = true;
            }
        }

        return changeHappened;
    }


    //Fill the grid with cross.
    public boolean IntutionMappingForCross(Map<String, List<String>> data, Map<String, Map<String, String>> dataMapping)
    {
        Map<String, Set<String>> mapOfYes = new HashMap<>();
        Map<String, Set<String>> mapOfNo = new HashMap<>();

        //Get all the yes and no from entire data mapping.
        for (Map.Entry<String, Map<String, String>> entry : dataMapping.entrySet())
        {
            Set<String> setOfYes = new HashSet<>();
            Set<String> setOfNo = new HashSet<>();
            for(Map.Entry<String, String> entryValuesMap : entry.getValue().entrySet()){
                if(entryValuesMap.getValue() != null){
                    if(entryValuesMap.getValue().equals("yes")){
                        setOfYes.add(entryValuesMap.getKey());
                    }
                    else{
                        setOfNo.add(entryValuesMap.getKey());
                    }

                }
            }
            if(!setOfYes.isEmpty()){
                mapOfYes.put(entry.getKey(), setOfYes);
            }
            if(!setOfNo.isEmpty()){
                mapOfNo.put(entry.getKey(), setOfNo);
            }
        }

        Map<String, String> changes = new HashMap<>();

        //Find if any change possible
        for (Map.Entry<String, Set<String>> entry : mapOfYes.entrySet())
        {
            String key = entry.getKey();
            List<String> set = new ArrayList<>(mapOfNo.get(key));

            Set<String> values = entry.getValue();

            for (String from : values){
                for (String to : set){
                    changes.put(from, to);
                }
            }
        }

        return applyChanges(changes, data, dataMapping, "no");
    }

}
