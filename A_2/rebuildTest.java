import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class rebuildTest {
    @Test
    void nullParameter() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.rebuild( null ), "rebuild null tree" );
    }

    private void checkTail( String[] structure, int startAt ) {
        for( int i = startAt; i < structure.length; i++ ) {
            if (structure[i] != null) {
                fail( "There is a string in the serial array at entry " + i );
            }
        }
    }

    private void checkHead( String[] shouldBe, String[] structure, int endAt ) {
        int end = endAt;
        if (structure.length-1 < end) {
            end = structure.length-1;
        }

        for( int i = 1; i <= end; i++ ) {
            if (shouldBe[i] == null) {
                if (structure[i] != null) {
                    fail( "Null expected in structure at position " + i);
                }
            } else {
                if ((structure[1] == null) || (!structure[i].equals( shouldBe[i] )) ){
                    fail( "Mismatch entry at position " + i );
                }
            }
        }
    }

    @Test
    void buildEmptyTreeProper() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        String[] structure = tree.serialize();
        assertNotNull( structure, "empty tree returned" );
        assertTrue( structure.length > 0, "serial tree wrong size" );

        checkTail( structure, 1 );
    }

    @Test
    void buildEmptyTreeDirtyStart() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {"ignore me"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        String[] structure = tree.serialize();
        assertNotNull( structure, "empty tree returned" );
        assertTrue( structure.length > 0, "serial tree wrong size" );

        checkTail( structure, 1 );
    }

    @Test
    void buildJustRoot() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "start"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        String[] structure = tree.serialize();
        assertNotNull( structure, "empty tree returned" );
        assertTrue( structure.length > 1, "serial tree wrong size" );

        checkHead( treeStructure, structure, 1 );
        checkTail( structure, 2 );
    }

    private void buildGivenTree( String[] treeStructure ) {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        String[] structure = tree.serialize();
        assertNotNull( structure, "empty tree returned" );
        assertTrue( structure.length >= treeStructure.length, "serial tree wrong size" );

        checkHead( treeStructure, structure, treeStructure.length-1 );
        checkTail( structure, treeStructure.length );
    }

    @Test
    void buildBalancedTree() {
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        buildGivenTree( treeStructure );
    }

    @Test
    void leftLinkedList() {
        String[] treeStructure = {null, "m", "h", null, "a"};
        buildGivenTree( treeStructure );
    }

    @Test
    void rightLinkedList() {
        String[] treeStructure = {null, "m", null, "s", null, null, null, "u"};
        buildGivenTree( treeStructure );
    }

    @Test
    void childHasNoParent() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", null, "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertFalse( tree.rebuild( treeStructure ), "rebuild tree" );
    }

    @Test
    void badOrderAtAChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "s", "h"};

        assertFalse( tree.rebuild( treeStructure ), "rebuild tree" );
    }

    @Test
    void badOrderAtGrandchild1() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "x", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertFalse( tree.rebuild( treeStructure ), "rebuild tree" );
    }

    @Test
    void badOrderAtGrandchild2() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "d", "z"};

        assertFalse( tree.rebuild( treeStructure ), "rebuild tree" );
    }

    @Test
    void allNulls() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );

        String[] structure = tree.serialize();
        assertNotNull( structure, "serialized return" );
        assertTrue( structure.length > 0, "returned size" );
        checkTail( structure, 1 );
    }

    @Test
    void buildOverATree() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "start"};

        assertTrue( tree.add( "x" ), "seed tree" );
        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );

        String[] structure = tree.serialize();
        assertNotNull( structure, "empty tree returned" );
        assertTrue( structure.length > 1, "serial tree wrong size" );

        checkHead( treeStructure, structure, 1 );
        checkTail( structure, 2 );
    }

}