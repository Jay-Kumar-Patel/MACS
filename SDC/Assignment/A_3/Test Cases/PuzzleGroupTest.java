import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

class PuzzleGroupTest {

    @Test
    void emptyData() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertEquals( new HashSet<>(), library.puzzleGroup(), "Before Data, call puzzle group." );
    }

    @Test
    void NoValidData() {
        PuzzleLibrary library = new PuzzleLibrary( 90 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        assertEquals( new HashSet<>(), library.puzzleGroup(), "Have input but it had lower support than minSupport" );
    }

    @Test
    void oneGroup() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA")));
        Set<Set<String>> expectedAns = new HashSet<>();

        Set<String> firstGroup = new HashSet<>();
        firstGroup.add("A");
        firstGroup.add("B");
        firstGroup.add("C");

        expectedAns.add(firstGroup);
        
        assertEquals( expectedAns, library.puzzleGroup(), "Only one group are present" );
    }

    @Test
    void twoGroup() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tA\nE\tF\nG\tF\nA\tB")));
        Set<Set<String>> expectedAns = new HashSet<>();

        Set<String> firstGroup = new HashSet<>();
        firstGroup.add("A");
        firstGroup.add("B");
        firstGroup.add("C");

        Set<String> SecondGroup = new HashSet<>();
        SecondGroup.add("E");
        SecondGroup.add("F");
        SecondGroup.add("G");

        expectedAns.add(firstGroup);
        expectedAns.add(SecondGroup);
        
        assertEquals( expectedAns, library.puzzleGroup(), "Two groups are present" );
    }

    @Test
    void multipleGroups() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nL\tU\nW\tN\nI\tG")));
        Set<Set<String>> expectedAns = new HashSet<>();

        Set<String> firstGroup = new HashSet<>();
        firstGroup.add("A");
        firstGroup.add("B");
        firstGroup.add("C");
        firstGroup.add("R");
        firstGroup.add("Q");
        firstGroup.add("D");
        firstGroup.add("F");
        firstGroup.add("Y");

        Set<String> SecondGroup = new HashSet<>();
        SecondGroup.add("P");
        SecondGroup.add("Z");
        SecondGroup.add("T");

        Set<String> thirdGroup = new HashSet<>();
        thirdGroup.add("E");
        thirdGroup.add("S");

        Set<String> fourthGroup = new HashSet<>();
        fourthGroup.add("L");
        fourthGroup.add("U");

        Set<String> fifthGroup = new HashSet<>();
        fifthGroup.add("W");
        fifthGroup.add("N");

        Set<String> sixthGroup = new HashSet<>();
        sixthGroup.add("I");
        sixthGroup.add("G");

        expectedAns.add(firstGroup);
        expectedAns.add(SecondGroup);
        expectedAns.add(thirdGroup);
        expectedAns.add(fourthGroup);
        expectedAns.add(fifthGroup);
        expectedAns.add(sixthGroup);
        
        assertEquals( expectedAns, library.puzzleGroup(), "Multiple groups are present" );
    }


    @Test
    void changeSupport() {
        PuzzleLibrary library = new PuzzleLibrary( 3 );

        library.comparePuzzles(new BufferedReader(new StringReader("W\tN\nW\tN\nW\tN\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nL\tU\nW\tN\nI\tG\nW\tN\nW\tN")));

        library.setSupport(5);

        Set<Set<String>> expectedAns = new HashSet<>();

        Set<String> group = new HashSet<>();
        group.add("W");
        group.add("N");

        expectedAns.add(group);
        
        assertEquals( expectedAns, library.puzzleGroup(), "After changing support only one group is left" );
    }


    @Test
    void callTwice() {
        PuzzleLibrary library = new PuzzleLibrary( 5 );
        library.comparePuzzles(new BufferedReader(new StringReader("E\tF\nE\tF\nE\tF\nE\tF\nA\tB\nB\tC\nC\tA\nE\tF\nC\tB\nE\tF\nE\tF")));
        Set<Set<String>> firstCallExpectedAns = new HashSet<>();

        Set<String> firstGroup = new HashSet<>();
        firstGroup.add("A");
        firstGroup.add("B");
        firstGroup.add("C");


        Set<String> secondGroup = new HashSet<>();
        secondGroup.add("E");
        secondGroup.add("F");

        firstCallExpectedAns.add(firstGroup);
        firstCallExpectedAns.add(secondGroup);

        assertEquals( firstCallExpectedAns, library.puzzleGroup(), "First Call" );

        library.setSupport(10);

        Set<Set<String>> secondCallExpectedAns = new HashSet<>();

        Set<String> secondCallFirstGroup = new HashSet<>();
        secondCallFirstGroup.add("E");
        secondCallFirstGroup.add("F");

        secondCallExpectedAns.add(secondCallFirstGroup);
        
        assertEquals( secondCallExpectedAns, library.puzzleGroup(), "Second Call" );
    }

    
}
