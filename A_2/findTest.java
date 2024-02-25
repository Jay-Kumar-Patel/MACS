import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class findTest {
    @Test
    void findNull() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "Add a string" );
        assertFalse( tree.find( null ), "find null string" );
    }

    @Test
    void emptyTree() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.find( "a" ), "find in an empty tree" );
    }

    @Test
    void emptyStringInTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "Add a string" );
        assertTrue( tree.add( "" ), "Add empty string" );
        assertTrue( tree.find( "" ), "find an empty string" );
    }

    @Test
    void emptyStringNotInTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "Add null string" );
        assertFalse( tree.find( "" ), "find an empty string" );
    }

    @Test
    void findSmallest() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "a" ), "find smallest" );
        assertEquals( 1, tree.levelOf( "a" ), "level check" );
    }

    @Test
    void findBiggest() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "z" ), "find smallest" );
        assertEquals( 1, tree.levelOf( "z" ), "level check" );
    }

    @Test
    void onlyRootInTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "a" ), "Add null string" );
        assertFalse( tree.find( "b" ), "find something not in tree" );
        assertTrue( tree.find( "a" ), "find root" );
    }

    @Test
    void findLeaf() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "n" ), "find a leaf" );
        assertEquals( 1, tree.levelOf( "n" ), "level check" );
    }

    @Test
    void findMiddleValue() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u", "a", "c", "i", "k", "n", "r", "t", "z"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "p" ), "find middle node" );
        assertEquals( 1, tree.levelOf( "p" ), "level check" );
    }

    private void checkTree( String[] shouldBe, String[] flattened ) {
        for( int i = 1; i < shouldBe.length; i++ ) {
            if (shouldBe[i] == null) {
                if (flattened[i] != null) {
                    fail( "expected null at position "  + i );
                }
            } else {
                if (!shouldBe[i].equals( flattened[i] )) {
                    fail ( "bad string at position " + i );
                }

            }
        }
    }

    @Test
    void findRootLeftChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "h" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "h", "b", "m", null, null, "j", "s", null, null, null, null, null, null, "p", "u"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findRootRightChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "s" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "s", "m", "u", "h", "p", null, null, "b", "j"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findRootLeftLeftChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", null, "b", "j"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "b" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "b", null, "h", null, null, null, "m", null, null, null, null, null, null, "j"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findRootLeftRightChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", null, "b", "j"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "j" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "j", "h", "m", "b"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findRootRightRightChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", null, "s", null, null, "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "u" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "u", "s", null, "m", null, null, null, null, "p"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findRootRightLeftChild() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", null, "s", null, null, "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "p" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "p", "m", "s", null, null, null, "u"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findTwoLevelRotThenOneLevelRot() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", null, "e", null, null, null, "b"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "b" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "b", null, "m", null, null, "e", null, null, null, null, null, null, "h"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void findTwoTwoLevelRot() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", null, "e", null, null, null, "b"};

        assertTrue( tree.add( "e" ), "add e" );
        assertTrue( tree.add( "d" ), "add d" );
        assertTrue( tree.add( "c" ), "add c" );
        assertTrue( tree.add( "b" ), "add b" );
        assertTrue( tree.add( "a" ), "add a" );
        assertTrue( tree.find( "e" ), "find node" );

        String[] flattened = tree.serialize();
        assertNotNull( flattened, "flat tree" );
        String[] shouldBe = {null, "e", "b", null, "a", "d", null, null, null, null, "c"};
        checkTree( shouldBe, flattened );
    }

    @Test
    void twoFindInARow() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.find( "h" ), "find node" );
        assertTrue( tree.find( "h" ), "find node" );
    }

    @Test
    void findAfterDelete() {
        Searchable tree = new WorkingSetTree();
        String[] treeStructure = {null, "m", "h", "s", "b", "j", "p", "u"};

        assertTrue( tree.rebuild( treeStructure ), "rebuild tree" );
        assertTrue( tree.remove( "s" ), "delete node" );
        assertFalse( tree.find( "s" ), "find node" );
    }

}