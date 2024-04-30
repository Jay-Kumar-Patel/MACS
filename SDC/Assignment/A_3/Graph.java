import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    //This class contain basic methods which need during the oprations of this class.
    Utils utils;


    public Graph(){
        utils = new Utils();
    }
    

    /**
     * Depth First Search (DFS) algorithm to traverse a graph and construct a path
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @param currVertex - The current vertex being explored.
     * @param visited - map to track visited vertices.
     * @param dfsPath - An arraylist to track vertices in the current DFS path.
     */
    private void dfs(Map<String, Set<String>> graph, String currVertex, Map<String, Boolean> visited, ArrayList<String> dfsPath){

        // Base case: If the current vertex has already been visited, return
        if(visited.get(currVertex)){
            return;
        }

        // Mark the current vertex as visited
        visited.put(currVertex, true);

        // Add the current vertex to the DFS path
        dfsPath.add(currVertex);

        // Get neighbors of the current vertex and recursively call dfs for each neighbor
        Set<String> neighbours = graph.get(currVertex);

        if(neighbours != null && !neighbours.isEmpty()){
            for(String currString : neighbours){
                dfs(graph, currString, visited, dfsPath);
            }
        }

        return;
    }


    /**
     * Merge lists of DFS paths to get the unique lists.
     * @param list - which we have to merge.
     * @return - List which contain all the unique sublists.
     */
    private ArrayList<ArrayList<String>> mergeList(ArrayList<ArrayList<String>> list){

        // Initialize an empty list to store the merged paths
        ArrayList<ArrayList<String>> mergedList = new ArrayList<>();

        // Flag to track whether a merge operation has been performed
        boolean isMergePerform = false;

        // Continue merging until the input list is empty
        while (list.size() > 0) {

            // If only one path is left, add it to the merged list and break
            if(list.size() == 1){
                mergedList.add(list.get(0));
                break;
            }

            // Initialize a list to store similar DFS paths for this particular loop
            ArrayList<ArrayList<String>> similarDfsPath = new ArrayList<>();

            // Set to store the indices of lists that are merge during this loop.
            Set<Integer> index = new HashSet<>();

            ArrayList<String> currDfsPath = list.get(0);

            similarDfsPath.add(currDfsPath);

            list.remove(0);
        
            // Iterate through characters in the current DFS path
            for (String currChar : currDfsPath) {

                // Iterate through remaining paths in the input list
                for(int i=0;i<list.size();i++){

                    // Iterate through characters in the current path
                    for(int j=0;j<list.get(i).size();j++){

                        // If a similar character is found, add the path to similarDfsPath
                        if(list.get(i).get(j).equals(currChar)){

                            similarDfsPath.add(list.get(i));
                            index.add(i);
                            isMergePerform = true;
                            break;

                        }
                    }
                }
            }


            // Extract unique elements from the merged paths
           Set<String> uniqueElementsformDfsPaths = new HashSet<>();

            for(int i=0;i<similarDfsPath.size();i++){

                for(int j=0;j<similarDfsPath.get(i).size();j++){

                    uniqueElementsformDfsPaths.add(similarDfsPath.get(i).get(j));

                }
            }

             // Convert the set of unique elements to an ArrayList
            ArrayList<String> convertSetToArrayList = new ArrayList<>();

            for(String i : uniqueElementsformDfsPaths){
                convertSetToArrayList.add(i);
            }

             // Add the merged list to the final result
            mergedList.add(convertSetToArrayList);

             // Sort the indices in descending order for removal
            ArrayList<Integer> sortedIndex = new ArrayList<>(index);

            quickSort(sortedIndex, 0, sortedIndex.size()-1);

            // Remove the merged paths from the input list
            for (int i : sortedIndex) {
                list.remove(i);
            }

            // If a merge operation was performed, add the merged list back to the input list
            if(isMergePerform){
                isMergePerform = false;
                list.add(mergedList.get(mergedList.size()-1));
                mergedList.remove(mergedList.size() - 1);
            }

        }

        return mergedList;
    }


    /**
     * Find disjoint graphs within the input graph
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @return - Arraylist which contain the elements which are comparable to eachother.
     */
    public ArrayList<ArrayList<String>> disjointGraphs(Map<String, Set<String>> graph){

        // Initialize a list to store composite graphs
        ArrayList<ArrayList<String>> compositeGraph = new ArrayList<>();

        ArrayList<ArrayList<String>> dfsPaths = new ArrayList<>();

        // Create a map to track visited vertices
        Map<String, Boolean> visited = utils.createVisitedMap(graph);

        // Iterate through the vertices of the graph
        for (Map.Entry<String, Boolean> entry : visited.entrySet()) {

            String key = entry.getKey();
            boolean value = entry.getValue();

            if(value){
                continue;
            }
            else{
                ArrayList<String> currDfsPath = new ArrayList<>();

                Map<String, Boolean> tempVisited = utils.createVisitedMap(graph);

                //Perfrom dfs for unvisited neighbours.
                dfs(graph, key, tempVisited, currDfsPath);

                dfsPaths.add(currDfsPath);
            }
        }

        compositeGraph = mergeList(dfsPaths);
        
        return compositeGraph;
    }


    /**
     * Find puzzles that are harder than the input puzzle.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @param puzzle - for which harder puzzles are to be found.
     * @param compositeGraph - graph representing the puzzles that the compareable to each other.
     * @return An ArrayList containing puzzles that are harder than the input puzzle.
     */
    public ArrayList<String> getHarderPuzzles(Map<String, Set<String>> graph, String puzzle, ArrayList<ArrayList<String>> compositeGraph){

        // Initialize an ArrayList to store the result
        ArrayList<String> ans = new ArrayList<>();

        ArrayList<String> dfsPath = new ArrayList<>();

        Map<String, Boolean> visited = utils.createVisitedMap(graph);

        // Perform DFS traversal starting from the input puzzle
        dfs(graph, puzzle, visited, dfsPath);

        // Iterate through the composite graphs
        for(int i=0;i<compositeGraph.size();i++){

            for(int j=0;j<compositeGraph.get(i).size();j++){

                // If the puzzle is found in a cluster
                if(compositeGraph.get(i).get(j).equals(puzzle)){

                    // Add all puzzles in the cluster to the result
                    for(int p=0;p<compositeGraph.get(i).size();p++){

                        ans.add(compositeGraph.get(i).get(p));

                    }
                    break;
                }
            }
        }

        // Remove puzzles that are present in our dfs path.
        ans.removeAll(dfsPath);

        ArrayList<String> harderPuzzles = new ArrayList<>(ans);

        for(String str : ans){
            ArrayList<String> strDfsPath = new ArrayList<>();
            Map<String, Boolean> tempVisited = utils.createVisitedMap(graph);
            // Perform DFS for each puzzle to check if it's harder
            dfs(graph, str, tempVisited, strDfsPath);
            boolean isPresent = false;
            for(String pathString : strDfsPath){
                if(pathString.equals(puzzle)){
                    isPresent = true;
                }
            }
            // If the input puzzle is not reachable, remove the puzzle from the result
            if(!isPresent){
                harderPuzzles.remove(str);
            }
        }

        return harderPuzzles;
    }


    /**
     * Find the hardest puzzles within the data.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @param compositeGraph - graph representing the puzzles that the compareable to each other.
     * @return - An ArrayList containing the hardest puzzles.
     */
    public ArrayList<String> getHardestPuzzles(Map<String, Set<String>> graph, ArrayList<ArrayList<String>> compositeGraph){
        
        ArrayList<String> hardestPuzzles = new ArrayList<>();

        Set<String> uniqueElements = utils.createUniqueElements(graph);

        // Iterate through each puzzle
        for(String str : uniqueElements){
            ArrayList<String> harderPuzzleOfStr = getHarderPuzzles(graph, str, compositeGraph);

            // If no harder puzzles exist or the list is empty, add the puzzle to hardestPuzzles
            if(harderPuzzleOfStr == null || harderPuzzleOfStr.isEmpty()){
                hardestPuzzles.add(str);
            }
        }

        return hardestPuzzles;
    }


    /**
     * Find equivalent puzzles within the data.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @param puzzle - The puzzle for which equivalent puzzles are to be found.
     * @param compositeGraph - graph representing the puzzles that the compareable to each other.
     * @return - An ArrayList containing equivalent puzzles to the input puzzle.
     */
    public ArrayList<String> getEquivalentPuzzles(Map<String, Set<String>> graph, String puzzle, ArrayList<ArrayList<String>> compositeGraph){

        ArrayList<String> equivalentPuzzles = new ArrayList<>();

        // Retrieve all cycles from the graph
        ArrayList<ArrayList<String>> cycles = getAllCycles(graph);

        // Merge cycles to obtain unique cycles
        ArrayList<ArrayList<String>> uniqueCycles = mergeList(cycles);

        // Iterate through each unique cycle
        for (ArrayList<String> cycle : uniqueCycles) {
            for (String element : cycle) {
                if(element.equals(puzzle)){
                    // If found, add all puzzles in the cycle to equivalentPuzzles
                    for(String str : cycle){
                        equivalentPuzzles.add(str);
                    }
                    break;
                }
            }
        }

        equivalentPuzzles.add(puzzle);

        return equivalentPuzzles;
    }


    /**
     * Retrieve all cycles within the graph.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @return - An ArrayList containing all cycles in the graph.
     */
    private ArrayList<ArrayList<String>> getAllCycles(Map<String, Set<String>> graph){

        ArrayList<ArrayList<String>> allCycles = new ArrayList<>();

        Map<String, Boolean> visited = utils.createVisitedMap(graph);

        Set<String> uniqueElements = utils.createUniqueElements(graph);

        Set<String> dfsPath = new HashSet<>();

         // Call dfs for each unique element in a graph.
        for (String str : uniqueElements) {
            dfsForCycleDetection(graph, str, visited, dfsPath, new ArrayList<>(), allCycles);
        }

        return allCycles;
    }


    /**
     * Depth First Search (DFS) for cycle detection within the graph.
     * @param graph - representing puzzles and the set of puzzles easier than that puzzle.
     * @param currVertex - The current vertex being explored.
     * @param visited - map to track visited vertices.
     * @param path - A set to track vertices in the current DFS path.
     * @param currPath - An ArrayList to store the current DFS path.
     * @param allCycles - An ArrayList to store all the cycles present in a graph.
     */
    private void dfsForCycleDetection(Map<String, Set<String>> graph, String currVertex, Map<String, Boolean> visited, Set<String> path, ArrayList<String> currPath,ArrayList<ArrayList<String>> allCycles){

        if (visited.get(currVertex)) {

            if (path.contains(currVertex)) {

                ArrayList<String> cyclePath = new ArrayList<>();

                int pathStartIndex = currPath.indexOf(currVertex);

                // Construct the cycle path
                for (int i = pathStartIndex; i < currPath.size(); i++) {
                    cyclePath.add(currPath.get(i));
                }

                // Add the cycle path to the list of all cycles
                allCycles.add(cyclePath);
            }

            return;
        }

        visited.put(currVertex, true);

        path.add(currVertex);

        currPath.add(currVertex);

        Set<String> neighbours = graph.get(currVertex);

        // Explore each neighbor recursively
        if(neighbours != null && !neighbours.isEmpty()){
            for(String currString : neighbours){

                if(path.contains(currString)){
    
                    ArrayList<String> cyclePath = new ArrayList<>();
    
                    int pathStartIndex = currPath.indexOf(currString);
    
                    for(int i=pathStartIndex;i<currPath.size();i++){
                        cyclePath.add(currPath.get(i));
                    }
    
                    allCycles.add(cyclePath);
                }
                else{
                    dfsForCycleDetection(graph, currString, visited, path, currPath, allCycles);
                }
            }
        }

        // Remove the current vertex from the DFS path
        path.remove(currVertex);

        if(currPath.size() > 0){
            currPath.remove(currPath.size() - 1);
        }

        visited.put(currVertex, false);
    }

    /**
     * Sorts the given ArrayList using the quicksort algorithm.
     * @param arr - The ArrayList to be sorted.
     * @param low - The starting index of the subarray to be sorted.
     * @param high - The ending index of the subarray to be sorted.
     */
    private void quickSort(ArrayList<Integer> arr, int low, int high){
        
        if(low < high){

            // Partition the array
            int pivotIndex = partition(arr, low, high);

            //recursively sort the subarrays
            quickSort(arr, low, pivotIndex-1);
            quickSort(arr, pivotIndex+1, high); 
        }
    }


    /**
     * Partitions the ArrayList for quicksort.
     * @param arr - The ArrayList to be partitioned.
     * @param low - The starting index of the subarray.
     * @param high - The ending index of the subarray.
     * @return - The pivot index.
     */
    private int partition(ArrayList<Integer> arr, int low, int high){

        // Choose the pivot element
        int pivot = arr.get(low);
        int i = low;
        int j = high;

        while (i < j) {

            //Move right side untill we get greater than pivot.
            while (arr.get(i) >= pivot && i <= high - 1) {
                i++;
            }

            //Move left side untill we get smaller than pivot.
            while (arr.get(j) < pivot  && j >= low + 1) {
                j--;
            }
           
            //Swap elements
            if(i < j){
                swap(arr, i, j);
            }
        }

        // Move pivot to its correct position
        swap(arr, low, j);

        return j;
    }

    /**
     * Swaps elements at the given indices in the ArrayList.
     * @param arr - The ArrayList in which elements are to be swapped.
     * @param i - The index of the first element.
     * @param j - The index of the second element.
     */
    private void swap(ArrayList<Integer> arr, int i, int j){
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }


}