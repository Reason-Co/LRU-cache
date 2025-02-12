import java.util.Arrays;
import java.util.HashMap;

public class LRUCache {
    /** A hashmap holding references to our nodes */
    HashMap<String, Node> map = new HashMap<>();

    /** Index of first element in the list */
    private int headIndex;
    /** Index of last element in the list */
    private int tailIndex;
    /** Index of first available space */
    private int avail;
    /** Array to store the nodes */
    private final Node[] cache;

    /**
     * Creates a new StaticLinkedList with specified size.
     * Initializes the available list to connect all empty spaces.
     * 
     * @param size maximum number of elements
     * @throws IllegalArgumentException if size ≤ 0
     */
    public LRUCache(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        // Create array of nodes
        cache = new Node[size];

        // Initialize available list
        // Links all spaces: 0 → 1 → 2 → ... → (size-1) → -1
        for (int i = 0; i < size; i++) {
            Node newNode = new Node();
            cache[i] = newNode;
            cache[i].setNextIndex((i == size - 1) ? -1 : i + 1);
            cache[i].setPrevIndex((i == 0) ? -1 : i - 1);
        }


        headIndex = tailIndex = -1; // Empty list
        avail = 0; // All spaces available
    }

    /** Checks if cache is full
     *  @return true if the cache is full and false otherwise
     *  */ 
    public boolean isFull() {
        return avail == -1;
    }

    /** Checks if cache is empty
     *  @return true if the cache is empty and false otherwise
     *  */ 
    public boolean isEmpty() {
        return headIndex == -1;
    }


    /**
     * Inserts a node to the cache
     * If there is no available space it removes the lRU node from the tail
     * and places the new node at the newly available space
     * @param key  the hash identifier
     * @param data the value to be stored in the new node
     */
    public void put(String key, String data) {
        Node node = new Node(data, -1, -1);
        int currentAvail = avail;

        if (isFull()) {
            System.out.println("Full!");
		    currentAvail = makeSpace();
            avail = currentAvail;
        }


        if (isEmpty()) {
            headIndex = tailIndex = currentAvail;
        }

        if (!isFull()) {
            avail = cache[avail].getNextIndex();
        }

        cache[currentAvail] = node;
        map.put(key, node);

        System.out.println("current available: " + currentAvail);
        System.out.println("Head: " + headIndex);
        System.out.println("Avail: " + avail);
        moveToHead(key, currentAvail);
    }

    private int makeSpace() {
        int newSpace = tailIndex;

        // Remove the key associated with the evicted node from the map
        for (String key : map.keySet()) {
            if (map.get(key) == cache[newSpace]) {
                map.remove(key);
                break;
            }
        }
        tailIndex = cache[newSpace].getPrevIndex();
        cache[tailIndex].setNextIndex(-1);
        cache[newSpace].setPrevIndex(-1);
        cache[newSpace].setNextIndex(avail);
        System.out.println("new space: " + newSpace);
        return newSpace;
    }

    void displayCache() {
        System.out.println(map);
    }

    /**
     * Returns the data found in the desired node
     * then moves the node to the head since it's the MRU node.
     * @param key hash key for the intended node
     * @return value of the desired node or null if there exists no such node
     */
    public String get(String key) {
        if(map.get(key) != null){
            Node fetchedNode = map.get(key);
            System.out.println( "node index: "+ cache[fetchedNode.getPrevIndex()].getNextIndex());
            moveToHead(key, cache[fetchedNode.getPrevIndex()].getNextIndex());
            return map.get(key).getValue();
        }

        return null;
    }

    /** Checks whether the node is new
     * @param node The node to be checked
     * @return true if the node is new and false otherwise
     * */
    private boolean isNewNode(Node node) {
        return (node.getNextIndex() == node.getPrevIndex()) && (node.getPrevIndex() == -1);
    }

    /** Moves the MRU node to be the new head
     * @param key The key associated with the node to be moved
     * @param index The index at which the new node is situated.
     */
    public void moveToHead(String key, int index) {
        Node node = map.get(key); // Retrieve the node's reference from the map
        if(cache[headIndex] == node){ return; } // If the node is the current head, do nothing.
        if(!isNewNode(node)){
             System.out.println("Turns out it wasn't a new node");
            // The sandwiched node between its predecessor and successor
            // is orphaned when setting the predecessor to point to its successor and vice versa.
            cache[node.getPrevIndex()].setNextIndex(node.getNextIndex());
            if(node.getNextIndex() != -1){
                cache[node.getNextIndex()].setPrevIndex(node.getPrevIndex());
            }else{
                tailIndex = cache[tailIndex].getPrevIndex(); 
            }
            }
            

        cache[headIndex].setPrevIndex(index); // The current head is set to  point to the new node as its predecessor
        cache[index].setNextIndex(headIndex); // The new node is set to point to the current head as its successor
        cache[index].setPrevIndex(-1);
        headIndex = index; // The new node becomes the new head
    }

        
    public static void main(String[] args) {
        // Initialize cache with size 5
        System.out.println("Creating LRU Cache with size 5...\n");
        LRUCache lruCache = new LRUCache(5);

        // Demonstrate initial insertions
        System.out.println("=== Adding 5 items to cache ===");
        lruCache.put("1", "First Item");
        lruCache.put("2", "Second Item");
        lruCache.put("3", "Third Item");
        lruCache.put("4", "Fourth Item");
        lruCache.put("5", "Fifth Item");

        // Display current state
        System.out.println("\n=== Current Cache State ===");
        System.out.println("Cache contents (value, prevIndex, nextIndex):");
        for (Node node : lruCache.cache) {
            System.out.printf("%-12s [prev: %2d, next: %2d]%n", 
                node.getValue(), node.getPrevIndex(), node.getNextIndex());
        }

        // Demonstrate cache eviction
        System.out.println("\n=== Adding 2 more items (should evict oldest) ===");
        lruCache.put("6", "Sixth Item");
        lruCache.put("7", "Seventh Item");

        // Demonstrate accessing existing item
        System.out.println("\n=== Accessing 'Third Item' ===");
        String result = lruCache.get("3");
        System.out.println("Retrieved: " + result);

        // Display final state
        System.out.println("\n=== Final Cache State ===");
        System.out.println("Head: " + lruCache.cache[lruCache.headIndex].getValue());
        System.out.println("Tail: " + lruCache.cache[lruCache.tailIndex].getValue());
        System.out.println("Next available index: " + lruCache.avail);

        // Add demonstration of HashMap-Cache synchronization
        System.out.println("\n=== HashMap and Cache Synchronization ===");
        System.out.println("HashMap contents:");
        for (String key : lruCache.map.keySet()) {
            System.out.printf("Key: %-2s | Value: %-12s%n", 
                key, lruCache.map.get(key).getValue());
        }

        System.out.println("\nCache array contents:");
        for (int i = 0; i < lruCache.cache.length; i++) {
            Node node = lruCache.cache[i];
            System.out.printf("Index: %d | Value: %-12s | Prev: %2d | Next: %2d%n",
                i, node.getValue(), node.getPrevIndex(), node.getNextIndex());
        }

        // Verify the linked list structure
        System.out.println("\nTraversing from Head to Tail:");
        int current = lruCache.headIndex;
        while (current != -1) {
            System.out.printf("Index: %d | Value: %s%n", 
                current, lruCache.cache[current].getValue());
            current = lruCache.cache[current].getNextIndex();
        }
    }
}