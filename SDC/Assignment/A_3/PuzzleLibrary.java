import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PuzzleLibrary {

    //This class contain methods related to graph (Ex - traversal, get harder, hardest, equivalent, and puzzle group using dfs)
    static Graph graph;

    //This class contain basic methods which need during the oprations in above graph class.
    static Utils utils;

    //Support for current data.
    static int support = 0;

    //Graph created from input.
    Map<String, Set<String>> dataGraph;

    //Data
    Map<String, Integer> input;

    /**
     * Constructor for PuzzleLibrary class
     * @param newSupport - Initial support while creating object.
     */
    public PuzzleLibrary( int newSupport ) {

        // Check if support value is valid
        if (newSupport < 0 || newSupport > 100) {
            throw new IllegalArgumentException("Invalid support value. Support must be between 0 and 100.");
        }

        // Initialize support and other necessary components
        support = newSupport;
        graph = new Graph();
        utils = new Utils();
        dataGraph = new HashMap<>();
        input = new HashMap<>();
    }

    // Main method  
    public static void main(String[] args) throws FileNotFoundException {
        
        // Create an instance of PuzzleLibrary with support value 5
        PuzzleLibrary library = new PuzzleLibrary(5);
        
        
    }


    /**
     * Method to set support value
     * @param newSupport - New support set to exsting data.
     * @return - true if successfully set support, else return false.
     */
    public boolean setSupport( int newSupport ) {

        // Check if new support value is valid
        if(newSupport < 0 || newSupport > 100){
            return false;
        }

        // Update support value and clear graph.
        support = newSupport;
        dataGraph.clear();
        return true;
    }


    /**
     * Method to get compare puzzles as input stream and processed them.
     * @param streamOfComparisons - Input stream in which puzzle comparisons are present.
     * @return - true if successfully input was readed, else return false.
     */
    public boolean comparePuzzles( BufferedReader streamOfComparisons ) {

        try {

            // Check if input stream is valid
            if (streamOfComparisons == null || !streamOfComparisons.ready()) {
                return false;
            }

             // Read data and clear graph if successful
            if (readData(streamOfComparisons)){
                dataGraph.clear();
                return true;
            }
            else{
                return false;
            }
        } 
        catch (Exception e) {
            return false;
        }
    }


    /**
     * Method to read data from input stream
     * @param streamOfComparisons - Stream in which comparison of puzzles are present
     * @return - true if successfully input was processed, else return false.
     */
    private boolean readData(BufferedReader streamOfComparisons){
        try {

            //Process bufferedReader and initially store data in temporary input.
            Map<String, Integer> tempInput = new HashMap<>();

            String nextLine;
            int itr = 0;
            while ((nextLine = streamOfComparisons.readLine()) != null) {

                if (nextLine.equals("")) {
                    continue;
                }

                nextLine = nextLine.trim();

                ArrayList<String> puzzles = new ArrayList<>(Arrays.asList(nextLine.split("\\t")));

                if (puzzles.size() != 2) {
                    return false;
                }

                if(puzzles.get(0).equals(puzzles.get(1))){
                    return false;
                }
    
                String str = puzzles.get(0) + "_" + puzzles.get(1);

                if(tempInput.containsKey(str)){
                    tempInput.put(str, tempInput.get(str) + 1);
                }
                else{
                    tempInput.put(str, 1);
                }

                itr++;
            }

            if (itr == 0) {
                return false;
            }

            //Copy data into main input variable.
            if(input.size() == 0){
                input.putAll(tempInput);
            }
            else{
                insertData(tempInput);
            }
            
            return true;
        } 
        catch (IOException io) {
            return false;
        }
    }


    /**
     * Method to copy data from temporary input to permanent input.
     * @param tempInput - Temporary Input
     */
    private void insertData(Map<String, Integer> tempInput){
        for(Map.Entry<String, Integer> entry : tempInput.entrySet()){
            if(input.containsKey(entry.getKey())){
                input.put(entry.getKey(), tempInput.get(entry.getKey()) + 1);
            }
            else{
                input.put(entry.getKey(), 1);
            }
        }
    }


    
    // Method to check and update graph
    //This function create a graph of Map<String, Set<String>> from data of type Map<String, Integer>.
    private void checkDataGraph(){

        if(dataGraph == null || dataGraph.isEmpty()){

            float size = utils.calculateSize(input);

            for(Map.Entry<String, Integer> entry : input.entrySet()){

                //Check the support for particular entry
                if(utils.checkSupport(entry.getValue(), size, support)){

                    ArrayList<String> puzzles = new ArrayList<>(Arrays.asList(entry.getKey().split("_")));

                    if(dataGraph.containsKey(puzzles.get(0))){
                        Set<String> neighbours = dataGraph.get(puzzles.get(0));
                        neighbours.add(puzzles.get(1));
                        dataGraph.put(puzzles.get(0), neighbours);
                    }
                    else{
                        Set<String> neighbours = new HashSet<>();
                        neighbours.add(puzzles.get(1));
                        dataGraph.put(puzzles.get(0), neighbours);
                    }
                }
            }
        }
    }


    /**
     * Method to find equivalent puzzles
     * @param puzzle - Puzzle for which we want the equivalent puzzles.
     * @return - List of puzzles equivalent to given input puzzle.
     */
    public Set<String> equivalentPuzzles( String puzzle ) {

        //Validation of input puzzle
        if (!checkValidityOfPuzzle(puzzle) || dataGraph == null || dataGraph.isEmpty() || !checkPuzzlePresence(puzzle)) {
            return new HashSet<>();
        }

        //Find equivalent puzzle
        return new HashSet<>(graph.getEquivalentPuzzles(dataGraph, puzzle, graph.utils.convert2DSetTo2DArrayList(puzzleGroup())));
    }


    /**
     * Method to find harder puzzles
     * @param puzzle - Puzzle for which we want the harder puzzles.
     * @return - List of puzzles harder to given input puzzle.
     */
    public Set<String> harderPuzzles( String puzzle ) {

        //Validation of input puzzle
        if (!checkValidityOfPuzzle(puzzle) || dataGraph == null || dataGraph.isEmpty() || !checkPuzzlePresence(puzzle)) {
            return new HashSet<>();
        }

        //Find harder puzzle
        return new HashSet<>(graph.getHarderPuzzles(dataGraph, puzzle, graph.utils.convert2DSetTo2DArrayList(puzzleGroup())));
    }


    /**
     * Method to check validity of puzzle
     * @param puzzle - Puzzle for which we want to check if it is null or empty
     * @return - true if puzzle is not null or empty and also create graph, else return false.
     */
    private boolean checkValidityOfPuzzle(String puzzle){
        if(puzzle == null){
            return false;
        }

        puzzle = puzzle.trim();

        if(puzzle.isEmpty() || input == null || input.isEmpty()){
            return false;
        }

        //If puzzle is valid then create graph if not already exist.
        checkDataGraph();

        return true;
    }

    /**
     * Check wheather the given puzzle is alreday exist in our data or not.
     * @param puzzle - Puzzle that we have to verify.
     * @return - true if puzzle is present in our data, else false.
     */
    private boolean checkPuzzlePresence(String puzzle){
        Set<String> uniqueElements = utils.createUniqueElements(dataGraph);

        //Check if puzzle is present in our data or not.
        if(!uniqueElements.contains(puzzle)){
            return false;
        }

        return true;
    }

    /**
     * Method to check if input is null or empty.
     * @return - true if input not null or empty.
     */
    private boolean checkInputIsNullorEmpty(){
        if(input == null || input.isEmpty()){
            return false;
        }

        //If it had input then create graph if not already exist.
        checkDataGraph();

        return true;
    }


    /**
     * Method to find hardest puzzles
     * @return - List of puzzles hardest in our entire data.
     */
    public Set<String> hardestPuzzles( ) {

        //Validation of existing data.
        if (!checkInputIsNullorEmpty() || dataGraph == null || dataGraph.isEmpty()) {
            new HashSet<>();
        }

         //Find hardest puzzle
        return new HashSet<>(graph.getHardestPuzzles(dataGraph, graph.utils.convert2DSetTo2DArrayList(puzzleGroup())));
    }

    /**
     * Method to find group puzzles
     * @return - List of List of puzzles which are comparable to eachother.
     */
    public Set<Set<String>> puzzleGroup( ) {

        //Validation of existing data.
        if (!checkInputIsNullorEmpty() || dataGraph == null || dataGraph.isEmpty()) {
            new HashSet<>();
        }

        //Find group puzzles
        ArrayList<ArrayList<String>> ans = graph.disjointGraphs(dataGraph);
        return graph.utils.convert2DArrayListTo2DSet(ans);
    }
}