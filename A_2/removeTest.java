import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class removeTest {
    @Test
    void nullParameter() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.remove( null ), "remove null string" );
    }


    @Test
    void removeFromEmptyTree() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.remove( "marble" ), "remove from empty tree" );
    }

    @Test
    void emptyStringInTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "" ), "add empty string" );
        assertTrue( tree.remove( "" ), "remove empty string" );
    }

    @Test
    void emptyStringNotInTree() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.remove( "" ), "remove empty string" );
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
                if ((structure[i] == null) || (!structure[i].equals( shouldBe[i] )) ){
                    fail( "Mismatch entry at position " + i );
                }
            }
        }
    }

    @Test
    void removeSmallest() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        String[] shouldBe = {null, "m", "h", "s", "b", "j", "p", "u", null, "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "a" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void removeLargest() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        String[] shouldBe = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "z" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
        assertEquals( 0, tree.levelOf( "z" ), "check that z is gone" );
    }

    @Test
    void removeLoneRoot() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "add value" );
        assertTrue( tree.remove( "a" ), "remove root" );
        assertEquals( 0, tree.size(), "removed root from tree" );
    }

    @Test
    void removeRoot() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "m" ), "add m value" );
        assertTrue( tree.add( "a" ), "add a value" );
        assertTrue( tree.add( "z" ), "add z value" );
        assertTrue( tree.remove( "z" ), "remove root" );
        assertEquals( 1, tree.levelOf( "a" ), "check new root" );
        assertEquals( 2, tree.levelOf( "m" ), "check remaining node" );
    }

    @Test
    void removeMiddleLeaf() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        String[] shouldBe = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", null, "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "k" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void removeIntermediateNode() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        String[] shouldBe = {null, "m", "h", "s", "b", "j", "n", "u", "a", "c", "i", "k", null, "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "p" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void notInTree() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};
        String[] shouldBe = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertFalse( tree.remove( "x" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void leftLeftIsMostRecent() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};
        String[] shouldBe = {null, "b",
                null, "h",
                null, null, null, "p",
                null, null, null, null,   null, null, "j", "s",
                null, null, null, null, null, null, null, null,    null, null, null, null, null, null, null, "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "b" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void rightRightIsMostRecent() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};
        String[] shouldBe = {null, "u",
                "s", null,
                "b", null, null, null,
                null, "h", null, null, null, null, null, null,
                null, null, null, "j"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "u" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
        assertEquals( 6, tree.levelOf( "p" ), "check p on own rather than write out full tree" );
    }

    @Test
    void rightLeftIsMostRecent() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null,
                "m",
                "h", "s",
                "b", null, null, null,
                "a", "c" };
        String[] beforeDelete = {null, "h",
                "b", "y",
                "a", "c", "m", null,
                null, null, null, null, null, "s", null, null };
        String[] shouldBeAfterDelete = {null, "m",
                "a", "y",
                null, "b", "s", null,
                null, null, null, "c" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "m" );
        tree.find( "s" );
        tree.add( "y" );
        tree.find( "h" );

        // Make sure the tree looks like we expect it to be
        String[] flatten = tree.serialize();
        checkHead( beforeDelete, flatten, beforeDelete.length-1 );

        // See how the deletion works out now.
        assertTrue( tree.remove( "h" ), "remove node" );
        flatten = tree.serialize();
        checkHead( shouldBeAfterDelete, flatten, shouldBeAfterDelete.length-1 );
    }

    @Test
    void leftRightIsMostRecent() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null,
                "m",
                "h", "s",
                null, null, null, "y",
                null, null, null, null, null, null, "x", "z" };
        String[] beforeDelete = {null, "s",
                "b", "y",
                null, "m", "x", "z",
                null, null, "h" };
        String[] shouldBeAfterDelete = {null, "m",
                "b", "x",
                null, "h", null, "y",
                null, null, null, null,    null, null, null, "z" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "m" );
        tree.find( "h" );
        tree.add( "b" );
        tree.find( "s" );

        // Make sure the tree looks like we expect it to be
        String[] flatten = tree.serialize();
        checkHead( beforeDelete, flatten, beforeDelete.length-1 );

        // See how the deletion works out now.
        assertTrue( tree.remove( "s" ), "remove node" );
        flatten = tree.serialize();
        checkHead( shouldBeAfterDelete, flatten, shouldBeAfterDelete.length-1 );
    }

    @Test
    void allSameAccessTimes() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};
        String[] shouldBe = {null, "b",
                null, "h",
                null, null, null, "p",
                null, null, null, null,   null, null, "j", "s",
                null, null, null, null, null, null, null, null,    null, null, null, null, null, null, null, "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    //@Test
    void leftChildMostRecentWithGrandchild() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b"};
        String[] shouldBe = {null, "b",
                null, "h",
                null, null, null, "s" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "h" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    //@Test
    void leftChildMostRecentNoGrandchild() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s"};
        String[] shouldBe = {null, "h",
                null, "s" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "h" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    //@Test
    void rightChildMostRecentWithGrandchild() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", null, null, null, "z"};
        String[] shouldBe = {null, "z",
                "s", null,
                "h" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "s" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    //@Test
    void rightChildMostRecentNoGrandchild() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s"};
        String[] shouldBe = {null, "s", "h" };

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        tree.find( "s" );
        tree.find( "m" );
        assertTrue( tree.remove( "m" ), "remove node" );
        String[] flatten = tree.serialize();
        checkHead( shouldBe, flatten, shouldBe.length-1 );
    }

    @Test
    void doubleRemove() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "m" ), "remove node" );
        assertFalse( tree.remove( "m" ), "second remove" );
    }

    @Test
    void removeAfterAdd() {
        // same test as leftLeft, but without the "find" operations to update access times
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.add( "x" ), "add node" );
        assertTrue( tree.remove( "x" ), "remove operation" );
    }

}