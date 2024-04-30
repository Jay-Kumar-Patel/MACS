public class TreeFunctions {

    /**
     * This method used to calculate the height of binary tree.
     * @param node : Root of the tree.
     * @return : height of the tree in interger.
     */
    public int heightOfBinarySearchTree(Node node){

        if(node == null){
            return 0;
        }

        //Get the left subtree height.
        int leftHeight = heightOfBinarySearchTree(node.left) + 1 ;

        //Get the right subtree height.
        int rightHeight = heightOfBinarySearchTree(node.right) + 1;

        //Return the maximum from either height of left or right subtree.
        return Math.max(leftHeight, rightHeight);
    }


    /**
     * This method is used to get the level of given node.
     * @param node : Root of the tree.
     * @param key : Value of the node for which we want a level.
     * @param level : Initialize the level as 1.
     * @return : If node present return level of the node, else retun 0.
     */
    public int heightOfBinarySearchTree(Node node, String key, int level){

        if(node == null){
            return 0;
        }

        if(node.key.equalsIgnoreCase(key)){
            return level;
        }

        int leftHeight = heightOfBinarySearchTree(node.left, key, level+1);
        if(leftHeight != 0){
            return leftHeight;
        }

        int rightHeight = heightOfBinarySearchTree(node.right, key, level+1);

        return rightHeight;
    }


    /**
     * This method is used during serialize() function.
     * @param node : Current node, initially it was a root node.
     * @param index : Maintain the index in ans array to add value in that array. 
     * @param ans : We store the values in this array.
     */
    public void preOrderTraversal(Node node, int index, String[] ans){

        if(node == null){
            return;
        }

        ans[index] = node.key;

        //left child is at 2x index of ans array; 
        preOrderTraversal(node.left, 2 * index, ans);

        //left child is at 2x+1 index of ans array;
        preOrderTraversal(node.right, 2 * index+1, ans);
    }


    /**
     * This method is used to get the count of the element present in the tree.
     * @param node : Current node, initially it was a root node.
     * @param ans : Maintain the count of the elements.
     */
    public void preOrderTraversal(Node node, int[] ans){

        if(node == null){
            return;
        }

        ans[0]++;
        preOrderTraversal(node.left, ans);
        preOrderTraversal(node.right, ans);
    }


    /**
     * This method is used to check the given tree holds the properties of binary serach tree or not.
     * @param root : Root of the tree for which we want to check.
     * @return : If tree is valid return true, else return false.
     */
    public boolean checkBinaryTree(Node root){
        
        if(root == null){
            return true;
        }

        if(root.left != null){
            if(root.key.compareTo(root.left.key) < 0){
                return false;
            }
        }

        if(root.right != null){
            if(root.key.compareTo(root.right.key) > 0){
                return false;
            }
        }

        checkBinaryTree(root.left);
        checkBinaryTree(root.right);
        
        return true;
    }
    
}
