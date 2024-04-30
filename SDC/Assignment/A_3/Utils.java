import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {
    
    /**
     * Creates a set containing unique elements from the given graph. 
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @return - A set containing unique elements (vertices) from the graph.
     */
    public Set<String> createUniqueElements(Map<String, Set<String>> graph){

        Set<String> uniqueElements = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            
            Set<String> neighbours = entry.getValue();
            String key = entry.getKey();
            
            uniqueElements.add(key);
            for (String str : neighbours) {
                uniqueElements.add(str);
            }
        }

        return uniqueElements;
    }


    /**
     * Creates a map from unique elements and assign false to each and every element.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @return - Map in which key as unique elements and value as false.
     */
    public Map<String, Boolean> createVisitedMap(Map<String, Set<String>> graph){

        Set<String> uniqueElements = createUniqueElements(graph);

        Map<String, Boolean> visited = new HashMap<>();

        for(String str : uniqueElements){
            visited.put(str, false);
        }

        return visited;
    }


    /**
     * Converts a 2D ArrayList to a 2D Set.
     * @param arrayList - The 2D ArrayList to be converted.
     * @return - A 2D Set containing unique elements from the input ArrayList.
     */
    public Set<Set<String>> convert2DArrayListTo2DSet(ArrayList<ArrayList<String>> arrayList){

        Set<Set<String>> set = new HashSet<>();

        for (ArrayList<String> innerList : arrayList) {
            Set<String> innerSet = new HashSet<>(innerList);
            set.add(innerSet);
        }

        return set;
    }


    /**
     * Converts a 2D Set to a 2D Arraylist.
     * @param set - The 2D Set to be converted.
     * @return - A 2D ArrayList containing elements from the input Set. 
     */
    public ArrayList<ArrayList<String>> convert2DSetTo2DArrayList(Set<Set<String>> set){

        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

        for (Set<String> innerSet : set) {
            ArrayList<String> list = new ArrayList<>(innerSet);
            arrayList.add(list);
        }

        return arrayList;
    }


    /**
     * Calculates the size of a current data.
     * @param input - Data from which we have to calculate the size
     * @return - Size of data.
     */
    public float calculateSize(Map<String, Integer> input){
        float size = 0;
        for(Map.Entry<String, Integer> entry : input.entrySet()){
            size += entry.getValue();
        }
        return size;
    }


    /**
     * Checks if a given frequency meets the support threshold.
     * @param frequency - The frequency of an itemset.
     * @param size - The size of the data.
     * @param support - he minimum support threshold.
     * @return - true if the frequency meets the support threshold, else false.
     */
    public boolean checkSupport(int frequency, float size, int support){
        return (frequency/size)*100 > support ? true : false;
    }

}