package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
    private int size = 0;
    private BSTNode root = null;

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private BSTNode findKey (K key, BSTNode now) {
        if (now == null) {
            return null;
        }

        int cmp = key.compareTo(now.key);
        if (cmp < 0) {
            return findKey(key, now.left);
        } else if (cmp == 0) {
            return now;
        } else {
            return findKey(key, now.right);
        }
    }
    @Override
    public boolean containsKey(K key) {
        return findKey(key, root) != null;
    }

    @Override
    public V get(K key) {
        BSTNode answer = findKey(key, root);

        if (answer == null) {
            return null;
        } else {
            return answer.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private BSTNode put(K key, V value, BSTNode now) {
        if (now == null) {
            return new BSTNode(key, value);
        }

        int cmp = key.compareTo(now.key);
        if (cmp < 0) {
            now.left = put(key, value, now.left);
        } else if (cmp > 0) {
            now.right = put(key, value, now.right);
        }

        return now;
    }
    @Override
    public void put(K key, V value) {
        size += 1;
        root = put(key, value, root);
    }

    /* remove iterator and keySet are not for now*/
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
