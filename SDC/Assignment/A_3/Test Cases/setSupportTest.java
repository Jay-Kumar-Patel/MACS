import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupportTest {
    
    @Test
    void positiveValue() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertTrue( library.setSupport(55), "Positive Value of Support" );
    }

    @Test
    void negativeValue() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertFalse( library.setSupport(-55), "Negative Value of Support" );
    }

    @Test
    void zero() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertTrue( library.setSupport(0), "Set support as zero" );
    }

    @Test
    void MaxIntegerValue() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertFalse( library.setSupport(Integer.MAX_VALUE), "Set support as maximum value of integer" );
    }

    @Test
    void MinIntegerValue() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertFalse( library.setSupport(Integer.MIN_VALUE), "Set support as minimum value of integer" );
    }

    @Test
    void setMultipleTimes() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertTrue( library.setSupport(2), "Postitive" );
        assertFalse( library.setSupport(-1), "Negative" );
        assertFalse( library.setSupport(101), "More than 100%" );
        assertTrue( library.setSupport(20), "Resonable Range" );
    }

}
