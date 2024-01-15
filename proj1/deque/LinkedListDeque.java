package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Iterable<Item>, Deque<Item> {
    private class InternalNode {
        public Item content;
        public InternalNode pre;
        public InternalNode next;

        public InternalNode(Item i, InternalNode pre, InternalNode next) {
            this.content = i;
            this.pre = pre;
            this.next = next;
        }
    }
    private InternalNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new InternalNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.pre = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(Item i) {
        InternalNode tmp = sentinel.next;
        InternalNode ans = new InternalNode(i, sentinel, tmp);
        sentinel.next = ans;
        tmp.pre = ans;
        size += 1;
    }

    @Override
    public void addLast(Item i) {
        InternalNode tmp = sentinel.pre;
        InternalNode ans = new InternalNode(i, tmp, sentinel);
        tmp.next = ans;
        sentinel.pre = ans;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        InternalNode tmp = sentinel.next;
        while (tmp != sentinel) {
            System.out.print(tmp.content + " ");
            tmp = tmp.next;
        }
        System.out.println();
    }

    @Override
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }

        InternalNode tmp = sentinel.next.next;
        Item ans = sentinel.next.content;

        sentinel.next = tmp;
        tmp.pre = sentinel;
        size -= 1;

        return ans;
    }

    @Override
    public Item removeLast() {
        if (size == 0) {
            return null;
        }

        InternalNode tmp = sentinel.pre.pre;
        Item ans = sentinel.pre.content;

        tmp.next = sentinel;
        sentinel.pre = tmp;
        size -= 1;

        return ans;
    }

    @Override
    public Item get(int index) {
        if (index < 0) {
            return null;
        }
        InternalNode tmp = sentinel.next;

        while (index != 0) {
            if (tmp == sentinel) {
                return null;
            }
            tmp = tmp.next;
            index -= 1;
        }

        return tmp.content;
    }

    public Item getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }

    private Item getRecursive(int index, InternalNode tmp) {
        if (index < 0) {
            return null;
        }

        if (index == 0 || tmp == sentinel) {
            return tmp.content;
        }

        return getRecursive(index - 1, tmp.next);
    }

    private class LinkedListDequeIterator implements Iterator<Item> {
        private int pos;

        public LinkedListDequeIterator() {
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
    @Override
    public Iterator<Item> iterator() {
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof LinkedListDeque) {
            LinkedListDeque test = (LinkedListDeque) o;
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
