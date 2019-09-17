public class LinkedListDeque<T> {
    private StuffNode sentinel;
    private int size;

    public class StuffNode {
        public T item;
        public StuffNode prev;
        public StuffNode next;

        public StuffNode(T i, StuffNode p, StuffNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public LinkedListDeque(LinkedListDeque<T> other) {
        size = other.size();
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        StuffNode p = other.sentinel;
        while (p.next != other.sentinel) {
            addLast(p.next.item);
            p = p.next;
        }
    }

    public void addFirst(T item) {
        size += 1;
        StuffNode added = new StuffNode(item, sentinel, sentinel.next);
        sentinel.next.prev = added;
        sentinel.next = added;
    }

    public void addLast(T item) {
        size += 1;
        StuffNode added = new StuffNode(item, sentinel.prev, sentinel);
        sentinel.prev.next = added;
        sentinel.prev = added;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
    	return size == 0;
    }

    public void printDeque() {
        StuffNode p = sentinel;
        while (p.next != sentinel) {
            System.out.printf("%s ", p.next.item);
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        StuffNode first = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return first.item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        StuffNode last = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return last.item;
    }

    public T get(int index) {
        StuffNode p = sentinel;
        while (index >= 0) {
            if (p.next == sentinel) {
                return null;
            }
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        return getHelper(sentinel, index);
    }

    private T getHelper(StuffNode p, int i) {
        if (p.next == this.sentinel) {
            return null;
        }
        if (i == 0) {
            return p.next.item;
        }
        return getHelper(p.next, i - 1);
    }

}
