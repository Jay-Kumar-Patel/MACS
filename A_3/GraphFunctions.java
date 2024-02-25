import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphFunctions {
    
    public void dfs(HashMap<String, ArrayList<String>> adj, String currVertex, HashMap<String, Boolean> visited, ArrayList<String> ans){

        if(visited.get(currVertex)){
            return;
        }

        visited.put(currVertex, true);
        ans.add(currVertex);

        ArrayList<String> neighbouStrings = adj.get(currVertex);

        for(String currString : neighbouStrings){
            dfs(adj, currString, visited, ans);
        }

        return;
    }


    public void disjointGraphs(HashMap<String, ArrayList<String>> adj, ArrayList<String> uniqueElements,String currVertex, HashMap<String, Boolean> visited, ArrayList<ArrayList<String>> ans){

        ArrayList<ArrayList<String>> tempAns = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : visited.entrySet()) {
            String key = entry.getKey();
            boolean value = entry.getValue();

            if(value){
                continue;
            }
            else{
                ArrayList<String> currList = new ArrayList<>();
                
                HashMap<String, Boolean> temp = new HashMap<>();
                for(String i : uniqueElements){
                    temp.put(i,false);
                }

                dfs(adj, key, temp, currList);
                tempAns.add(currList);
            }
        }
        
    
        while (tempAns.size() > 0) {

            if(tempAns.size() == 1){
                ans.add(tempAns.get(0));
                break;
            }

            ArrayList<ArrayList<String>> currAns = new ArrayList<>();
            Set<Integer> index = new HashSet<>();
            ArrayList<String> currList = tempAns.get(0);
            currAns.add(currList);
            tempAns.remove(0);
        
            for (String currChar : currList) {
                for(int i=0;i<tempAns.size();i++){
                    for(int j=0;j<tempAns.get(i).size();j++){
                        if(tempAns.get(i).get(j).equals(currChar)){
                            currAns.add(tempAns.get(i));
                            index.add(i);
                            break;
                        }
                    }
                }
            }

           Set<String> ansArray = new HashSet<>();

            for(int i=0;i<currAns.size();i++){
                for(int j=0;j<currAns.get(i).size();j++){
                    ansArray.add(currAns.get(i).get(j));
                }
            }

            ArrayList<String> finalAns = new ArrayList<>();
            for(String i : ansArray){
                finalAns.add(i);
            }

            ans.add(finalAns);

            ArrayList<Integer> sortedIndex = new ArrayList<>(index);
            quickSort(sortedIndex, 0, sortedIndex.size()-1);
            for (int i : sortedIndex) {
                tempAns.remove(i);
            }

        }
    }


    public void getHarderPuzzles(HashMap<String, ArrayList<String>> adj, ArrayList<String> uniqueElements, String puzzle, ArrayList<ArrayList<String>> arr, ArrayList<String> harderAns){

        ArrayList<String> dfsAns = new ArrayList<>();
        HashMap<String, Boolean> visited = new HashMap<>();
        for(int i=0;i<uniqueElements.size();i++){
            visited.put(uniqueElements.get(i), false);
        }
        dfs(adj, puzzle, visited, dfsAns);

        for(int i=0;i<arr.size();i++){
            for(int j=0;j<arr.get(i).size();j++){
                if(arr.get(i).get(j).equals(puzzle)){
                    for(int p=0;p<arr.get(i).size();p++){
                        harderAns.add(arr.get(i).get(p));
                    }
                    break;
                }
            }
        }

        harderAns.removeAll(dfsAns);
    }


    public void getHardestPuzzles(HashMap<String, ArrayList<String>> adj, ArrayList<String> uniqueElements, ArrayList<ArrayList<String>> arr, ArrayList<String> hardestAns){
        
        for(int i=0;i<arr.size();i++){
            ArrayList<String> tempAns = new ArrayList<>();
            for(int j=0;j<arr.get(i).size();j++){
                HashMap<String, Boolean> visited = new HashMap<>();
                for(int z=0;z<uniqueElements.size();z++){
                    visited.put(uniqueElements.get(z), false);
                }
                ArrayList<String> dfsAns = new ArrayList<>();
                dfs(adj, arr.get(i).get(j), visited, dfsAns);
                dfsAns.remove(0);
                tempAns.addAll(dfsAns);
            }
            Set<String> removeDuplicate = new HashSet<>(tempAns);
            ArrayList<String> tempList = new ArrayList<>(arr.get(i));

            tempList.removeAll(removeDuplicate);
            for(String str : tempList){
                hardestAns.add(str);
            }
        }
    }



    public ArrayList<ArrayList<String>> getAllCycles(HashMap<String, ArrayList<String>> adj, ArrayList<String> uniqueElements){

        ArrayList<ArrayList<String>> ans = new ArrayList<>();

        HashMap<String, Boolean> visited = new HashMap<>();
        for(int i=0;i<uniqueElements.size();i++){
            visited.put(uniqueElements.get(i), false);
        }

        Set<String> path = new HashSet<>();

        for (String str : uniqueElements) {
            dfsForCycleDetection(adj, str, visited, path, new ArrayList<>(), ans);
        }

        return ans;
    }

    private void dfsForCycleDetection(HashMap<String, ArrayList<String>> adj, String currVertex, HashMap<String, Boolean> visited, Set<String> path, ArrayList<String> currPath,ArrayList<ArrayList<String>> ans){

        if(visited.get(currVertex)){
            return;
        }

        visited.put(currVertex, true);
        path.add(currVertex);
        currPath.add(currVertex);

        ArrayList<String> neighbouStrings = adj.get(currVertex);

        for(String currString : neighbouStrings){
            if(path.contains(currString)){
                ArrayList<String> tempPath = new ArrayList<>();
                int pathStartIndex = currPath.indexOf(currString);
                for(int i=pathStartIndex;i<currPath.size();i++){
                    tempPath.add(currPath.get(i));
                }
                ans.add(tempPath);
            }
            else{
                dfsForCycleDetection(adj, currString, visited, path, currPath, ans);
            }
        }

        path.remove(currVertex);
        if(currPath.size() > 0){
            currPath.remove(currPath.size() - 1);
        }
    }

    private void quickSort(ArrayList<Integer> arr, int low, int high){
        if(low < high){
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex-1);
            quickSort(arr, pivotIndex+1, high); 
        }
    }

    private int partition(ArrayList<Integer> arr, int low, int high){

        int pivot = arr.get(low);
        int i = low;
        int j = high;

        while (i < j) {

            while (arr.get(i) <= pivot && i <= high - 1) {
                i++;
            }

            while (arr.get(j) > pivot  && j >= low + 1) {
                j--;
            }
           

            if(i < j){
                swap(arr, i, j);
            }
        }

        swap(arr, low, j);

        return j;
    }

    private void swap(ArrayList<Integer> arr, int i, int j){
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }


}
