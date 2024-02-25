import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class sizeTest {
    @Test
    void emptyTree() {
        Searchable tree = new WorkingSetTree();

        assertEquals( 0, tree.size( ), "empty tree" );
    }

    @Test
    void rootOnly() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "add node" );
        assertEquals( 1, tree.size( ), "empty tree" );
    }

    @Test
    void rightChainTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "zebra" ), "add zebra" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );

        assertEquals( 3, tree.size(), "multi-level tree" );
    }

    @Test
    void afterDelete() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "zebra" ), "add zebra" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );
        assertTrue( tree.remove( "top" ), "remove element" );

        assertEquals( 2, tree.size(), "multi-level tree" );
    }

    @Test
    void afterFind() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "zebra" ), "add zebra" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );

        assertTrue( tree.find("zebra" ), "find value" );
        assertEquals( 3, tree.size(), "multi-level tree" );
    }

    @Test
    void afterSerialize() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "zebra" ), "add zebra" );
        assertTrue( tree.add( "top" ), "add top" );
        assertTrue( tree.add( "middle" ), "add middle" );

        String[] flat = tree.serialize();
        assertEquals( 3, tree.size(), "multi-level tree" );
    }

    @Test
    void afterRebuild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "f", "t", "b", null, null, "z", "a" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertEquals( 6, tree.size(), "size after rebuild" );
    }

}