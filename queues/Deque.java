import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private final StuffNode sentinel;
    private int size;

    private class StuffNode {
        private final Item item;
        private StuffNode prev;
        private StuffNode next;

        private StuffNode(Item i, StuffNode p, StuffNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public Deque() {
        size = 0;
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
        size += 1;
        StuffNode added = new StuffNode(item, sentinel, sentinel.next);
        sentinel.next.prev = added;
        sentinel.next = added;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
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

    private void printDeque() {
        StuffNode p = sentinel;
        while (p.next != sentinel) {
            System.out.printf("%s ", p.next.item);
            p = p.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty");
        }
        size -= 1;
        StuffNode first = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return first.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty");
        }
        size -= 1;
        StuffNode last = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return last.item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private StuffNode p;

        DequeIterator() {
            p = sentinel;
        }

        @Override
        public boolean hasNext() {
            return p.next != sentinel;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no next items.");
            }
            p = p.next;
            return p.item;
        }

        @Override
        public void remove() {
            throw new  UnsupportedOperationException(
                    "Can't call remove() on DequeIterator");
        }
    }
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        Deque<Integer> dq2 = new Deque<>();
        for (int i = 0; i < 10; i++) {
            dq.addFirst(i);
        }
        dq.printDeque();
        System.out.printf("size: %d\n", dq.size());
        for (int i = 10; i < 20; i++) {
            dq2.addLast(i);
        }
        dq2.printDeque();
        System.out.printf("size: %d\n", dq2.size());
        System.out.printf("removed item: %d\n", dq.removeFirst());
        dq.printDeque();
        System.out.printf("removed item: %d\n", dq2.removeLast());
        dq2.printDeque();
        Iterator<Integer> dqIterator = dq.iterator();
        Iterator<Integer> dqIterator2 = dq2.iterator();
        for (int i = 0; i < 9; i++) {
            System.out.printf("item %d: %d\n", i, dqIterator.next());
        }
        for (int i = 0; i < 9; i++) {
            System.out.printf("item %d: %d\n", i, dqIterator2.next());
        }
        System.out.printf("size: %d\n", dq.size());
    }
}
