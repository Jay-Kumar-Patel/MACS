import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class serializeTest {
    private void checkTail( String[] structure, int startAt ) {
        for( int i = startAt; i < structure.length; i++ ) {
            if (structure[i] != null) {
                fail( "There is a string in the serial array at entry " + i );
            }
        }
    }

    @Test
    void emptyTree() {
        Searchable tree = new WorkingSetTree();

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "Check that we have something returned" );
        assertTrue( flattened.length > 0, "Check the size of what is returned" );
        assertEquals( null, flattened[0], "Check that we start correctly" );

        checkTail( flattened, 1 );
    }

    @Test
    void twoLevels() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );
        assertTrue( tree.add( "noon" ), "add noon" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "Check that we have something returned" );
        assertTrue( flattened.length > 3, "Check the size of what is returned" );
        // Code doesn't say what to do with entry 0 so don't check it.
        assertEquals( "noon", flattened[1], "Check the root" );
        assertEquals( "middle", flattened[2], "Check the left child" );
        assertEquals( "top", flattened[3], "Check the right child" );

        checkTail( flattened, 4 );
    }

    @Test
    void leftChainTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "middle" ), "add middle" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "zebra" ), "add zebra" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "Check that we have something returned" );
        assertTrue( flattened.length > 4, "Check the size of what is returned" );
        // Code doesn't say what to do with entry 0 so don't check it.
        assertEquals( "zebra", flattened[1], "Check the root" );
        assertEquals( "top", flattened[2], "Check the left child" );
        assertEquals( null, flattened[3], "Check the right child" );
        assertEquals( "middle", flattened[4], "Check the left left child" );

        checkTail( flattened, 5 );
    }

    @Test
    void rightChainTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "zebra" ), "add zebra" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "Check that we have something returned" );
        assertTrue( flattened.length > 7, "Check the size of what is returned" );
        // Code doesn't say what to do with entry 0 so don't check it.
        assertEquals( "middle", flattened[1], "Check the root" );
        assertEquals( null, flattened[2], "Check the left child" );
        assertEquals( "top", flattened[3], "Check the right child" );
        assertEquals( null, flattened[4], "Check the left left child" );
        assertEquals( null, flattened[5], "Check the left right child" );
        assertEquals( null, flattened[6], "Check the right left child" );
        assertEquals( "zebra", flattened[7], "Check the right right child" );

        checkTail( flattened, 8 );
    }

}