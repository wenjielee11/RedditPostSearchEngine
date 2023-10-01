// --== CS400 Project One File Header ==--
// Name: Wen Jie Lee
// CSL Username: wlee
// Email: wlee298@wisc.edu
// Lecture 2: 1pm TF
// Notes to Grader: Please don't be mad Please don't be mad Please don't be mad Please don't be mad Please don't be mad
//                  Please don't be mad Please don't be mad Please don't be mad Please don't be mad Please don't be mad

/**
 * This wrapper class contains a node of a key-value pair of the hashtable
 */
public class Node<KeyType, ValueType> {
    private ValueType val; //value of the node
    private KeyType key; //Key mapping the value, which has a unique key value.
    private boolean isDeleted; //used for a dummyNode to indicate a node was recently removed there

    /**
     * Constructor to create a node
     *
     * @param key Type of key and the value of key used to map the value
     * @param val Value mapped by the key
     */
    public Node(KeyType key, ValueType val) {
        this.val = val;
        this.key = key;
        isDeleted = false;
    }

    /**
     * Constrructor used for creating a dummyNode that will replace nodes being removed.
     *
     * @param key       Type of key and the value of key used to map the value
     * @param val       Type and the Value mapped by the key
     * @param isDeleted true if dummyNode is replacing the node being removed
     */
    public Node(KeyType key, ValueType val, boolean isDeleted) {
        this(key, val);
        this.isDeleted = isDeleted;
    }

    /**
     * Checks if the node is a dummyNode.
     *
     * @return true if node is a dummyNode used for replacing deleted nodes, false if a legitimate hashtable node
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Gets the value of the node
     *
     * @return value of the node
     */
    public ValueType getValue() {
        return this.val;
    }

    /**
     * Gets the key of the node
     *
     * @return key of the node
     */
    public KeyType getKey() {
        return this.key;
    }

}
