import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public Deque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    private void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];
        int initFirst = arrayAdd(nextFirst, 1);
        if (nextLast <= nextFirst) {
            int tailLength = items.length - nextFirst - 1;
            int newFirst = (capacity - tailLength) % capacity;
            System.arraycopy(items, 0, resized, 0, nextLast);
            System.arraycopy(items, initFirst, resized, newFirst, tailLength);
            items = resized;
            nextFirst = arrayAdd(newFirst, -1);
        } else {
            System.arraycopy(items, initFirst, resized, 4, size);
            items = resized;
            nextFirst = 3;
            nextLast = size + 4;
        }
    }

    private int arrayAdd(int index, int addend) {
        int res = (index + addend) % items.length;
        if (res >= 0) {
            return res;
        } else {
            return items.length + res;
        }
    }

    public void addFirst(T item) {
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

    public void addLast(T item) {
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

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        size -= 1;
        nextFirst = arrayAdd(nextFirst, 1);
        T first = items[nextFirst];
        items[nextFirst] = null;

        if (((float) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return first;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        size -= 1;
        nextLast = arrayAdd(nextLast, -1);
        T last = items[nextLast];
        items[nextLast] = null;

        if (((float) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return last;
    }

    private T get(int index) {
        if (index > size) {
            return null;
        }
        return items[arrayAdd(nextFirst, index + 1)];
    }

    @Override
    public Iterator<T> iterator() {
        return new DequeIterator<>(this);
    }

    private class DequeIterator<T> implements Iterator<T> {
        private int index;
        private Deque<T> dq;

        DequeIterator(Deque<T> dq) {
            index = 0;
            this.dq = dq;
        }

        @Override
        public boolean hasNext() {
            return index < dq.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no next items.");
            }
            T item = dq.get(index);
            index += 1;
            return item;
        }

        @Override
        public void remove() {
            throw new  UnsupportedOperationException(
                       "Can't call remove() on DequeIterator");
        }
    }

    private void printDeque() {
        int p = arrayAdd(nextFirst, 1);
        while (p != nextLast) {
            System.out.printf("%s ", items[p]);
            p = arrayAdd(p, 1);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        for (int i = 0; i < 10; i++) {
            dq.addFirst(i);
        }
        dq.printDeque();
        System.out.printf("size: %d\n", dq.size());
        for (int i = 10; i < 20; i++) {
            dq.addLast(i);
        }
        dq.printDeque();
        System.out.printf("size: %d\n", dq.size());
        System.out.printf("removed item: %d\n", dq.removeFirst());
        dq.printDeque();
        System.out.printf("removed item: %d\n", dq.removeLast());
        dq.printDeque();
        Iterator<Integer> dqIterator = dq.iterator();
        for (int i = 0; i < 18; i++) {
            System.out.printf("item %d: %d\n", i, dqIterator.next());
        }
        System.out.printf("size: %d\n", dq.size());
    }
}
