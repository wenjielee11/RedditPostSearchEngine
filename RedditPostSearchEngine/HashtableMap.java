// --== CS400 Project One File Header ==--
// Name: Wen Jie Lee
// CSL Username: wlee
// Email: wlee298@wisc.edu
// Lecture 2: 1pm TF
// Notes to Grader: Please don't be mad Please don't be mad Please don't be mad Please don't be mad Please don't be mad
//                  Please don't be mad Please don't be mad Please don't be mad Please don't be mad Please don't be mad

import java.util.NoSuchElementException;

/**
 * This class implements the Hashtable data structure that is based on open addressing and simple linear probing as the hash
 * collision solution.
 *
 * @param <KeyType>   The type of key that will map to value
 * @param <ValueType> The type of value paired with key
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
    protected Node<KeyType, ValueType>[] hashTable; // Oversized array storing wrapper nodes containing the key-value pairs
    private int size; // size to keep track of oversized array implementation

    /**
     * Default constructor with no parameters, creating a default hashtable of capacity 8
     */
    @SuppressWarnings("unchecked")
    public HashtableMap() {
        hashTable = (Node<KeyType, ValueType>[]) new Node[8];
        this.size = 0;
    }

    /**
     * Paramterized constructor that creates an array of a capacity
     *
     * @param capacity the length of the array to be created
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Error, capacity cannot be <=0");
        }
        hashTable = (Node<KeyType, ValueType>[]) new Node[capacity];
        this.size = 0;
    }

    /**
     * add a new key-value pair/mapping to this collection
     * throws exception when key is null or duplicate of one already stored
     *
     * @param key   key to add
     * @param value value of the key
     * @throws IllegalArgumentException when key is null or duplicate
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        //This put implementation does not use containsKey to avoid O(2N) complexity. It uses a seperate var to
        //keep track of a dummy node for insertion, then check the rest of the array for duplicates, which can be
        //done in one iteration.

        if (key == null || value == null) {
            throw new IllegalArgumentException("Error, key or value cant be null");
        }

        //Creating a newnode to be stored inside the hashtable array
        Node newNode = new Node(key, value);
        boolean foundDummy = false;
        int tempIndex = -1;
        int index = hashFunction(key, getCapacity());

        //Open address and linear probing if collision occurs
        for (int i = 0; i < hashTable.length; i++) {

            //If there's an empty slot/with a dummyNode
            if (hashTable[index] == null) {
                break; //Do not have to iterate rest of array to check for duplicate, since no collision.

                //Finds the first occurrence of a dummy node for insertion.
            } else if (hashTable[index].isDeleted()) {
                if (!foundDummy) {
                    tempIndex = index;
                    foundDummy = true;
                } else {
                    index = (index + 1) % hashTable.length;
                    continue;
                }
            } else if (hashTable[index].getKey() != null && hashTable[index].getKey().equals(key)) {
                throw new IllegalArgumentException("Error, key is duplicate");
            }
            index = (index + 1) % hashTable.length;
        }

        if (foundDummy) { //Insert into dummy node
            hashTable[tempIndex] = newNode;
        } else {
            //foundDummy should be false if there was no collision.
            hashTable[index] = newNode;
        }
        size++;

        //Create a new larger hashTable if it exceeds/equals 70% capacity
        if (getLoadFactor() >= 0.7) {
            rehash();
        }

    }

    /**
     * This method creates a new hashtable double the capacity of the old one, then
     * recalculates and reassigns
     * the hash indexes of the elements using the larger capacity, reducing collision rates over
     * more empty spaces, and optimizing future runtimes of put()
     */
    private void rehash() {
        /**
         //O(n^2) space and time approach
         Node<KeyType, ValueType>[] oldArray = hashTable;
         hashTable = (Node<KeyType,ValueType>[]) new Node[oldArray.length*2];
         for(int i=0;i<oldArray.length;i++){
         put(oldArray[i].getKey(),oldArray[i].getValue());
         }
         **/
        //O(n) space approach, have to duplicate code from put(), while optimizing runtime and space by disregarding dummy nodes
        Node<KeyType, ValueType>[] newArray = (Node<KeyType, ValueType>[]) new Node[hashTable.length * 2];
        //O(N^2) worst case runtime
        //Loop through every node in the old array to rehash in newArray, if node exists
        for (int i = 0; i < hashTable.length; i++) {
            //Will continue/skip loop if element is a dummyNode
            if (hashTable[i] != null && (!hashTable[i].isDeleted())) {
                int index = hashFunction(hashTable[i].getKey(), newArray.length);
                //Linear probe if collisions occur
                for (int j = 0; j < newArray.length; j++) {
                    if (newArray[index] == null) {
                        newArray[index] = hashTable[i];
                        break;
                    }
                    index = (index + 1) % newArray.length;
                }
            }
        }
        //Reassign the longer array as the new hashTable here
        hashTable = newArray;
    }

    /**
     * check whether a key maps to a value within this collection
     *
     * @param key to check if the hashtable contains or not
     * @return true if the key exists, false if not
     */
    @Override
    public boolean containsKey(KeyType key) {
        try {
            get(key);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * @param key the key of the value pair that we want to find
     * @return the value of the key pair
     * @throws NoSuchElementException if key is null, or if key does not exist
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        // Prevents NPE's
        if (key == null) {
            throw new NoSuchElementException("Error, key cannot be null");
        }
        // Hashing function
        int index = hashFunction(key, getCapacity());

        //Linear probing if collision occurs
        // O(N) worst case
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[index] == null) {
                //Key does not exist, break and throw NoSuchElementEx
                break;
                // if the node is not a dummynode, and the key matches(means no collision)
            } else if (!hashTable[index].isDeleted() && hashTable[index].getKey().equals(key)) {
                return hashTable[index].getValue();
            }
            // % wraps the index to the beginning if it reaches end of array, and if i<arr.length
            index = (index + 1) % hashTable.length;
        }
        //If element does not exist, throw the exception to be caught in other parts of code
        throw new NoSuchElementException("Error, element does not exist when trying to get with key");
    }

    /**
     * Calculates the hash index of the key
     *
     * @param key to find the hashcode and calculate its index
     * @return the index where the key is located
     */
    private int hashFunction(KeyType key, int capacity) {
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * remove the mapping for a given key from this collection.
     *
     * @param key node with the key to remove
     * @return the value that was removed
     * @throws NoSuchElementException when key is not stored in this collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        //O(N) runtime from removing

        //Key exists, find the key with duplicate code because get() wont return the node, but the value instead. (Why tho?)
        int index = hashFunction(key, getCapacity());
        if (hashTable[index] == null) { //If the first index hashed is null, it does not exist.
            throw new NoSuchElementException("Error, key is not stored in array at index " + index);
        }
        ValueType toReturn = null;
        //Loop through entire array with two pointers i and index
        for (int i = 0; i < getCapacity(); i++) {

            //Skip dummy nodes and empty spots.
            //So the loop will not end on first iteration

            if (hashTable[index] != null && !hashTable[index].isDeleted()) {
                if (hashTable[index].getKey().equals(key)) {
                    //Value found. Save the value to be returned, then replace with a dummyNode, effectively deleting it
                    toReturn = hashTable[index].getValue();
                    hashTable[index] = new Node<>(null, null, true);
                    size--;
                    return toReturn;
                }
            }
            //Continue to move onto the next indexes by linear probing
            index = (index + 1) % getCapacity();

        }
        throw new NoSuchElementException("Error, key is not stored in this collection. Last index stopped at: " + index);
    }

    /**
     * remove all key-value pairs from this collection
     */
    @Override
    public void clear() {
        //Set all elements to null
        for (int i = 0; i < getCapacity(); i++) {
            hashTable[i] = null;
        }
        size = 0;
    }

    /**
     * retrieve the number of keys stored within this collection
     *
     * @return the number of keys stored
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * retrieve this collection's capacity (size of its underlying array)
     *
     * @return capacity with the length of the array
     */
    @Override
    public int getCapacity() {
        return hashTable.length;
    }

    /**
     * Calculates the load factor by dividing the number of pairs in table with
     * The array length
     *
     * @return the load factor
     */
    private double getLoadFactor() {
        return (double) this.size / this.getCapacity();
    }

}

