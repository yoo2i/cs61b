package deque;

public class LinkedListDeque<Item> {
    private class InternalNode {
        public Item content;
        public InternalNode pre;
        public InternalNode next;

        public InternalNode(Item i,InternalNode pre,InternalNode next) {
            this.content = i;
            this.pre = pre;
            this.next = next;
        }
    }
    private InternalNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new InternalNode(null,null,null);
        sentinel.next = sentinel;
        sentinel.pre = sentinel;
        size = 0;
    }

    public void addFirst(Item i) {
        InternalNode tmp = sentinel.next;
        InternalNode ans = new InternalNode(i,sentinel,tmp);
        sentinel.next = ans;
        tmp.pre = ans;
        size += 1;
    }

    public void addLast(Item i) {
        InternalNode tmp = sentinel.pre;
        InternalNode ans = new InternalNode(i,tmp,sentinel);
        tmp.next = ans;
        sentinel.pre =ans;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        InternalNode tmp = sentinel.next;
        while (tmp != sentinel) {
            System.out.print(tmp.content+" ");
            tmp = tmp.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if(size == 0) {
            return null;
        }

        InternalNode tmp = sentinel.next.next;
        Item ans = sentinel.next.content;

        sentinel.next = tmp;
        tmp.pre = sentinel;
        size -= 1;

        return ans;
    }

    public Item removeLast() {
        if(size == 0) {
            return null;
        }

        InternalNode tmp = sentinel.pre.pre;
        Item ans = sentinel.pre.content;

        tmp.next = sentinel;
        sentinel.pre = tmp;
        size -= 1;

        return ans;
    }

    public Item get(int index) {
        if (index < 0) {
            return null;
        }
        InternalNode tmp = sentinel.next;

        while(index != 0) {
            if (tmp == sentinel) return null;
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

        if(index == 0 || tmp == sentinel){
            return tmp.content;
        }

        return getRecursive(index - 1, tmp.next);
    }
}
