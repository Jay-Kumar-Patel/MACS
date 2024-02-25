import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class levelOfTest {
    @Test
    void nullParameter() {
        Searchable tree = new WorkingSetTree();

        assertEquals( 0, tree.levelOf( null ), "Check null string" );
    }

    @Test
    void emptyStringNotInTree() {
        Searchable tree = new WorkingSetTree();

        assertEquals( 0, tree.levelOf( "" ), "Check empty string" );
    }

    @Test
    void emptyStringInTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "" ), "add empty string to tree" );
        assertEquals( 1, tree.levelOf( "" ), "Check null string" );
    }

    private void checkNodeLevel( String key, int expectedLevel ) {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertEquals( expectedLevel, tree.levelOf( key ), "Check string level" );
    }

    @Test
    void smallest() {
        checkNodeLevel( "a", 4 );
    }

    @Test
    void biggest() {
        checkNodeLevel( "z", 4 );
    }

    @Test
    void intermediate() {
        checkNodeLevel( "b", 3 );
        checkNodeLevel( "j", 3 );
        checkNodeLevel( "p", 3 );
        checkNodeLevel( "u", 3 );
    }

    @Test
    void root() {
        checkNodeLevel( "m", 1 );
    }

    @Test
    void notInTree() {
        checkNodeLevel( "o", 0 );
    }

    @Test
    void afterFind() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "r" ), "find node" );
        assertEquals( 1, tree.levelOf( "r" ), "Check new root level" );
    }

    @Test
    void afterRemove() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "i" ), "remove node" );
        assertEquals( 0, tree.levelOf( "i" ), "Check that the removed item has no level" );
    }

}