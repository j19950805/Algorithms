import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        items = (Item[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    private void resize(int capacity) {
        Item[] resized = (Item[]) new Object[capacity];
        for (int i = 1; i <= size; i++) {
            resized[i] = items[arrayAdd(nextFirst, i)];
        }
        items = resized;
        nextFirst = 0;
        nextLast = size + 1;
    }

    private int arrayAdd(int index, int addend) {
        int res = (index + addend) % items.length;
        if (res >= 0) {
            return res;
        } else {
            return items.length + res;
        }
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
        size += 1;
        items[nextFirst] = item;
        nextFirst = arrayAdd(nextFirst, -1);
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
        size += 1;
        items[nextLast] = item;
        nextLast = arrayAdd(nextLast, 1);
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty");
        }
        size -= 1;
        nextFirst = arrayAdd(nextFirst, 1);
        Item first = items[nextFirst];
        items[nextFirst] = null;

        if (((float) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return first;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty");
        }
        size -= 1;
        nextLast = arrayAdd(nextLast, -1);
        Item last = items[nextLast];
        items[nextLast] = null;

        if (((float) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return last;
    }

    private Item get(int index) {
        if (index > size) {
            return null;
        }
        return items[arrayAdd(nextFirst, index + 1)];
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayDequeIterator<>(this);
    }

    private class ArrayDequeIterator<Item> implements Iterator<Item> {
        private int index;
        private ArrayDeque<Item> dq;

        ArrayDequeIterator(ArrayDeque<Item> dq) {
            index = 0;
            this.dq = dq;
        }

        @Override
        public boolean hasNext() {
            return index < dq.size();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no next items.");
            }
            Item item = dq.get(index);
            index += 1;
            return item;
        }

        @Override
        public void remove() {
            throw new  UnsupportedOperationException(
                       "Can't call remove() on ArrayDequeIterator");
        }
    }

    private void printArrayDeque() {
        int p = arrayAdd(nextFirst, 1);
        while (p != nextLast) {
            System.out.printf("%s ", items[p]);
            p = arrayAdd(p, 1);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            dq.addFirst(i);
        }
        dq.printArrayDeque();
        System.out.printf("size: %d\n", dq.size());
        for (int i = 10; i < 20; i++) {
            dq.addLast(i);
        }
        dq.printArrayDeque();
        System.out.printf("size: %d\n", dq.size());
        System.out.printf("removed item: %d\n", dq.removeFirst());
        dq.printArrayDeque();
        System.out.printf("removed item: %d\n", dq.removeLast());
        dq.printArrayDeque();
        Iterator<Integer> dqIterator = dq.iterator();
        for (int i = 0; i < 18; i++) {
            System.out.printf("item %d: %d\n", i, dqIterator.next());
        }
        System.out.printf("size: %d\n", dq.size());
    }
}
