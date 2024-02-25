import java.util.ArrayList;
import java.util.HashMap;

public class PuzzleLibrary{

    public static void main(String[] args) {
        
        HashMap<String, ArrayList<String>> data = new HashMap<>();

        ArrayList forA = new ArrayList<>();
        forA.add("B");

        ArrayList forB = new ArrayList<>();
        forB.add("C");

        ArrayList forC = new ArrayList<>();
        forC.add("R");
        forC.add("Q");

        ArrayList forD= new ArrayList<>();
        forD.add("B");

        ArrayList forQ= new ArrayList<>();
        forQ.add("F");

        ArrayList forF= new ArrayList<>();
        forF.add("B");

        ArrayList forR= new ArrayList<>();
        forR.add("C");

        ArrayList forP = new ArrayList<>();
        forP.add("Z");

        ArrayList forZ = new ArrayList<>();
        forZ.add("T");

        ArrayList forE = new ArrayList<>();
        forE.add("S");

        data.put("A", forA);
        data.put("B", forB);
        data.put("C", forC);
        data.put("D", forD);
        data.put("R", forR);
        data.put("F", forF);
        data.put("Q", forQ);
        data.put("E", forE);
        data.put("S", new ArrayList<>());
        data.put("P", forP);
        data.put("Z", forZ);
        data.put("T", new ArrayList<>());


        ArrayList<String> uniqueElements = new ArrayList<>();
        uniqueElements.add("A");
        uniqueElements.add("B");
        uniqueElements.add("C");
        uniqueElements.add("D");
        uniqueElements.add("R");
        uniqueElements.add("F");
        uniqueElements.add("Q");
        uniqueElements.add("E");
        uniqueElements.add("S");
        uniqueElements.add("P");
        uniqueElements.add("Z");
        uniqueElements.add("T");

        HashMap<String, Boolean> visited = new HashMap<>();
        for(int i=0;i<uniqueElements.size();i++){
            visited.put(uniqueElements.get(i), false);
        }

        GraphFunctions graphFunctions = new GraphFunctions();
        //graphFunctions.dfs(data, "A", visited, ans);

        ArrayList<ArrayList<String>> ans = new ArrayList<>();

        graphFunctions.disjointGraphs(data, uniqueElements, "A", visited, ans);

        //System.out.println(String.valueOf(ans));

        // ArrayList<String> harderAns = new ArrayList<>();
        // graphFunctions.getHarderPuzzles(data, uniqueElements, "P", ans, harderAns);

        // System.out.println(String.valueOf(harderAns));

        // ArrayList<String> hardestAns = new ArrayList<>();
        // graphFunctions.getHardestPuzzles(data, uniqueElements, ans, hardestAns);
        // System.out.println(String.valueOf(hardestAns));

        ArrayList<ArrayList<String>> cycles = graphFunctions.getAllCycles(data, uniqueElements);
        System.out.println(String.valueOf(cycles));

        return;
    }
}