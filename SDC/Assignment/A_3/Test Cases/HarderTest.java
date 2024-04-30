import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;


class HarderTest {
    
    @Test
    void nullPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.harderPuzzles(null);
        assertEquals( expectedAns, ans, "Null Puzzle" );
    }

    @Test
    void emptyPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.harderPuzzles("");
        assertEquals( expectedAns, ans, "Empty Puzzle" );
    }

    @Test
    void harderBeforeInput() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.harderPuzzles("");
        assertEquals( expectedAns, ans, "Call harder puzzle before input stream" );
    }

    @Test
    void puzzleNotPresentInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.harderPuzzles("C");
        assertEquals( expectedAns, ans, "Call harder puzzle for that puzzle which is not present in our input stream" );
    }

    @Test
    void NoHarderPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.harderPuzzles("A");
        assertEquals( expectedAns, ans, "Their is no puzzle harder than given puzzle.");
    }

    @Test
    void support100Percent() {
        PuzzleLibrary library = new PuzzleLibrary( 1 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        library.setSupport(100);
        Set<String> ans = library.harderPuzzles("B");
        assertEquals( new HashSet<>(), ans, "Their is no puzzle harder than given puzzle as support is 100 Percent");
    }

    @Test
    void callTwice() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        library.comparePuzzles(new BufferedReader(new StringReader("D\tA")));
        Set<String> ansD = library.harderPuzzles("D");
        assertEquals( new HashSet<>(), ansD, "Call the harder puzzle twice.");

        Set<String> expectedB = new HashSet<>();
        expectedB.add("D");
        Set<String> ansB = library.harderPuzzles("B");
        assertEquals( expectedB, ansB, "Call the harder puzzle twice.");
    }


    @Test
    void oneCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("Y");
        expectedAns.add("D");

        Set<String> ans = library.harderPuzzles("C");
        assertEquals( expectedAns, ans, "Their is only one cycle in our input.");
    }


    @Test
    void multipleCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA\nC\tD\nD\tQ\nQ\tP\nP\tZ\nZ\tT\nT\tP")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("D");
        expectedAns.add("Q");
        Set<String> ans = library.harderPuzzles("Z");
        assertEquals( expectedAns, ans, "Their are 2 cycles in our input.");
    }

    @Test
    void entireInputIsCycle() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("B\tC\nC\tR\nC\tQ\nB\tD\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nY\tB")));
        Set<String> ans = library.harderPuzzles("B");
        assertEquals( new HashSet<>(), ans, "Our entire input form a cycle.");
    }

    @Test
    void nestedCycle1() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("Z\tH\nH\tC\nC\tA\nA\tB\nB\tQ\nQ\tL\nM\tC\nB\tC\nL\tM")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("H");
        expectedAns.add("Z");

        Set<String> ans = library.harderPuzzles("B");
        assertEquals( expectedAns, ans, "Our input had cycles inside a cycle.");
    }

    @Test
    void nestedCycle2() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA\nC\tD\nD\tE\nE\tC\nE\tF")));

        Set<String> ansForC = library.harderPuzzles("E");
        assertEquals( new HashSet<>(), ansForC, "Our input had cycles inside a cycle.");

        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("D");
        expectedAns.add("E");

        Set<String> ans = library.harderPuzzles("F");
        assertEquals( expectedAns, ans, "Our input had cycles inside a cycle.");
    }


    @Test
    void changeSupport() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("Z\tH\nH\tC\nC\tA\nA\tZ\nA\tB\nB\tQ\nQ\tL\nM\tC\nB\tC\nL\tM\nA\tB\nC\tA\nZ\tH")));
        
        Set<String> ansForM = library.harderPuzzles("M");
        assertEquals( new HashSet<>(), ansForM, "Before support change output will be entire data as our input contain cycle");

        library.setSupport(15);

        Set<String> afterSupportChange = new HashSet<>();
        afterSupportChange.add("A");
        afterSupportChange.add("C");

        Set<String> ansForZ = library.harderPuzzles("B");
        assertEquals( afterSupportChange, ansForZ, "After changing of support, vertes z is not present in our data as it had lower support than minSupport.");
        
    }

}
