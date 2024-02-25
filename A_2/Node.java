
//It is the structure of each node of our tree.
public class Node {
    
    //Value of the node.
    String key;

    //Store the parent address of that node. For root node it will null.
    Node parent;

    //Left and right store the address of left and right child of that particular node.
    Node left,right;


    /**
     * Constructor which create and set the value of newly created node as the paremeter passed to it.
     * @param key : Value of the new node.
     */
    public Node(String key){
        this.key = key;
        parent = null;
        left = null;
        right = null;
    }

}
