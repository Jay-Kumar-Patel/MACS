import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

class HardestTest {
    
    @Test
    void emptyData() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertEquals( new HashSet<>(), library.hardestPuzzles(), "Before Data, call hardest puzzle." );
    }

    @Test
    void NoValidData() {
        PuzzleLibrary library = new PuzzleLibrary( 90 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        assertEquals( new HashSet<>(), library.hardestPuzzles(), "Have input but it had lower support than minSupport" );
    }

    @Test
    void NoHardestPuzzle() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("B");
        expectedAns.add("C");
        assertEquals( expectedAns, library.hardestPuzzles(), "Their is no hardest puzzle as entire input form a cycle.");
    }

    @Test
    void callMultipleTimes() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tD")));
        Set<String> ansA = new HashSet<>();
        ansA.add("A");
        assertEquals( ansA, library.hardestPuzzles(), "A will be the hardest puzzle");
        library.comparePuzzles(new BufferedReader(new StringReader("E\tA")));
        Set<String> ansB = new HashSet<>();
        ansB.add("E");
        assertEquals( ansB, library.hardestPuzzles(), "After adding E to A than in entire graph E will be the hardest puzzle.");
    }

    @Test
    void changeSupport() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("Z\tH\nH\tC\nC\tA\nA\tB\nB\tQ\nQ\tL\nM\tC\nL\tM\nA\tB\nC\tA")));
        
        Set<String> ans = new HashSet<>();
        ans.add("Z");
        assertEquals( ans, library.hardestPuzzles(), "Before support change there is only cycle in input");

        library.setSupport(15);

        Set<String> afterSupportChange = new HashSet<>();
        afterSupportChange.add("C");

        assertEquals( afterSupportChange, library.hardestPuzzles(), "After changing of support, only A, B and C were present in the graph.");
    }

    @Test
    void oneCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("P");
        expectedAns.add("A");
        expectedAns.add("T");
        expectedAns.add("E");
        expectedAns.add("Y");
        expectedAns.add("Z");

        assertEquals( expectedAns, library.hardestPuzzles(), "Their is only one cycle in our input.");
    }


    @Test
    void multipleCycleInInput() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("A");
        expectedAns.add("Y");
        expectedAns.add("D");
        expectedAns.add("P");
        expectedAns.add("Z");
        expectedAns.add("T");
        expectedAns.add("E");
        assertEquals( expectedAns, library.hardestPuzzles(), "Their are 3 cycles in our input.");
    }

    @Test
    void entireInputIsCycle() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("B\tC\nC\tR\nC\tQ\nB\tD\nQ\tF\nF\tB\nP\tZ\nZ\tT\nY\tD\nT\tP\nD\tY\nR\tC\nY\tB")));
        Set<String> expectedAns = new HashSet<>();
        expectedAns.add("Y");
        expectedAns.add("D");
        expectedAns.add("B");
        expectedAns.add("C");
        expectedAns.add("R");
        expectedAns.add("Q");
        expectedAns.add("F");
        expectedAns.add("P");
        expectedAns.add("Z");
        expectedAns.add("T");
        assertEquals( expectedAns, library.hardestPuzzles(), "Our entire input form a cycle.");
    }
}
