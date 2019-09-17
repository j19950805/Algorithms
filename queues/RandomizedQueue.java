import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int next;

    public RandomizedQueue() {
        size = 0;
        items = (T[]) new Object[8];
        next = 0;
    }

    private void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];
        System.arraycopy(items, 0, resized, 0, size);
        items = resized;
    }

    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
        size += 1;
        items[next] = item;
        next += 1;
        if (next >= items.length) {
            resize(items.length * 2);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int randomIndex = StdRandom.uniform(size);
        T item = items[randomIndex];
        int tail = size - randomIndex - 1;
        System.arraycopy(items, randomIndex + 1, items, randomIndex, tail);
        next -= 1;
        items[next] = null;
        size -= 1;

        if (((float) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return item;
    }

    public T sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int randomIndex = StdRandom.uniform(size);
        return items[randomIndex];
    }

    @Override
    public Iterator<T> iterator() {
        return new RQIterator<>(this);
    }

    private class RQIterator<T> implements Iterator<T> {
        private int index;
        private RandomizedQueue<T> rq;

        RQIterator(RandomizedQueue<T> rq) {
            index = 0;
            this.rq = rq;
        }

        @Override
        public boolean hasNext() {
            return index < rq.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no next items.");
            }
            T item = rq.items[index];
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
        int p = 0;
        while (p < size) {
            System.out.printf("%s ", items[p]);
            p += 1;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        rq.printDeque();
        System.out.printf("size: %d\n", rq.size());
        for (int i = 0; i < 5; i++) {
            System.out.printf("sample %d: %d\n", i, rq.sample());
        }
        Iterator<Integer> rqIterator = rq.iterator();
        for (int i = 0; i < 10; i++) {
            System.out.printf("item %d: %d\n", i, rqIterator.next());
        }
        System.out.printf("size: %d\n", rq.size());
        for (int i = 0; i < 10; i++) {
            System.out.printf("removed item: %d\n", rq.dequeue());
            rq.printDeque();
        }
    }
}
