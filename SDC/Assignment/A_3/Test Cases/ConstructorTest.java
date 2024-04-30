import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorTest {

    @Test
    void OkConstructor() {
        PuzzleLibrary library = new PuzzleLibrary( 10 );
        assertNotNull(library);
    }

    @Test
    void PositiveConstructor() {
        PuzzleLibrary library = new PuzzleLibrary( 28 );
        assertNotNull(library);
    }

    @Test
    void NegativeConstructor() {
        PuzzleLibrary library = null;
        try{
            library = new PuzzleLibrary( -16 );
            fail("Not valid Support level");
        }
        catch(IllegalArgumentException e){
            assertNull(library);
        }
    }

    @Test
    void ZeroConstructor() {
        PuzzleLibrary library = new PuzzleLibrary( 0 );
        assertNotNull(library);
    }

    @Test
    void MaxConstructor() {
        PuzzleLibrary library = null;
        try{
            library = new PuzzleLibrary( Integer.MAX_VALUE );
            fail("Not valid Support level");
        }
        catch(IllegalArgumentException e){
            assertNull(library);
        }
    }

    @Test
    void MinConstructor() {
        PuzzleLibrary library = null;
        try{
            library = new PuzzleLibrary( Integer.MIN_VALUE );
            fail("Not valid Support level");
        }
        catch(IllegalArgumentException e){
            assertNull(library);
        }
    }
    
}
