package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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

    /* fuck autograder! */
    private void preView(BSTNode now, Set<K> ans) {
        if (now != null) {
            ans.add(now.key);
            preView(now.left, ans);
            preView(now.right, ans);
        }
        return;
    }
    @Override
    public Set<K> keySet() {
        Set<K> answer = new TreeSet<>();

        preView(root, answer);

        return answer;
    }

    private V remove(BSTNode now, K key) {
        if (now == null) {
            return null;
        }
        else {
            int cmp = key.compareTo(now.key);

            if (cmp < 0) {
                return remove(now.left, key);
            }
            else if (cmp > 0) {
                return remove(now.right, key);
            }
            else if (now.left != null && now.right != null) {
                BSTNode targetFather = now;
                BSTNode target = now.right;

                while (target.left != null) {
                    targetFather = target;
                    target = target.left;
                }

                V value = now.value;
                now.key = target.key;
                now.value = target.value;

                if (now == targetFather) {
                    now.right = target.right;
                } else {
                    targetFather.left = target.right;
                }

                return value;
            }
            else {
                V value = now.value;
                if (root == now) {
                    now = (now.left != null) ? now.left : now.right;
                    root = now;
                } else {
                    now = (now.left != null) ? now.left : now.right;
                }
                return value;
            }
        }
    }
    @Override
    public V remove(K key) {
        size -= 1;
        return remove(root, key);
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        System.out.println("1");
    }
}
