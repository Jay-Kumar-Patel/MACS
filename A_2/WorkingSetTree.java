import java.util.Arrays;

public class WorkingSetTree implements Searchable{

    //Root of our Tree.
    Node root;

    //Treefunctions class conatins methods like calulate height of tree, preOrder Traversal, and check tree is holding properties of BST or not after using rebuilt function.
    TreeFunctions treeFunctions;

    //This root is temporary used during rebuit function. If tree is valid than this temporary root copied to original root.
    Node rootForRebuilt;

    private String[] access;
    private int accessIndex = 0;
    private int accessSize = 3;


    //Constructor which initialzes the root, temporary root and our TreeFunctions class.
    public WorkingSetTree(){
        root = null;
        treeFunctions = new TreeFunctions();
        rootForRebuilt = null;
        access = new String[accessSize];
    }


    /**
     * Main Method which first create the object of this class using which we acces the other functions like add, find, remove, size, levelOf, serialize, and rebuild.
     * @param args
     */
    public static void main(String[] args) {

        WorkingSetTree tree = new WorkingSetTree();

        //Insert element into tree (null value is not acceptable)
        tree.add("Orange");
        tree.add("Salsa");
        tree.add("Banana");
        tree.add(null);
        tree.add("Mango");
        tree.add("Apple");
        tree.add("XXX");
        tree.add("Straw");
        tree.add(null);


        //Count the number of nodes present in tree
        tree.size();


        //Search a node in tree
        tree.find(null);
        tree.find("Mango");
        tree.find("");


        //Get tree structure into array format (In ans1 root is at 1st index)
        String[] ans1 = tree.serialize();
        

        //Get the level of node (Root is at level 1)
        tree.levelOf(null);
        tree.levelOf("XXX");
        tree.levelOf("Fruit");


        //Delete the node from a tree
        tree.remove(null);
        tree.remove("");
        tree.remove("Salsa");


        //Get tree structure into array format after removal of some values from the tree.
        String[] ans2 = tree.serialize();


        //Built tree from an array (Start building the tree from index 1)
        tree.rebuild(new String[]{null, "D", "B", "E", "A"});


        //After rebuild get the count of nodes present in tree
        tree.size();
    }

    /**
     * This method use to add the element in tree. It accepts the value as parameter that you want to add.
     * It will return true if value is successfully added to our tree, else return false 
     */
    @Override
    public boolean add(String key) {

        if (key == null) {
            return false;
        }
        
        Node newNode = new Node(key.trim());

        if(accessIndex == (int)accessSize/2){
            increaseSizeOfArray();
        }

        //It tree is empty set element as root node.
        if(root == null){
            root = newNode;
            access[accessIndex] = newNode.key;
            accessIndex++;
            return true;
        }

        
        if(find(newNode.key)){
            return false;
        }

        Node currNode = root;
        Node parentNode;


        while(true){

            parentNode = currNode;


            //If element that we want to add is smaller than currNode value than we further move to left side of the tree.
            if(currNode.key.compareToIgnoreCase(newNode.key) > 0){
                currNode = currNode.left;
                if(currNode == null){
                    parentNode.left = newNode;
                    newNode.parent = parentNode;

                    access[accessIndex] = key;
                    accessIndex++;

                    //After adding our node we have to move that node as a root of the tree.
                    SplayTree(newNode);
                    return true;
                }
            }
            //If element that we want to add is larger than currNode value than we further move to right side of the tree.
            else{
                currNode = currNode.right;
                if(currNode == null){
                    parentNode.right = newNode;
                    newNode.parent = parentNode;

                    access[accessIndex] = key;
                    accessIndex++;

                     //After adding our node we have to move that node as a root of the tree.
                    SplayTree(newNode);
                    return true;
                }
            }
        }
    }



    /**
     * This method is use to check that particular value is present in our tree or not. It accepts value that we want to find as a parameter.
     * It will return true if value is present in our tree or else return false;
     */
    @Override
    public boolean find(String key) {

        if(root == null || key == null){
            return false;
        }

        if(accessIndex == (int)accessSize/2){
            increaseSizeOfArray();
        }

        Node temp = root;

        if(searchNode(temp, key.trim())){
            access[accessIndex] = key.trim();
            accessIndex++;
            return true;
        }

        return false;
    }


     /**
     * This function is used to delete the node from tree.
     * It takes input as the key of the node which we want to delete.
     * It will return true if node was successfully deleted, else return false.
     */
    @Override
    public boolean remove(String key) {
        
        if(root == null || key == null){
            return false;
        }

        Node temp = root;

        return findDeletionNode(temp, key.trim());
    }


    
    /**
     * This method is used to get the size of our tree. Size means the count of elements present in our tree, excluding null values.
     * It will return true if we get our size of tree, else return false.
     */
    @Override
    public int size() {

        if (root == null) {
            return 0;
        }

        //If tree only contain single value.
        if(root.left == null && root.right == null && root.parent == null){
            return 1;
        }

        //Here we use int array instead of int varaible because if we pass variable it pass by value but, here we want that same varible to upadte in each iteration during recursion that's why we choose array so it will pass by reference.
        int treeSize[] = new int[1];
        //This function just do the preOrder traversal of our tree and during traversal calculate the count of elements.
        Node temp = root;
        treeFunctions.preOrderTraversal(temp, treeSize);
        return treeSize[0];
    }



    /**
     * This method is used to get the level of given value. Root is at level 1, their child is at level 2, and so on.
     * If child is present, it will return the level of that node, else return false.
     */
    @Override
    public int levelOf(String key) {

        if (root == null || key == null) {
            return 0;
        }

        Node temp = root;
        //This method is using internally same calculation as we did in calulate the height of binary tree. We just maintain one extra variable to get level of the node. 
        return treeFunctions.heightOfBinarySearchTree(temp, key.trim(), 1);
    }


    /**
     * This method represent tree into array format. At 0th position it is null, 1st position is root. The children of node which is at x position, its left child is at 2x and right child is at 2x+1. 
     * Return that array after all sucessfully operation. 
     */
    @Override
    public String[] serialize() {

        if(root == null){
            return new String[]{null};
        }

        Node temp = root;
        //Firstly, we calculate the height of tree.
        int heightOfTree = treeFunctions.heightOfBinarySearchTree(temp);

        //After we get the height we make a array of size 2^height of tree.
        String[] ans = new String[(int)Math.pow(2, heightOfTree)];

        temp = root;
        //After that we just travel through the tree and store that element into array.
        treeFunctions.preOrderTraversal(temp, 1, ans);

        return ans;
    }


    /**
     * This method is opposite of serialize(). Here we get the array as parameter and we have to build the tree using that array.
     * Return true if tree is successfully made, else return false. 
     */
    @Override
    public boolean rebuild(String[] keys) {

        if(keys == null || keys[1] == null){
            return false;
        }

        rootForRebuilt = createBinaryTree(1, keys, null);

        //After building that tree we have to check that it holds the properties of binary search tree or not.
        if(treeFunctions.checkBinaryTree(rootForRebuilt)){

            if(keys.length >= (int)accessSize/2){
                increaseSizeOfArray();
            }

            root = rootForRebuilt;
            access = new String[]{};
            accessIndex = 0;
            rootForRebuilt = null;
            return true;
        }
        
        rootForRebuilt = null;
        return false;
    }
    

    /**
     * This function travel through every node and compare to our value which we want to find. If it matches than it splay the tree (means move that searched node as a root of our tree).
     * @param node : Initailly we pass root of our tree and than we upadte it with the currNode on which we currently stand.
     * @param key : Value that we want to serach
     * @return : It value present and sucessfully made that node as root return true, else return false;
     */
    private boolean searchNode(Node node, String key){

        if(root == null || node == null || key == null){
            return false;
        }

        if(node.key.equalsIgnoreCase(key)){
            SplayTree(node);
            return true;
        }

        //Recursively search to left subtree.
        boolean leftAns = searchNode(node.left, key);

        //Recursively search to right subtree.
        boolean rightAns = searchNode(node.right, key);


        //If present on either left or right return true.
        return (leftAns || rightAns);
    }


    /**
     * This function is used to move the node which we want to delete, to the leaf node and than delete it from this tree.
     * @param node : Root Node
     * @param key : Value of that node
     * @return : True if we find and delete that node successfully, else return false.
     */
    private boolean findDeletionNode(Node node, String key){

        if(root == null || node == null || key == null){
            return false;
        }

        if(node.key.equalsIgnoreCase(key)){
            while(true){

                //Leaf Node
                if(node.left == null && node.right == null){
                    if(node == node.parent.left){
                        node.parent.left = null;
                        return true;
                    }
                    else{
                        node.parent.right = null;
                        return true;
                    }
                }
                else{

                    //Node having one child

                    //Child on left side
                    if(node.left != null && node.right == null){

                        //child having no further child
                        if(node.left.left == null && node.left.right == null){
                            if(node == node.parent.left){
                                node.parent.left = node.left;
                                node.left.parent = node.parent;
                                node.left = null;
                                return true;
                            }
                            else{
                                node.parent.right = node.left;
                                node.left.parent = node.parent;
                                node.left = null;
                                return true;
                            }
                            
                        }

                        //If child have further childrens
                        else{

                            //Child have one child on left side (right-right rotation)
                            if(node.left.left != null && node.left.right ==  null){
                                deletionSplayTree(node.left.left);
                            }

                            //Child have one child on right side (left-right rotation)
                            else if(node.left.left == null && node.left.right !=  null){
                                deletionSplayTree(node.left.right);
                            }

                            //Child having two childrens
                            else{

                                //Check who is recently accessed
                                boolean ans = findLastAccessElement(node.left.left.key, node.left.right.key);

                                //If left child is recently accessed than true, else false
                                if(ans){

                                    //right-right rotation
                                    deletionSplayTree(node.left.left);
                                }
                                else{

                                    //left-right rotation
                                    deletionSplayTree(node.left.right);
                                }
                            }
                        }
                    }

                    //Child on right side
                    else if(node.left == null && node.right != null){

                        //child having no further child
                        if(node.right.left == null && node.right.right == null){
                            if(node == node.parent.left){
                                node.parent.left = node.right;
                                node.right.parent = node.parent;
                                node.right = null;
                                return true;
                            }
                            else{
                                node.parent.right = node.right;
                                node.right.parent = node.parent;
                                node.right = null;
                                return true;
                            }
                            
                        }

                        //If child have further childrens
                        else{

                            //Child have no further childrens
                            if(node.right.left != null && node.right.right ==  null){
                                deletionSplayTree(node.right.left);
                            }

                            //Child have one more child on right side (left-left rotations)
                            else if(node.right.left == null && node.right.right !=  null){
                                deletionSplayTree(node.right.right);
                            }

                            //Child have further two childrens
                            else{

                                //Check who is recently accessed
                                boolean ans = findLastAccessElement(node.right.left.key, node.right.right.key);

                                //If left child is recently accessed than true, else false
                                if(ans){

                                    //right-left rotation
                                    deletionSplayTree(node.right.left);
                                }
                                else{

                                    //left-left rotation
                                    deletionSplayTree(node.right.right);
                                }
                            }
                        }
                    }

                    //Node having two childrens
                    else{

                        //Check who is recently accessed
                        boolean ans = findLastAccessElement(node.left.key, node.right.key);
    
                        //If left child is recently accessed than true, else false
                        if(ans){

                            //No further Child
                            if(node.left.left == null && node.left.right == null){
                                if(node == node.parent.left){
                                    node.parent.left = node.left;
                                    node.left.parent = node.parent;
                                    node.left.right = node.right;
                                    node.left = null;
                                    return true;
                                }
                                else{
                                    node.parent.right = node.left;
                                    node.left.parent = node.parent;
                                    node.left.right = node.right;
                                    node.left = null;
                                    return true;
                                }
                                
                            }

                            //Child has further more one child
                            else{

                                //grandchild is on left side (right-right rotation)
                                if(node.left.left != null && node.left.right ==  null){
                                    deletionSplayTree(node.left.left);
                                }

                                //grandchild is on right side (left-right rotation)
                                else if(node.left.left == null && node.left.right !=  null){
                                    deletionSplayTree(node.left.right);
                                }

                                //Child had further more two childrens.
                                else{

                                    //Check who is recently accessed
                                    boolean ans2 = findLastAccessElement(node.left.left.key, node.left.right.key);
    
                                    //If left child is recently accessed than true, else false
                                    if(ans2){

                                        //right-right rotation
                                        deletionSplayTree(node.left.left);
                                    }
                                    else{

                                        //left-right rotation
                                        deletionSplayTree(node.left.right);
                                    }
                                }
                            }
                        }

                        //Right child recently accessed
                        else{

                            //No further Child
                            if(node.right.left == null && node.right.right == null){
                                if(node == node.parent.left){
                                    node.parent.left = node.right;
                                    node.right.parent = node.parent;
                                    node.right = null;
                                    return true;
                                }
                                else{
                                    node.parent.right = node.right;
                                    node.right.parent = node.parent;
                                    node.right = null;
                                    return true;
                                }
                                
                            }

                            //Child has further more one child
                            else{

                                //grandchild is on left side (right-left rotation)
                                if(node.right.left != null && node.right.right ==  null){
                                    deletionSplayTree(node.right.left);
                                }

                                 //grandchild is on right side (left-left rotation)
                                else if(node.right.left == null && node.right.right !=  null){
                                    deletionSplayTree(node.right.right);
                                }

                                //Child had further more two childrens.
                                else{

                                    //Check who is recently accessed
                                    boolean ans3 = findLastAccessElement(node.right.left.key, node.right.right.key);
    
                                    //If left child is recently accessed than true, else false
                                    if(ans3){

                                        //right-left rotation
                                        deletionSplayTree(node.right.left);
                                    }
                                    else{

                                        //left-left rotation
                                        deletionSplayTree(node.right.right);
                                    }
                                }
                            }
                        }        
                    }
                }
            }
        }

        //Recursively search to left subtree.
        boolean leftAns = findDeletionNode(node.left, key);

        //Recursively search to right subtree.
        boolean rightAns = findDeletionNode(node.right, key);


        return (leftAns || rightAns);
    }


    /**
     * This function is used to get which child is recently accessed. We just traverse from end of the access array and see which value left or right node value comes first.
     * @param leftChild : Left Node value
     * @param rightChild : Right Node value
     * @return : true if left child is recently accessed, else return false. If we rebuilt the tree, than all the node have same accesss time, in that case we take left child for rotation because it is lexicographically smaller.
     */
    private boolean findLastAccessElement(String leftChild, String rightChild){

        if(accessIndex == 0){
            return true;
        }

        for(int i=access.length-1; i>=0; i--){
            if(access[i] != null){
                if(access[i].equalsIgnoreCase(leftChild)){
                    return true;
                }
                if(access[i].equalsIgnoreCase(rightChild)){
                    return false;
                }
            }
        }

        return false;
    }


    /**
     * This function is used during rotation in the deletion of one particular node.
     * @param deletionGrandParent : The node which we want to delete its grandchild will be passed here as a parameter.
     */
    private void deletionSplayTree(Node deletionGrandParent){
        Node parent = deletionGrandParent.parent;
        Node grandParent = parent.parent;

        //left-left
        if(deletionGrandParent == deletionGrandParent.parent.left && parent == parent.parent.left){
            right_rotation(grandParent);
            right_rotation(parent);
        }

        //right-right
        else if(deletionGrandParent == deletionGrandParent.parent.right && parent == parent.parent.right){
            left_rotation(grandParent);
            left_rotation(parent);
        }

        //right-left
        else if(deletionGrandParent == deletionGrandParent.parent.left && parent == parent.parent.right){
            right_rotation(parent);
            left_rotation(grandParent);
        }

        //left-right
        else{
            left_rotation(parent);
            right_rotation(grandParent);
        }
    }



    /**
     * This method creates the binary tree.
     * @param pointer : It will maintain the index in the array to which we have to insert in our tree.
     * @param keys : Array of values from which we have to create tree.
     * @param parent : This node used to set the parent for each node.
     * @return : Root of the newly created tree.
     */
    private Node createBinaryTree(int pointer, String[] keys, Node parent){

        if(pointer >= keys.length || keys[pointer] == null){
            return null;
        }

        Node currNode = new Node(keys[pointer].trim());
        currNode.parent = parent;

        //Go to set left child
        currNode.left = createBinaryTree(pointer * 2, keys, currNode);

        //Go to set right child.
        currNode.right = createBinaryTree((pointer * 2) + 1, keys, currNode);

        return currNode;
    }


    /**
     * This method used to set the particular node, as a root node during search and insert operation.
     * @param newNode : The node which we want to make a root node.
     */
    private void SplayTree(Node newNode){

        //Until the parent of the node we want to do operation not become null. 
        while(newNode.parent != null){

            if(newNode.parent == root){
                if(newNode == newNode.parent.left){
                    //node is on the left of the root node.
                    right_rotation(newNode.parent);
                }
                else{
                    //node is on the right of the root node.
                    left_rotation(newNode.parent);
                }
            }

            //
            else{
                Node parent = newNode.parent;
                Node grandParent = parent.parent;

                //left-left
                if(newNode == newNode.parent.left && parent == parent.parent.left){
                    right_rotation(grandParent);
                    right_rotation(parent);
                }

                //right-right
                else if(newNode == newNode.parent.right && parent == parent.parent.right){
                    left_rotation(grandParent);
                    left_rotation(parent);
                }

                //right-left
                else if(newNode == newNode.parent.left && parent == parent.parent.right){
                    right_rotation(parent);
                    left_rotation(grandParent);
                }

                //left-right
                else{
                    left_rotation(parent);
                    right_rotation(grandParent);
                }
            }
        }
    }


    /**
     * This method is used to rotate the node parent on right hand side
     * @param node : Node which we want to move upward.
     */
    private void right_rotation(Node node) {
        Node parent = node.parent;
        Node temp = node.left;
        node.left = temp.right;
        if (temp.right != null) {
            temp.right.parent = node;
        }
        temp.right = node;
        node.parent = temp;
        if (parent != null) {
            if (parent.left == node) {
                parent.left = temp;
            } else {
                parent.right = temp;
            }
        }
        temp.parent = parent;
        if (node == root) {
            root = temp;
        }
    }
    

    /**
     * This method is used to rotate the node parent on left hand side
     * @param node : Node which we want to move upward.
     */
    private void left_rotation(Node node) {
        Node parent = node.parent;
        Node temp = node.right;
        node.right = temp.left;
        if (temp.left != null) {
            temp.left.parent = node;
        }
        temp.left = node;
        node.parent = temp;
        if (parent != null) {
            if (parent.left == node) {
                parent.left = temp;
            } else {
                parent.right = temp;
            }
        }
        temp.parent = parent;
        if (node == root) {
            root = temp;
        }
    }

    private void increaseSizeOfArray() {
        int newSize = accessSize * 2;
        accessSize = newSize;
        String[] newArray = Arrays.copyOf(access, accessSize);
        access = newArray;
    }
}