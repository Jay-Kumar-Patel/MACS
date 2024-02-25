import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class addTest {

    @Test
    void addNull() {
        Searchable tree = new WorkingSetTree();

        assertFalse( tree.add( null ), "Add null string" );
    }


    @Test
    void addToEmptyTree() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
    }

    @Test
    void addSmallValues() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "hair" ), "Add small value" );
        assertTrue( tree.add( "apple" ), "Add smaller value" );
        assertEquals( 1, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "hair" ), "hair spot" );
        assertEquals( 3, tree.levelOf( "marble" ), "marble spot" );
    }

    @Test
    void addBigValues() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "tent" ), "Add big value" );
        assertTrue( tree.add( "zebra" ), "Add bigger value" );
        assertEquals( 1, tree.levelOf( "zebra" ), "Zebra spot" );
        assertEquals( 2, tree.levelOf( "tent" ), "Tent spot" );
        assertEquals( 3, tree.levelOf( "marble" ), "Marble spot" );
    }

    @Test
    void addMiddleValue() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "tent" ), "Add big value" );
        assertTrue( tree.add( "open" ), "Add middle value" );
        assertEquals( 1, tree.levelOf( "open" ), "open spot" );
        // Double rotation to happen
        assertEquals( 2, tree.levelOf( "tent" ), "Tent spot" );
        assertEquals( 2, tree.levelOf( "marble" ), "Marble spot" );
    }

    @Test
    void addRepeatValue() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "tent" ), "Add big value" );
        assertFalse( tree.add( "marble" ), "Add middle value" );
        assertEquals( 1, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 2, tree.levelOf( "tent" ), "tent spot" );
        assertFalse( tree.add( "tent" ), "Add middle value" );
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot again" );
        assertEquals( 1, tree.levelOf( "tent" ), "tent spot again" );
    }

    @Test
    void addLeftChild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Add left child" );
        // left child rotates to the root
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 1, tree.levelOf( "apple" ), "apple spot" );
    }

    @Test
    void addRightChild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "stone" ), "Add right child" );
        // right child rotates to the root
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 1, tree.levelOf( "stone" ), "stone spot" );
    }

    @Test
    void addLeftLeftGrandchild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "stone" ), "Create a left child after rotation" );
        assertTrue( tree.add( "apple" ), "Create a left left grandchild to rotate" );

        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 3, tree.levelOf( "stone" ), "stone spot" );
        assertEquals( 1, tree.levelOf( "apple" ), "apple spot" );
    }

    @Test
    void addLeftRightGrandchild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "stone" ), "Create a left child after rotation" );
        assertTrue( tree.add( "pear" ), "Create a left right grandchild to rotate" );

        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 2, tree.levelOf( "stone" ), "stone spot" );
        assertEquals( 1, tree.levelOf( "pear" ), "pear spot" );
    }

    @Test
    void addRightLeftGrandchild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create a right child after rotation" );
        assertTrue( tree.add( "hair" ), "Create a right left grandchild to rotate" );

        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 2, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 1, tree.levelOf( "hair" ), "hair spot" );
    }

    @Test
    void addRightRightGrandchild() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create a right child after rotation" );
        assertTrue( tree.add( "stone" ), "Create a right right grandchild to rotate" );

        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 3, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 1, tree.levelOf( "stone" ), "stone spot" );
    }

    @Test
    void addTwoLevelRotThenOneLevelRot() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create a right child after rotation" );
        assertTrue( tree.add( "stone" ), "Create a right right grandchild to rotate" );
        assertTrue( tree.add( "aid" ), "Create a linked list down the small side to rotate up" );

        assertEquals( 4, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 3, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 1, tree.levelOf( "aid" ), "aid spot" );
        assertEquals( 2, tree.levelOf( "stone" ), "stone spot" );
    }

    @Test
    void addTwoTwoLevelRot() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create a right child after rotation" );
        assertTrue( tree.add( "stone" ), "Create a right right grandchild to rotate" );
        assertTrue( tree.add( "aid" ), "Create a linked list down the small side to rotate up" );
        assertTrue( tree.add( "beta" ), "Beta at bottom level" );

        assertEquals( 1, tree.levelOf( "beta" ), "beta spot" );
        assertEquals( 3, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 3, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "aid" ), "aid spot" );
        assertEquals( 2, tree.levelOf( "stone" ), "stone spot" );
    }

    @Test
    void addAfterRebuild() {
        Searchable tree = new WorkingSetTree();
        String[] baseTree = {null, "m", "h", "s", null, "j", "p", null };

        assertTrue( tree.rebuild( baseTree ), "Rebuild tree" );
        assertTrue( tree.add( "l" ), "add to built tree" );

        assertEquals( 1, tree.levelOf( "l" ), "l spot" );
        assertEquals( 3, tree.levelOf( "h" ), "h spot" );
        assertEquals( 2, tree.levelOf( "j" ), "j spot" );
        assertEquals( 2, tree.levelOf( "m" ), "m spot" );
        assertEquals( 4, tree.levelOf( "p" ), "p spot" );
        assertEquals( 3, tree.levelOf( "s" ), "s spot" );
    }

    @Test
    void addAfterFind() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create apple" );

        assertEquals( 1, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );

        // find root

        assertTrue( tree.find( "apple" ), "find root" );
        assertTrue( tree.add( "beta" ), "Create beta" );
        assertEquals( 2, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 1, tree.levelOf( "beta" ), "beta spot" );

        // find non-root

        assertTrue( tree.find( "marble" ), "find non-root" );
        assertTrue( tree.add( "carrot" ), "Create carrot" );
        assertEquals( 3, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );
        assertEquals( 2, tree.levelOf( "beta" ), "beta spot" );
        assertEquals( 1, tree.levelOf( "carrot" ), "carrot spot" );

    }

    @Test
    void addAfterDelete() {
        Searchable tree = new WorkingSetTree();

        assertTrue( tree.add( "marble" ), "Add to empty tree" );
        assertTrue( tree.add( "apple" ), "Create apple" );

        assertEquals( 1, tree.levelOf( "apple" ), "apple spot" );
        assertEquals( 2, tree.levelOf( "marble" ), "marble spot" );

        assertTrue( tree.remove( "marble" ), "remove leaf" );
        assertTrue( tree.add( "stone" ), "Create stone" );

        assertEquals( 1, tree.levelOf( "stone" ), "stone spot" );
        assertEquals( 2, tree.levelOf( "apple" ), "apple spot" );
    }

}