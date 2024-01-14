package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Iterable<Item> {
    private Item[] array;
    private int first;
    private int last;
    private int size;
    private int capacity;

    public ArrayDeque() {
        array = (Item[]) new Object[8];
        first = 3;
        last = 4;
        size = 0;
        capacity = 8;
    }

    private void bigSize(int newcapacity) {
        Item[] newarray = (Item[]) new Object[newcapacity];

        System.arraycopy(array, 0, newarray, 0, last);
        System.arraycopy(array, last, newarray, newcapacity - capacity + last, capacity - last);

        first = newcapacity - capacity + last - 1;
        capacity = newcapacity;
        array = newarray;
    }

    private void smallSize(int newcapacity) {
        Item[] newarray = (Item[]) new Object[newcapacity];

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

    public void addFirst(Item i) {
        if (size == capacity) {
            bigSize(capacity * 2);
        }

        array[first] = i;
        first = firstForCycle(first);
        size += 1;
    }

    public void addLast(Item i) {
        if (size == capacity) {
            bigSize(capacity * 2);
        }

        array[last] = i;
        last = lastForCycle(last);
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int start = lastForCycle(first);
        int end = firstForCycle(last);

        while (start != end) {
            System.out.print(array[start] + " ");
            start = lastForCycle(start);
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && size - 1 < capacity / 4) {
            smallSize(capacity / 2);
        }

        first = lastForCycle(first);
        Item ans = array[first];
        array[first] = null;
        size -= 1;

        return ans;
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && size - 1 < capacity / 4) {
            smallSize(capacity / 2);
        }

        last = firstForCycle(last);
        Item ans = array[last];
        array[last] = null;
        size -= 1;

        return ans;
    }

    public Item get(int index) {
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

    private class ArrayDequeIterator implements Iterator<Item> {
        private int pos;

        public ArrayDequeIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public Item next() {
            Item returnItem = get(pos);
            pos += 1;
            return returnItem;
        }
    }
    public Iterator<Item> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ArrayDeque test) {
            if (this.size() == test.size()) {
                for (int pos = 0; pos < size; pos++) {
                    Item param1 = get(pos);
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
