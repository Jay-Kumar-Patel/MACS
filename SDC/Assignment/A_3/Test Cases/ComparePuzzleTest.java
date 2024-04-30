import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.StringReader;

class ComparePuzzleTest {

    @Test
    void nullStream() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles( null ), "null input stream" );
    }

    @Test
    void emptyStream() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles( new BufferedReader( new StringReader( "" )) ), "empty input stream" );
    }


    @Test
    void singleComparision() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertTrue( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB" )) ), "Single Line in input" );
    }

    @Test
    void ThreePuzzleForComparsion() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB\tC" )) ), "Three puzzles in one line" );
    }

    @Test
    void InappropriateSpaces_1() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles( new BufferedReader( new StringReader( "A\t\t\tB" )) ), "Three tab between two puzzles in one line" );
    }

    @Test
    void InappropriateSpaces_2() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles(  new BufferedReader( new StringReader( " \tB" )) ), "First Puzzle as empty string and Second Puzzle contain value." );
    }

    @Test
    void BlankLines() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertTrue( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB\n\n\n\n\n\nC\tD" )) ), "Some blank lines are present in our input" );
    }

    @Test
    void longInput() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertTrue( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC\nA\tB\nB\tC\nC\tR\nC\tQ\nD\tB\nQ\tF\nF\tB\nP\tZ\nZ\tT\nE\tS\nY\tD\nT\tP\nD\tY\nR\tC" )) ), "Very long input" );
    }

    @Test
    void BothPuzzleSame() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertFalse( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB\nD\tD" )) ), "Puzzle are compare with itself" );
    }

    @Test
    void callTwice() {
        PuzzleLibrary library = new PuzzleLibrary(8);

        assertTrue( library.comparePuzzles( new BufferedReader( new StringReader( "A\tB" )) ), "First Call" );
        assertTrue( library.comparePuzzles( new BufferedReader( new StringReader( "C\tD" )) ), "Second Call" );
    }


}