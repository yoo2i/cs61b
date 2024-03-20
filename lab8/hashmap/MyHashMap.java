package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author yoo2i
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private Set<K> keySet;
    private final int initialSize;
    private final double loadFactor;//float会报错
    private int itemNum;
    private int bucketNum;

    /** Constructors */
    public MyHashMap() {
        initialSize = 16;
        loadFactor = 0.75;

        buckets = createTable(initialSize);
        keySet = new HashSet<>();

        itemNum = 0;
        bucketNum = initialSize;
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        loadFactor = 0.75;

        buckets = createTable(initialSize);
        keySet = new HashSet<>();

        itemNum = 0;
        bucketNum = initialSize;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;

        buckets = createTable(initialSize);
        keySet = new HashSet<>();

        itemNum = 0;
        bucketNum = initialSize;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];

        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }

        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int getIndex(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, bucketNum);
    }

    private void maxBuckets() {
        MyHashMap<K, V> newMyHashMap = new MyHashMap<>(bucketNum * 2, this.loadFactor);

        for (K key : keySet) {
            newMyHashMap.put(key, get(key));
        }

        buckets = newMyHashMap.buckets;
        bucketNum = newMyHashMap.bucketNum;
    }

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        keySet.clear();
        itemNum = 0;
        bucketNum = initialSize;
    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);

        for (Node tmp : buckets[index]) {
            if (key.equals(tmp.key)) {
                return tmp.value;
            }
        }

        return null;
    }

    @Override
    public int size() {
        return itemNum;
    }

    @Override
    public void put(K key, V value) {
        Node node = createNode(key, value);
        int index = getIndex(key);

        if (containsKey(key)) {
            for (Node tmp : buckets[index]) {
                if (key.equals(tmp.key)) {
                    tmp.value = value;
                    return;
                }
            }
        } else {
            buckets[index].add(node);
            itemNum += 1;
            keySet.add(key);

            if ((double) itemNum / bucketNum > loadFactor) {
                maxBuckets();
            }
        }
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

}
