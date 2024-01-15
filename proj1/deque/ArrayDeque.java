package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] array;
    private int first;
    private int last;
    private int size;
    private int capacity;

    public ArrayDeque() {
        array = (T[]) new Object[8];
        first = 3;
        last = 4;
        size = 0;
        capacity = 8;
    }

    private void bigSize(int newcapacity) {
        T[] newarray = (T[]) new Object[newcapacity];

        System.arraycopy(array, 0, newarray, 0, last);
        System.arraycopy(array, last, newarray, newcapacity - capacity + last, capacity - last);

        first = newcapacity - capacity + last - 1;
        capacity = newcapacity;
        array = newarray;
    }

    private void smallSize(int newcapacity) {
        T[] newarray = (T[]) new Object[newcapacity];

        int start = lastForCycle(first);
        int end = firstForCycle(last);
        if (start < end) {
            System.arraycopy(array, start, newarray, 0, end - start + 1);
        } else {
            System.arraycopy(array, start, newarray, 0, capacity - start);
            System.arraycopy(array, 0, newarray, capacity - start, end + 1);
        }

        first = newcapacity - 1;
        last = (capacity + end - start + 1) % capacity;
        capacity = newcapacity;
        array = newarray;
    }

    private int firstForCycle(int i) {
        i -= 1;
        i = (i + capacity) % capacity;
        return i;
    }

    private int lastForCycle(int i) {
        i += 1;
        i = i % capacity;
        return i;
    }

    @Override
    public void addFirst(T i) {
        if (size == capacity) {
            bigSize(capacity * 2);
        }

        array[first] = i;
        first = firstForCycle(first);
        size += 1;
    }

    @Override
    public void addLast(T i) {
        if (size == capacity) {
            bigSize(capacity * 2);
        }

        array[last] = i;
        last = lastForCycle(last);
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int start = lastForCycle(first);
        int end = firstForCycle(last);

        while (start != end) {
            System.out.print(array[start] + " ");
            start = lastForCycle(start);
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && size - 1 < capacity / 4) {
            smallSize(capacity / 2);
        }

        first = lastForCycle(first);
        T ans = array[first];
        array[first] = null;
        size -= 1;

        return ans;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && size - 1 < capacity / 4) {
            smallSize(capacity / 2);
        }

        last = firstForCycle(last);
        T ans = array[last];
        array[last] = null;
        size -= 1;

        return ans;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index <= size - 1) {
            int start = lastForCycle(first);

            while (index != 0) {
                start = lastForCycle(start);
                index -= 1;
            }

            return array[start];
        } else {
            return null;
        }
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;

        ArrayDequeIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T returnT = get(pos);
            pos += 1;
            return returnT;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ArrayDeque) {
            ArrayDeque test = (ArrayDeque) o;
            if (this.size() == test.size()) {
                for (int pos = 0; pos < size; pos++) {
                    T param1 = get(pos);
                    Object param2 = test.get(pos);
                    if (!param1.equals(param2)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } else if (o instanceof deque.LinkedListDeque) {
            deque.LinkedListDeque test = (deque.LinkedListDeque) o;
            if (this.size() == test.size()) {
                for (int pos = 0; pos < size; pos++) {
                    T param1 = get(pos);
                    Object param2 = test.get(pos);
                    if (!param1.equals(param2)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
