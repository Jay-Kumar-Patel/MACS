import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;


class EquivalentTest {

    @Test
    void nullPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.equivalentPuzzles(null);
        assertEquals( expectedAns, ans, "Null Puzzle" );
    }

    @Test
    void emptyPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.equivalentPuzzles("");
        assertEquals( expectedAns, ans, "Empty Puzzle" );
    }

    @Test
    void equivalentBeforeInput() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.equivalentPuzzles("");
        assertEquals( expectedAns, ans, "Call equivalent puzzle before input stream" );
    }

    @Test
    void puzzleNotPresentInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB")));
        Set<String> expectedAns = new HashSet<>();
        Set<String> ans = library.equivalentPuzzles("C");
        assertEquals( expectedAns, ans, "Call equivalent puzzle for that puzzle which is not present in our input stream" );
    }

    @Test
    void NoEquivalentPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("B");
        Set<String> ans = library.equivalentPuzzles("B");
        assertEquals( expectedAns, ans, "Their is no puzzle equivalent to given puzzle. So ans is that puzzle itself.");
    }

    @Test
    void support100Percent() {
        PuzzleLibrary library = new PuzzleLibrary( 1 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        library.setSupport(100);
        Set<String> ans = library.equivalentPuzzles("B");
        assertEquals( new HashSet<>(), ans, "Their is no puzzle equivalent to given puzzle as support is 100%");
    }

    @Test
    void callMultipleTimes() {
        PuzzleLibrary library = new PuzzleLibrary( 30 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        library.comparePuzzles(new BufferedReader(new StringReader("A\tD")));
        Set<String> ansA = library.equivalentPuzzles("A");
        assertEquals( new HashSet<>(), ansA, "Their is no puzzle equivalent to given puzzle. So ans is that puzzle itself.");
        Set<String> ansB = library.equivalentPuzzles("B");
        assertEquals( new HashSet<>(), ansB, "Their is no puzzle equivalent to given puzzle. So ans is that puzzle itself.");
        Set<String> ansC = library.equivalentPuzzles("C");
        assertEquals( new HashSet<>(), ansC, "Their is no puzzle equivalent to given puzzle. So ans is that puzzle itself.");
    }

    @Test
    void oneCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("Q");
        expectedAns.add("F");

        Set<String> ans = library.equivalentPuzzles("B");
        assertEquals( expectedAns, ans, "Their is only one cycle in our input.");
    }


    @Test
    void multipleCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("Q");
        expectedAns.add("F");
        expectedAns.add("R");
        Set<String> ans = library.equivalentPuzzles("B");
        assertEquals( expectedAns, ans, "Their are 3 cycles in our input.");
    }

    @Test
    void entireInputIsCycle() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("B\tC\nC\tR\nC\tQ\nB\tD\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nY\tB")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("Q");
        expectedAns.add("B");
        expectedAns.add("R");
        expectedAns.add("C");
        expectedAns.add("D");
        expectedAns.add("F");
        expectedAns.add("Y");
        Set<String> ans = library.equivalentPuzzles("B");
        assertEquals( expectedAns, ans, "Our entire input form a cycle.");
    }

    @Test
    void nestedCycle1() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("Z\tH\nH\tC\nC\tA\nA\tZ\nA\tB\nB\tQ\nQ\tL\nM\tC\nB\tC\nL\tM")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("Q");
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("H");
        expectedAns.add("Z");
        expectedAns.add("L");
        expectedAns.add("M");

        Set<String> ansForB = library.equivalentPuzzles("B");
        assertEquals( expectedAns, ansForB, "Our input had cycles inside a cycle.");
        Set<String> ansForC = library.equivalentPuzzles("C");
        assertEquals( expectedAns, ansForC, "Our input had cycles inside a cycle.");
        Set<String> ansForL = library.equivalentPuzzles("L");
        assertEquals( expectedAns, ansForL, "Our input had cycles inside a cycle.");
    }

    @Test
    void nestedCycle2() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA\nC\tD\nD\tE\nE\tC\nE\tF\nF\tG\nG\tE")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("D");
        expectedAns.add("E");
        expectedAns.add("F");
        expectedAns.add("G");

        Set<String> ansForC = library.equivalentPuzzles("C");
        assertEquals( expectedAns, ansForC, "Our input had cycles inside a cycle.");

        Set<String> ansForf = library.equivalentPuzzles("F");
        assertEquals( expectedAns, ansForf, "Our input had cycles inside a cycle.");
    }


    @Test
    void changeSupport() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("Z\tH\nH\tC\nC\tA\nA\tZ\nA\tB\nB\tQ\nQ\tL\nM\tC\nB\tC\nL\tM\nA\tB\nB\tC\nC\tA")));
        Set<String> beforeSupportChange = new HashSet<>();
        beforeSupportChange.add("A");
        beforeSupportChange.add("Q");
        beforeSupportChange.add("B");
        beforeSupportChange.add("C");
        beforeSupportChange.add("H");
        beforeSupportChange.add("Z");
        beforeSupportChange.add("L");
        beforeSupportChange.add("M");
        
        Set<String> ansForB = library.equivalentPuzzles("B");
        assertEquals( beforeSupportChange, ansForB, "Before support change output will be entire data as our input contain cycle");

        library.setSupport(15);

        Set<String> afterSupportChange = new HashSet<>();
        afterSupportChange.add("A");
        afterSupportChange.add("B");
        afterSupportChange.add("C");

        Set<String> ansForZ = library.equivalentPuzzles("Z");
        assertEquals( new HashSet<>(), ansForZ, "After changing of support, vertes z is not present in our data as it had lower support than minSupport.");

        Set<String> ansForC = library.equivalentPuzzles("C");
        assertEquals( afterSupportChange, ansForC, "After changing of support, only A,B and C vertex are present. Moreover it contain cycle so entire input will be our ans");
        
    }


}